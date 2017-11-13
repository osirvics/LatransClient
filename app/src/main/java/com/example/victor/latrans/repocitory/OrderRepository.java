package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Request;

import java.util.List;


public interface OrderRepository {

    LiveData<Resource<List<Request>>> getRequest();
    LiveData<Resource<Request>> addOrderRequest(long userId, Request request);


}
