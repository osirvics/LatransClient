package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.Request;

import java.util.List;

/**
 * Created by Victor on 10/3/2017.
 */

@Dao
public interface OrderDao {

    @Query("select * from request ORDER BY id DESC")
    LiveData<List<Request>> getAllRequests();

    @Query("select * from request ORDER BY id ASC LIMIT 1")
    LiveData<Request> getARequest();

    @Query("select * from request where id = :id")
    LiveData<Request> findARequestById(long id);

    @Query("select * from request where user_id = :user_id ORDER BY id DESC")
    LiveData<List<Request>> findRequestForUser(long user_id);

    @Query("select * from request where deliver_before = :deliver_before")
    LiveData<List<Request>> findARequestByDeliveryDate(String deliver_before);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveRequests(Request... requests);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRequest(Request request);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListRequest(List<Request> requests);

    @Query("DELETE FROM request")
    void deleteAll();
}
