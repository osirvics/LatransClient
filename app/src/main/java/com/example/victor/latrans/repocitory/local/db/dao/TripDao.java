package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.Trip;

import java.util.List;


@Dao
public interface TripDao {


    @Query("select * from trip ORDER BY id DESC")
    LiveData<List<Trip>> getAllTrips();

    @Query("select * from trip ORDER BY id ASC LIMIT 1")
    LiveData<Trip> getATrip();

    @Query("select * from trip where id = :id ORDER BY id DESC")
    LiveData<Trip> findATripById(long id);

    @Query("select * from trip where user_id = :user_id ORDER BY id DESC")
    LiveData<List<Trip>> findTripsForUser(long user_id);

    @Query("select * from trip where traveling_from_city = :traveling_from_city")
    LiveData<List<Trip>> findAtripByTravelingFromCity(String traveling_from_city);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveTrips(Trip... trips);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTrip(Trip trip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insertAllTrips(List<Trip> trips);

    @Query("DELETE FROM trip")
    void deleteAll();










}
