package com.example.victor.latrans.view.ui.signup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.SignupRepository;

import javax.inject.Inject;

/**
 * Created by Victor on 31/08/2017.
 */

public class SignUpViewModel extends ViewModel implements AppComponent.Injectable{



    @Inject
    SignupRepository mSignupRepository;



    private String username;
    private String password;
    private String email;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private LiveData<Resource<NewUser>> mLiveData;

     LiveData<Resource<NewUser>> getResponse(){
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return  mLiveData;

    }

    private void queryResponse(){
        mLiveData = mSignupRepository.createUser(buildCredentials());
    }

    private Registration buildCredentials(){
        Registration registration = new Registration();
        registration.setUsername(getUsername());
        registration.setPassword(getPassword());
        registration.setEmail(getEmail());
        return registration;
    }

    @Override
    public void inject(AppComponent countdownComponent) {
        countdownComponent.inject(this);
    }
}
