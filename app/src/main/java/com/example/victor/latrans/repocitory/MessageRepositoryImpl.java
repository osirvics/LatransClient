package com.example.victor.latrans.repocitory;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.google.NetworkBoundResource;
import com.example.victor.latrans.google.RateLimiter;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.MessageResponse;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    public LiveData<Resource<List<ConversationAndMessage>>>  getMessagesAndConversation(long id) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<ConversationAndMessage>, MessageResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull MessageResponse item) {
                List<Message> messages = item.getMessages();
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
}
