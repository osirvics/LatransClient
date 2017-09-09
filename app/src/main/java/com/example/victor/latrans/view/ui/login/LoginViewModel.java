package com.example.victor.latrans.view.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.model.Login;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.SignupRepository;

import javax.inject.Inject;


public class LoginViewModel extends ViewModel implements AppComponent.Injectable{

    private String mEmailOrPassword;
    private String mPassword;

    @Override
    public void inject(AppComponent countdownComponent) {
        countdownComponent.inject(this);
    }



    public LoginViewModel() {
        Login.setPassword("victorr");
        Login.setUsername("chemistryy");
    }

    @Inject
    SignupRepository mSignupRepository;

    private LiveData<Resource<NewUser>> mTokenLiveData;



    void setEmailOrPassword(String emailOrPass){
        mEmailOrPassword = emailOrPass;

    }

    void setPassword(String pass){
        mPassword = pass;
    }


    String getPassword(){
        return mPassword;
    }
    String getEmailOrPassword(){
        return mEmailOrPassword;
    }

    public void login(){
        mSignupRepository = null;
        mTokenLiveData = null;
    }

    private void loadToken(){
      //  onLoginReady(getEmailOrPassword(), getPassword());

        mTokenLiveData =   mSignupRepository.getToken(getEmailOrPassword(), getPassword());
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
        if (mTokenLiveData == null){
            mTokenLiveData = new MutableLiveData<>();
            loadToken();
        }
        return mTokenLiveData;
    }


//    private void onLoginReady(String usernameorEmail, String password){
//            Login.setPassword(usernameorEmail);
//            Login.setUsername(password);
//
//    }

}
