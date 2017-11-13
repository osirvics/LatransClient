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
        request.user_id = userId;
        request.item_location_state = mItemLocationState;
        request.item_location_city = mItemLocationCity;
        request.delivery_state = mDeliveryState;
        request.delivery_city = mDeliveryCity;
        request.item_name = mItemName;

        request.picture = mItemPictureUrl;

        request.offer_amount = mItemStartingRewardAmount;
        request.deliver_before = mExpectedDelivryDate;
        request.phone_no = mContactNumber;
        request.posted_on = System.currentTimeMillis();
        request.time_updated = System.currentTimeMillis();

        return request;


    }



}
