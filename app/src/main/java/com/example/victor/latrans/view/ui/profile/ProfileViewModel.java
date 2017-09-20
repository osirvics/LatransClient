package com.example.victor.latrans.view.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.local.db.entity.User;

import javax.inject.Inject;

/**
 * Created by Victor on 9/10/2017.
 */

public class ProfileViewModel extends ViewModel implements AppComponent.Injectable {
    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Inject
    MessageRepository mMessageRepository;

    private LiveData<Resource<User>> mLiveData;


    public LiveData<Resource<User>> getResponse(){
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return mLiveData;
    }

    private void queryResponse(){
        //TODO remove hardcoded id here
        mLiveData = mMessageRepository.getUser();
    }
}
