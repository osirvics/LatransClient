package com.example.victor.latrans.view.ui.addtrip;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.PostRepository;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.util.SharedPrefsHelper;

import javax.inject.Inject;



public class AddTripViewModel extends ViewModel implements AppComponent.Injectable {
    String mToState;
    String mToLocal;
    String mFromState;
    String mFromLocal;
    String mPhoneNo;
    String mTravelDate;
    long mUserId;
    @Inject
    PostRepository mPostRepository;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;

    private LiveData<Resource<Region>> mLiveDataRegion;
    private LiveData<Resource<User>> mUserData;
    private LiveData<Resource<Trip>> mTripData;

    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public LiveData<Resource<Region>> getRegions(){
        if (mLiveDataRegion == null){
            mLiveDataRegion = new MutableLiveData<>();
            mLiveDataRegion = mPostRepository.getRegions();
        }
        return mLiveDataRegion;
    }

    public LiveData<Resource<User>> getUserData(){
        if(mUserData == null){
            mUserData = new MutableLiveData<>();
            mUserData = mPostRepository.getUser();
        }
        return mUserData;
    }

    public LiveData<Resource<Trip>> getResponse(){
        mTripData = new MutableLiveData<>();
        postTrip();
        return mTripData;
    }

    private void postTrip(){
     mTripData = mPostRepository.postTrip(mSharedPrefsHelper.getUserId(), buildTrip());
    }

    private Trip buildTrip(){
        Trip trip = new Trip();
        trip.setTraveling_to_state(mToState);
        trip.setTraveling_to_city(mToLocal);
        trip.setTraveling_from_state(mFromState);
        trip.setTraveling_from_city(mFromLocal);
        trip.setPhone_no(mPhoneNo);
        trip.setPosted_on(System.currentTimeMillis());
        trip.setTime_updated(System.currentTimeMillis());
        trip.setTraveling_date(mTravelDate);
        trip.setUser_id(mUserId);
        return trip;
    }
}
