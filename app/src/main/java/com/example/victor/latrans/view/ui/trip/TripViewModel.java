package com.example.victor.latrans.view.ui.trip;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.SignupRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Victor on 9/3/2017.
 */

public class TripViewModel extends ViewModel implements AppComponent.Injectable {
    @Inject
    SignupRepository mSignupRepository;

    private LiveData<Resource<List<Trip>>> mLiveData;


    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    LiveData<Resource<List<Trip>>> getResponse() {
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return  mLiveData;

    }

    private void queryResponse(){
        mLiveData = mSignupRepository.getTrips();
    }
}
