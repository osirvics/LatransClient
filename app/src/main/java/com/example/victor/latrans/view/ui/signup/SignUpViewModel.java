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

public class SignUpViewModel extends ViewModel implements AppComponent.Injectable{

    @Inject
    SignupRepository mSignupRepository;

    public String firstName;
    public String lastName;
    public String password;
    public String email;



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
        registration.first_name = firstName;
        registration.last_name = lastName;
        registration.password = password;
        registration.email = email.trim();
        return registration;
    }

    @Override
    public void inject(AppComponent countdownComponent) {
        countdownComponent.inject(this);
    }
}
