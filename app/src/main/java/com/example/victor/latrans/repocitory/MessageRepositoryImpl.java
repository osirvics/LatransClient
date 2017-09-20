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
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.MessageResponse;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Profile;
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
    APIService mAPIService;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;
    @Inject
    AppDatabase mAppDatabase;
    @Inject
    Context mContext;
    @Inject
    AppExecutors appExecutors;
    TransferUtility transferUtility;
    TransferObserver observer;

    @Inject
    public MessageRepositoryImpl(AppDatabase appDatabase,SharedPrefsHelper sharedPrefsHelper, Context context,AppExecutors executors){
        this.mAppDatabase = appDatabase;
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mContext = context;
        this.appExecutors = executors;
    }

    @Override
    public LiveData<Resource<List<ConversationAndMessage>>>  getMessagesAndConversation(long id) {
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
                return mAPIService.getMessages(id);
            }

            @Override
            protected void onFetchFailed() {
               repoListRateLimit.reset(String.valueOf(id));

            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<List<Message>>> getMessages(long id) {
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
                return mAppDatabase.messageDao().getAllConversation(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MessageResponse>> createCall() {
                return mAPIService.getMessages(id);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(String.valueOf(id));
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
        transferUtility = AmazonUtility.getTransferUtility(mContext);
        String key = file.getName();
        observer = transferUtility.upload(
                Constants.BUCKET_NAME,     /* The bucket to upload to */
                key,    /* The key for the uploaded object */
                file       /* The file where the data to upload exists */
        );
             return   transferObserverListener(observer);
    }

    public  LiveData<Resource<UploadResponse>> transferObserverListener(TransferObserver transferObserver){
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
            public void onError(int id, Exception ex) {;
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
            conversation.id = message.conversation_id;
            conversation.user_one_id = message.sender_id;
            conversation.user_two_id = message.recipient_id;
            conversationList.add(conversation);

            ConversationAndMessage conversationAndMessage = new ConversationAndMessage();
            conversationAndMessage.id = message.conversation_id;
            conversationAndMessage.sender_username = message.sender_username;
            conversationAndMessage.sender_picture = message.sender_picture;
            conversationAndMessage.time_sent = message.time_sent;
            conversationAndMessage.message = message.message;
            messageLis.add(conversationAndMessage);

        }
        mAppDatabase.conversationDao().insertList(conversationList);
        mAppDatabase.messageDao().insertList(messages);
        mAppDatabase.dialogueDao().insertdialogueList(messageLis);
    }
}
