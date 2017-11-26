package com.example.victor.latrans.view.ui.addorder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.OrderRepository;
import com.example.victor.latrans.repocitory.PostRepository;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.repocitory.local.model.State;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Victor on 10/4/2017.
 */

public class AddOrderViewModel extends ViewModel implements AppComponent.Injectable {

    public String mDeliveryState;
    public String mDeliveryCity;
    public String mItemLocationState;
    public String mItemLocationCity;
    public String mItemName;
    public String mItemPictureUrl;
    public String mItemStartingRewardAmount;
    public String mExpectedDelivryDate;
    public String mContactNumber;
    public long userId;
    public File mItemImagefile;
    public List<Uri> mSelected;
    public List<State> mStates = new ArrayList<>();


    @Inject
    PostRepository mPostRepository;
    @Inject
    OrderRepository mOrderRepository;
    @Inject
    MessageRepository mMessageRepository;

    private LiveData<Resource<User>> mUserData;
    private LiveData<Resource<Region>> mLiveDataRegion;


    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }





    public LiveData<Resource<UploadResponse>> startUpload(){
        LiveData<Resource<UploadResponse>> stransferState = new MutableLiveData<>();
        stransferState = mMessageRepository.beginUpload(mItemImagefile);
        return stransferState;

    }


    public LiveData<Resource<User>> getUserData() {
    if(mUserData == null){
        mUserData = new MutableLiveData<>();
        mUserData = mPostRepository.getUser();
    }
    return mUserData;

    }

    public LiveData<Resource<Region>> getRegions(){
        if (mLiveDataRegion == null){
            mStates = new ArrayList<>();
            mLiveDataRegion = new MutableLiveData<>();
            mLiveDataRegion = mPostRepository.getRegions();
        }
        return mLiveDataRegion;
    }

    public LiveData<Resource<Request>> postOrder(){
        LiveData<Resource<Request>> requestData = new MutableLiveData<>();
        requestData = mOrderRepository.addOrderRequest(userId, buildRequest());
        return requestData;
    }



    private Request buildRequest(){
        Request request = new Request();
        request.setUser_id(userId);
        request.setItem_location_state(mItemLocationState);
        request.setItem_location_city(mItemLocationCity);
        request.setDelivery_state(mDeliveryState);
        request.setDelivery_city(mDeliveryCity);
        request.setItem_name(mItemName);

        request.setPicture(mItemPictureUrl);

        request.setOffer_amount(mItemStartingRewardAmount);
        request.setDeliver_before(mExpectedDelivryDate);
        request.setPhone_no(mContactNumber);
        request.setPosted_on(System.currentTimeMillis());
        request.setTime_updated(System.currentTimeMillis());

        return request;


    }



}
