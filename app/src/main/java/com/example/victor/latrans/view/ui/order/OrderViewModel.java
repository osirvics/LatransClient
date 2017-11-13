package com.example.victor.latrans.view.ui.order;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.OrderRepository;
import com.example.victor.latrans.repocitory.local.db.entity.Request;

import java.util.List;

import javax.inject.Inject;


public class OrderViewModel extends ViewModel implements AppComponent.Injectable {

    @Inject
    OrderRepository mOrderRepository;

    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    private LiveData<Resource<List<Request>>> mLiveData;

    public  LiveData<Resource<List<Request>>> getOrders(){
        if(mLiveData == null){
            mLiveData = new MutableLiveData<>();
            mLiveData = mOrderRepository.getRequest();
        }
        return mLiveData;
    }
}
