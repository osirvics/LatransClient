package com.example.victor.latrans.view.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.example.victor.latrans.amazon.AmazonUtility;
import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Profile;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;
import com.example.victor.latrans.util.SharedPrefsHelper;

import java.io.File;
import java.util.List;

import javax.inject.Inject;


public class EditProfileViewModel extends ViewModel implements AppComponent.Injectable {
    public String name;
    public String phoneNo;
    public String profile_url;
    public File file;
    public List<Uri> mSelected;

    public List<Uri> getSelected() {
        return mSelected;
    }

    public void setSelected(List<Uri> selected) {
        mSelected = selected;
    }


    public void setProfile(String profile_url) {
        this.profile_url = profile_url;
    }
    @Inject
    MessageRepository mMessageRepository;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;

    private LiveData<Resource<User>> mLiveData;
    private LiveData<Resource<User>> mUserData;

    private LiveData<Resource<UploadResponse>> mStransferState;
    private LiveData<Resource<Long>> percentage;


    public LiveData<Resource<UploadResponse>> startUpload(){
        mStransferState = new MutableLiveData<>();
        mStransferState = mMessageRepository.beginUpload(file);
        return mStransferState;

    }

    public LiveData<Resource<User>> getResponse(){
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return mLiveData;
    }

    private void queryResponse(){
        mLiveData = mMessageRepository.updateUser(mSharedPrefsHelper.getUserId(), buildProfile());
    }

    public LiveData<Resource<User>> getUserData(){
        if(mUserData == null){
            mUserData = new MutableLiveData<>();
            mUserData = mMessageRepository.getUser();
        }
        return mUserData;

    }

    public Profile buildProfile(){
        Profile profile = new Profile();
        profile.name = name;
        profile.phone_no = phoneNo;
        if (file != null ){
            profile.picture = AmazonUtility.getAmazonLink(file);
        }
        return profile;

    }

    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }
}
