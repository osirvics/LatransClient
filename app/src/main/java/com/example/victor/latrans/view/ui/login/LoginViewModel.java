package com.example.victor.latrans.view.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.SignupRepository;
import com.example.victor.latrans.repocitory.local.model.NewUser;

import javax.inject.Inject;


public class LoginViewModel extends ViewModel implements AppComponent.Injectable{

    public String mEmail;
    public String mPassword;

    @Override
    public void inject(AppComponent countdownComponent) {
        countdownComponent.inject(this);
    }

    @Inject
    SignupRepository mSignupRepository;

    private LiveData<Resource<NewUser>> mTokenLiveData;



    public void login(){
        mSignupRepository = null;
        mTokenLiveData = null;
    }

    private void loadToken(){
        mTokenLiveData =   mSignupRepository.getToken(mEmail, mPassword);
//        mTokenLiveData = Transformations.switchMap(isDataLoginChanged(), new Function<Boolean, LiveData<ApiResponse>>() {
//            @Override
//            public LiveData<ApiResponse> apply(Boolean input) {
//                if (Boolean.TRUE.equals(input)){
//                    onLoginReady(getEmailOrPassword(), getPassword());
//                    return mLoginRepository.getToken();
//                }
//                return null;
//            }
//        });
    }

    public LiveData<Resource<NewUser>> getToken(){
            mTokenLiveData = new MutableLiveData<>();
            loadToken();
        return mTokenLiveData;
    }

}
