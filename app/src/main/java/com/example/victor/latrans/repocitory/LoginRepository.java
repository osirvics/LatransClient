package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.victor.latrans.repocitory.local.model.Token;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ApiResponse;
import com.example.victor.latrans.util.SharedPrefsHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Victor on 30/08/2017.
 */

public class LoginRepository {
    private static LoginRepository sInstance;



    public static LoginRepository getInstance(@NonNull APIService apiService, @NonNull SharedPrefsHelper sharedPrefsHelper) {
        if (sInstance == null) {
            sInstance = new LoginRepository(apiService, sharedPrefsHelper);
        }
        return sInstance;
    }

    public static void killInstatnce(){
        sInstance = null;
    }

    private final SharedPrefsHelper mSharedPrefsHelper;

    private final APIService mAPIService;

    private LoginRepository(APIService apiService, SharedPrefsHelper sharedPrefsHelper) {
        this.mSharedPrefsHelper = sharedPrefsHelper;
        this.mAPIService = apiService;
    }


    public LiveData<ApiResponse>  getToken(){
       MutableLiveData<ApiResponse> token = new MutableLiveData<>();
       String cached =  mSharedPrefsHelper.getAccessToken();
       Token cachedToked = new Token();
       cachedToked.setToken(cached);
        if (cachedToked.getToken() != null) {
            token.setValue(new ApiResponse(cachedToked));
            Timber.e("Token present: " + cachedToked.getToken());
            return token;
        }
        Timber.e("No Token present, making api call " );

        final MutableLiveData<ApiResponse> data = new MutableLiveData<>();
        //userCache.put(userId, data);



        Call<Token> call = mAPIService.getLogInToken();
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    //TODO remove comment below
                   // SharedPrefsHelper.getInstance(App.getContext()).setAccessToken(response.body().getToken());
                    data.setValue(new ApiResponse(response.body()));

                }
                else{
                    //APIError error = ErrorUtils.parseError(response);
                    //Timber.e("error message", error.message());
                    //TODO: remove sensitive debug logs
                    Timber.e("status code: %s", response.code());
                    Timber.e("body: %s", response.body());
                    Timber.e("error body: %s", response.errorBody());
                    Timber.e("message: %s", response.message());
                    data.setValue(new ApiResponse("Joking with you"));

                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                data.setValue(new ApiResponse(t));
            }
        });

        return data;


    }




}
