package com.example.victor.latrans.view.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.victor.latrans.repocitory.LoginRepository;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ApiResponse;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;


/**
 * Created by Victor on 29/08/2017.
 */

public class LoginViewModel extends AndroidViewModel {

    private String mEmailOrPassword;
    private String mPassword;

    private LoginRepository mLoginRepository;

    private LiveData<ApiResponse> mTokenLiveData;


    public LoginViewModel(Application application) {
        super(application);
    }



    void setEmailOrPassword(String emailOrPass){
        mEmailOrPassword = emailOrPass;

    }

    void setPassword(String pass){
        mPassword = pass;
    }

//    public void setIsloginValueChanged(boolean flag){
//        mIsloginValueChanged.setValue(flag);
//    }
//
//    LiveData<Boolean> isDataLoginChanged(){
//        return mIsloginValueChanged;
//    }
    String getPassword(){
        return mPassword;
    }
    String getEmailOrPassword(){
        return mEmailOrPassword;
    }

    public void login(){
        mLoginRepository = null;
        mTokenLiveData = null;
       LoginRepository.killInstatnce();
    }

    private void loadToken(){
        onLoginReady(getEmailOrPassword(), getPassword());

        mTokenLiveData =   mLoginRepository.getToken();
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

    public LiveData<ApiResponse> getToken(){
        if (mTokenLiveData == null){
            mTokenLiveData = new MutableLiveData<>();
            loadToken();
        }
        return mTokenLiveData;
    }

    private void onLoginReady(String usernameorEmail, String password){
        if (mLoginRepository == null){
            mLoginRepository = LoginRepository.getInstance(ServiceGenerator.createService(APIService.class,
                    usernameorEmail, password), SharedPrefsHelper.getInstance(this.getApplication()));
        }
    }

}
