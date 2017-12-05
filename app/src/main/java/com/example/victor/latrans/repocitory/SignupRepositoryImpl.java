package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.victor.latrans.R;
import com.example.victor.latrans.google.ApiResponse;
import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.google.NetworkBoundResource;
import com.example.victor.latrans.google.RateLimiter;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.local.db.dao.UserDao;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.local.model.Token;
import com.example.victor.latrans.repocitory.local.model.TripResponse;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class SignupRepositoryImpl implements SignupRepository{

    public static String TRIP_TOPIC = "trips_fcm";
    public static String ORDER_TOPIC = "request_fcm";
    public static String USER =  "user";
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
    public SignupRepositoryImpl(AppDatabase appDatabase,SharedPrefsHelper sharedPrefsHelper, Context context,AppExecutors executors){
        this.mAppDatabase = appDatabase;
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mContext = context;
        this.appExecutors = executors;

    }

    public static String getUser(){
        return  USER;
    }


   public LiveData<Resource<NewUser>> createUser(final Registration registration){
       final MutableLiveData<Resource<NewUser>> data = new MutableLiveData<>();
       mAPIService = ServiceGenerator.createService(APIService.class,"", "");
       Call<NewUser> call = mAPIService.createUser(registration);
       call.enqueue(new Callback<NewUser>() {
           @Override
           public void onResponse(Call<NewUser> call, Response<NewUser> response) {
               if (response.isSuccessful()) {

                       mSharedPrefsHelper.setAccessToken(response.body().getToken());
                       long id = response.body().getUser().getId();
                       mSharedPrefsHelper.setUserId(id);
                       String user = "user";
                       String myTopic = user + String.valueOf(id);
                       FirebaseMessaging.getInstance().subscribeToTopic(myTopic);
                       FirebaseMessaging.getInstance().subscribeToTopic(TRIP_TOPIC);
                       FirebaseMessaging.getInstance().subscribeToTopic(ORDER_TOPIC);
                       appExecutors.diskIO().execute(() -> mAppDatabase.userDao().insertUser(response.body().getUser()));
                       data.setValue(Resource.success(response.body()));
                      // new MyAsynTask(response.body().getUser(),mAppDatabase.userDao()).execute();
               }
               else{
                   if(response.code() == 302){
                       String message = mContext.getString(R.string.username_exists);
                       data.setValue(Resource.message(message, null));
                   }

                   else if(response.code() == 303){
                       data.setValue(Resource.message(mContext.getString(R.string.email_exists), null));
                   }
                   else {
                       Timber.e("status code: %s", response.code());
                       Timber.e("body: %s", response.body());
                       Timber.e("error body: %s", response.errorBody().toString());
                       Timber.e("message: %s", response.message());
                      data.setValue(Resource.message(response.message(), null));
                   }
               }
           }

           @Override
           public void onFailure(Call<NewUser> call, Throwable t) {
               Timber.e("Failed");
             data.setValue(Resource.error(t));
           }
       });
       return data;
   }

    @Override
    public LiveData<Resource<NewUser>> getToken(String username, String passowrd) {
        MutableLiveData<Resource<Token>> token = new MutableLiveData<>();
//        String cached =  mSharedPrefsHelper.getAccessToken();
//        Token cachedToked = new Token();
//        cachedToked.setToken(cached);
//        if (cachedToked.getToken() != null) {
//            token.setValue(Resource.success(cachedToked));
//            Timber.e("Token present: " + cachedToked.getToken());
//            return token;
//        }
        Timber.e("No Token present, making api call " );

        final MutableLiveData<Resource<NewUser>> data = new MutableLiveData<>();
        //userCache.put(userId, data);
        mAPIService = ServiceGenerator.createService(APIService.class, username, passowrd);

        Call<NewUser> call = mAPIService.getLogInCredentials();
        call.enqueue(new Callback<NewUser>() {
            @Override
            public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                if (response.isSuccessful()) {
                    long id = response.body().getUser().getId();
                    mSharedPrefsHelper.setUserId(id);

                    String myTopic = USER + String.valueOf(id);
                    FirebaseMessaging.getInstance().subscribeToTopic(myTopic);
                    FirebaseMessaging.getInstance().subscribeToTopic(TRIP_TOPIC);
                    FirebaseMessaging.getInstance().subscribeToTopic(ORDER_TOPIC);
                    data.setValue(Resource.success(response.body()));
                    new MyAsynTask(response.body().getUser(),mAppDatabase.userDao()).execute();

                }
                else{
                    //APIError error = ErrorUtils.parseError(response);
                    //Timber.e("error message", error.message());
                    //TODO: remove sensitive debug logs
                    Timber.e("status code: %s", response.code());
                    Timber.e("body: %s", response.body());
                    Timber.e("error body: %s", response.errorBody());
                    Timber.e("message: %s", response.message());
                    data.setValue(Resource.message(response.message(),null));

                }
            }

            @Override
            public void onFailure(Call<NewUser> call, Throwable t) {
                data.setValue(Resource.error(t));
            }
        });

        return data;
    }

    @Override
    public LiveData<Resource<List<Trip>>> getTrips() {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<Trip>, TripResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull TripResponse item) {
                mAppDatabase.tripDao().insertAllTrips(item.getTrips());

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Trip> data) {;
                //return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("data");
                return true;

            }

            @NonNull
            @Override
            protected LiveData<List<Trip>> loadFromDb() {
                return  mAppDatabase.tripDao().getAllTrips();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<TripResponse>> createCall() {
                return mAPIService.getTrips();
            }

            @Override
            protected void onFetchFailed() {
                Timber.e("failed to fetch data");
                repoListRateLimit.reset("data");
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<List<Trip>>> getTripsForUser(long user_id) {
        mAPIService = ServiceGenerator.createService(APIService.class,"", "");
        return new NetworkBoundResource<List<Trip>, TripResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull TripResponse item) {
                mAppDatabase.tripDao().insertAllTrips(item.getTrips());
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Trip> data) {
                //return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(String.valueOf(user_id));
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Trip>> loadFromDb() {
                return  mAppDatabase.tripDao().findTripsForUser(user_id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<TripResponse>> createCall() {
                return mAPIService.getTripsForUser(user_id);
            }
        }.asLiveData();
    }

    @Override
    public long getUserId() {
        return mSharedPrefsHelper.getUserId();
    }


    public static class MyAsynTask extends AsyncTask<Void, Void, Void>{
       private User mUser;
       private UserDao mUserDao;
       public MyAsynTask(User user, UserDao userDao){
           this.mUser = user;
           this.mUserDao = userDao;
       }
       @Override
       protected Void doInBackground(Void... voids) {
           mUserDao.deleteAll();
           mUserDao.insertUser(mUser);
           return null;
       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
           //TODO remove log
           Timber.d("Finished saving user");
       }
   }



}
