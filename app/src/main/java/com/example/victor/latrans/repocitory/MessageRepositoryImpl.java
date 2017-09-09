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
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.ConversationResponse;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;

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
    public LiveData<Resource<List<ConversationAndMessage>>> getConversation(long id) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<ConversationAndMessage>, ConversationResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull ConversationResponse item) {
               // mAppDatabase.conversationDao().deleteAll();
                mAppDatabase.conversationDao().insertList(item.getConversations());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ConversationAndMessage> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("id");
            }

            @NonNull
            @Override
            protected LiveData<List<ConversationAndMessage>> loadFromDb() {
                return mAppDatabase.messageDao().findConversationAndMessage();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ConversationResponse>> createCall() {
                return mAPIService.getConversations(id);
            }

            @Override
            protected void onFetchFailed() {
               repoListRateLimit.reset(String.valueOf(id));

            }
        }.asLiveData();
    }
}
