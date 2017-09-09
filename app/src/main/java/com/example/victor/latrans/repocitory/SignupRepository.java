package com.example.victor.latrans.repocitory;

import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.repocitory.local.model.Registration;

import java.util.List;

/**
 * Created by Victor on 9/4/2017.
 */

public interface SignupRepository {
    LiveData<Resource<NewUser>> createUser(Registration registration);
    LiveData<Resource<NewUser>> getToken(String username, String password);
    LiveData<Resource<List<Trip>>> getTrips();

}
