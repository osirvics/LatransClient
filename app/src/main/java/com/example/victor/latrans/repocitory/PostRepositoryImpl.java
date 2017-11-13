package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.victor.latrans.R;
import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.google.NetworkBoundResource;
import com.example.victor.latrans.google.RateLimiter;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Victor on 9/20/2017.
 */

public class PostRepositoryImpl implements PostRepository{

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


    @Inject
    public PostRepositoryImpl(AppDatabase appDatabase,SharedPrefsHelper sharedPrefsHelper, Context context,AppExecutors executors){
        this.mAppDatabase = appDatabase;
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mContext = context;
        this.appExecutors = executors;
    }


    @Override
    public LiveData<Resource<Region>> getRegions() {
        MutableLiveData<Resource<Region>> data = new MutableLiveData<>();
        StringBuilder builder = new StringBuilder();
        InputStream in = mContext.getResources().openRawResource(R.raw.regions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Timber.e("Failed to read line");
            e.printStackTrace();
        }
        //Parse resource into key/values
        final String rawJson = builder.toString();
        Gson gson = new Gson();
        Region regions = gson.fromJson(rawJson, Region.class);
        data.setValue(Resource.success(regions));
        return data;
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
    public LiveData<Resource<Trip>> postTrip(long id, Trip trip) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<Trip, Trip>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Trip item) {
                //mAppDatabase.tripDao().insertTrip(trip);
            }

            @Override
            protected boolean shouldFetch(@Nullable Trip data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Trip> loadFromDb() {
                return mAppDatabase.tripDao().getATrip();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Trip>> createCall() {
                return mAPIService.postTrips(trip, id);
            }

            @Override
            protected void onFetchFailed() {
                super.onFetchFailed();
            }
        }.asLiveData();
    }
}
