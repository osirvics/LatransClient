package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.google.NetworkBoundResource;
import com.example.victor.latrans.google.RateLimiter;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.model.RequestResponse;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


public class OrderRepositoryImpl implements OrderRepository{
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
    public OrderRepositoryImpl(AppDatabase appDatabase,SharedPrefsHelper sharedPrefsHelper, Context context,AppExecutors executors){
        this.mAppDatabase = appDatabase;
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mContext = context;
        this.appExecutors = executors;
    }

    @Override
    public LiveData<Resource<List<Request>>> getRequest() {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<Request>, RequestResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull RequestResponse item) {
                mAppDatabase.orderDao().insertListRequest(item.getRequest());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Request> data) {
                //return true;
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("order");
            }

            @NonNull
            @Override
            protected LiveData<List<Request>> loadFromDb() {
                return mAppDatabase.orderDao().getAllRequests();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RequestResponse>> createCall() {
                return mAPIService.getRequests();
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<Request>> addOrderRequest(long userId, Request request) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<Request, Request>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Request item) {
               // mAppDatabase.orderDao().insertRequest(item);

            }

            @Override
            protected boolean shouldFetch(@Nullable Request data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Request> loadFromDb() {
                return mAppDatabase.orderDao().getARequest();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Request>> createCall() {
                return mAPIService.postResquest(userId, request);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<Request>>> getRequestForUser(long userId) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<Request>, RequestResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull RequestResponse item) {
                mAppDatabase.orderDao().insertListRequest(item.getRequest());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Request> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(String.valueOf(userId));
               // return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Request>> loadFromDb() {
                return mAppDatabase.orderDao().findRequestForUser(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RequestResponse>> createCall() {
                return mAPIService.getRequestForUser(userId);
            }
        }.asLiveData();
    }
}
