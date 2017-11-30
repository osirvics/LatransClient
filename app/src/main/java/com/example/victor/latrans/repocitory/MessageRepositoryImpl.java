package com.example.victor.latrans.repocitory;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.example.victor.latrans.amazon.AmazonUtility;
import com.example.victor.latrans.amazon.Constants;
import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.google.NetworkBoundResource;
import com.example.victor.latrans.google.RateLimiter;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.ConversationResponse;
import com.example.victor.latrans.repocitory.local.model.MessageResponse;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Profile;
import com.example.victor.latrans.repocitory.local.model.SingleMessageResponse;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

public class MessageRepositoryImpl implements MessageRepository {

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);
    private APIService mAPIService;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    Context mContext;
    @Inject
    AppExecutors appExecutors;

    @Inject
    public MessageRepositoryImpl(AppDatabase appDatabase,SharedPrefsHelper sharedPrefsHelper, Context context,AppExecutors executors){
        this.mAppDatabase = appDatabase;
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mContext = context;
        this.appExecutors = executors;
    }

    @Override
    public LiveData<Resource<List<ConversationAndMessage>>>  getMessagesAndConversation(long userId) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<ConversationAndMessage>, MessageResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull MessageResponse item) {
                List<Message> messages = item.getMessages();
                persistConversationAndMesaages(messages);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ConversationAndMessage> data) {
                return true;
               // return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("id");
            }

            @NonNull
            @Override
            protected LiveData<List<ConversationAndMessage>> loadFromDb() {

                return mAppDatabase.dialogueDao().getAllDialogue();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MessageResponse>> createCall() {
                return mAPIService.getMessages(userId);
            }

            @Override
            protected void onFetchFailed() {
               repoListRateLimit.reset(String.valueOf(userId));

            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<List<Message>>>  getMessagesInConversation(long conversationId) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<Message>, MessageResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull MessageResponse item) {
                List<Message> messages = item.getMessages();
                persistConversationAndMesaages(messages);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Message> data) {
                return true;
                // return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("id");
            }

            @NonNull
            @Override
            protected LiveData<List<Message>> loadFromDb() {
                return mAppDatabase.messageDao().getAllMessagesInConversation(conversationId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MessageResponse>> createCall() {
                return mAPIService.getMessagesInConversation(conversationId);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(String.valueOf(conversationId));
            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<User>> getUser() {
        return new NetworkBoundResource<User, NewUser>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull NewUser item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return mAppDatabase.userDao().loadUser();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewUser>> createCall() {
                return null;
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<User>> updateUser(long id, Profile profile) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<User, NewUser>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull NewUser item) {
                Timber.e("save called here");
                mAppDatabase.userDao().insertUser(item.getUser());
                mSharedPrefsHelper.setUserProfileUrl(item.getUser().getPicture());
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                Timber.e("fetch called here");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                Timber.e("load called here");
                return mAppDatabase.userDao().loadUser();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NewUser>> createCall() {
                Timber.e("querry called here");
                return mAPIService.updateUser(profile, id);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<UploadResponse>> beginUpload(File file){
        TransferUtility transferUtility = AmazonUtility.getTransferUtility(mContext);
        String key = file.getName();
        TransferObserver observer = transferUtility.upload(
                Constants.BUCKET_NAME,     /* The bucket to upload to */
                key,    /* The key for the uploaded object */
                file       /* The file where the data to upload exists */
        );
             return   transferObserverListener(observer);
    }

    @Override
    public LiveData<Resource<Message>> postMessage(Message message) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<Message, SingleMessageResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull SingleMessageResponse messageResponse) {
                //TODO create converstion, dialogue, before inserting message;
                   // mAppDatabase.messageDao().insertMesaage(messageResponse.getMessage());
                    List<Message> messages = new ArrayList<>(1);
                    messages.add(messageResponse.getMessage());
                    persistConversationAndMesaages(messages);

            }

            @Override
            protected boolean shouldFetch(@Nullable Message data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Message> loadFromDb() {
                return mAppDatabase.messageDao().getAMessageBySender(mSharedPrefsHelper.getUserId());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SingleMessageResponse>> createCall() {
                return mAPIService.postMessage(message);
            }


        }.asLiveData();
    }

    @Override
    public LiveData<Resource<Conversation>> getConversation(long recipient_id) {
        return new NetworkBoundResource<Conversation, ConversationResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull ConversationResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Conversation data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Conversation> loadFromDb() {
                Timber.e("Attempting loading of conversation from db");
                return mAppDatabase.conversationDao().getAConversationByUserId(recipient_id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ConversationResponse>> createCall() {
                return null;
            }
        }.asLiveData();
    }


    private   LiveData<Resource<UploadResponse>> transferObserverListener(TransferObserver transferObserver){
        MutableLiveData<Resource<UploadResponse>> mStrasferState = new MutableLiveData<>();
        transferObserver.setTransferListener(new TransferListener(){
            @Override
            public void onStateChanged(int id, TransferState state) {
                mStrasferState.setValue(Resource.success(UploadResponse.state(state)));
                Timber.e("State " + state);
            }
            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                // int percentagek = (int)(double) (bytesCurrent/bytesTotal * 100);
                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;
                float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                Timber.e("Progress in %"
                        + percentage);
                mStrasferState.setValue(Resource.success(UploadResponse.progress((long)percentage)));
            }
            @Override
            public void onError(int id, Exception ex) {
                Timber.e("failed to upload" + ex);
                ex.printStackTrace();
                mStrasferState.setValue(Resource.success(UploadResponse.error((ex))));
            }
        });

        return mStrasferState;

    }

    private void persistConversationAndMesaages(List<Message> messages){
        List<ConversationAndMessage> messageLis = new ArrayList<>();
        List<Conversation> conversationList = new ArrayList<>();

        for (Message message: messages){
            Conversation conversation = new Conversation();
            conversation.setId(message.getConversation_id());
            conversation.setUser_one_id(message.getSender_id());
            conversation.setUser_two_id(message.getRecipient_id());
            conversationList.add(conversation);

            ConversationAndMessage conversationAndMessage = new ConversationAndMessage();
            conversationAndMessage.setId(message.getConversation_id());
            conversationAndMessage.setSender_id(message.getSender_id());
            conversationAndMessage.setRecipient_id(message.getRecipient_id());
            conversationAndMessage.setSender_first_name(message.getSender_first_name());
            conversationAndMessage.setSender_last_name(message.getSender_last_name());
            conversationAndMessage.setRecipient_first_name(message.getRecipient_first_name());
            conversationAndMessage.setRecipient_last_name(message.getRecipient_last_name());
            conversationAndMessage.setSender_picture(message.getSender_picture());
            conversationAndMessage.setTime_sent(message.getTime_sent());
            conversationAndMessage.setMessage(message.getMessage());
            messageLis.add(conversationAndMessage);

        }
        mAppDatabase.conversationDao().insertList(conversationList);
        mAppDatabase.messageDao().insertList(messages);
        mAppDatabase.dialogueDao().insertdialogueList(messageLis);
    }
}
