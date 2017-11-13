package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Region;


public interface PostRepository {
    LiveData<Resource<Region>> getRegions();
    LiveData<Resource<User>> getUser();
    LiveData<Resource<Trip>> postTrip(long id, Trip trip);


}
