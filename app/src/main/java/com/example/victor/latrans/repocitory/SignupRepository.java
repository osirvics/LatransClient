package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.victor.latrans.repocitory.local.db.dao.UserDao;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.response.NewUserResponse;
import com.example.victor.latrans.util.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class SignupRepository {
    private static  SignupRepository sInstance;

    public static  SignupRepository getInstance(@NonNull APIService apiService, @NonNull SharedPrefsHelper sharedPrefsHelper, UserDao userDao) {
        if (sInstance == null) {
            sInstance = new  SignupRepository(apiService, sharedPrefsHelper, userDao);
        }
        return sInstance;
    }

    public static void killInstatnce(){
        sInstance = null;
    }

    private final SharedPrefsHelper mSharedPrefsHelper;

    private final APIService mAPIService;
    private final UserDao mUserDao;

    private SignupRepository(APIService apiService, SharedPrefsHelper sharedPrefsHelper, UserDao userDao) {
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mAPIService = apiService;
        this.mUserDao = userDao;
    }

   public LiveData<NewUserResponse> getUserCredential(final Registration registration){
       final MutableLiveData<NewUserResponse> data = new MutableLiveData<>();
       Call<NewUser> call = mAPIService.createUser(registration);
       call.enqueue(new Callback<NewUser>() {
           @Override
           public void onResponse(Call<NewUser> call, Response<NewUser> response) {
               if (response.isSuccessful()) {

                       mSharedPrefsHelper.setAccessToken(response.body().getToken());
                       data.setValue(new NewUserResponse(response.body()));
                       new MyAsynTask(response.body().getUser(),mUserDao).execute();
               }

               else{
                   //TODO: remove sensitive debug logs
                   if(response.code() == 302){
                       //TODO remove comment below
                       //String message = App.get.getString(R.string.username_exists);
                       //data.setValue(new NewUserResponse(message));
                   }

                   else if(response.code() == 303){
                       //TODO remove comment below
                      // data.setValue(new NewUserResponse(App.getContext().getString(R.string.email_exists)));
                   }

                   else {
                       Timber.e("status code: %s", response.code());
                       Timber.e("body: %s", response.body());
                       Timber.e("error body: %s", response.errorBody().toString());
                       Timber.e("message: %s", response.message());
                      data.setValue(new NewUserResponse(response.message()));
                   }
               }
           }

           @Override
           public void onFailure(Call<NewUser> call, Throwable t) {
               Timber.e("Failed");
             data.setValue(new NewUserResponse(t));
           }
       });
       return data;
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
