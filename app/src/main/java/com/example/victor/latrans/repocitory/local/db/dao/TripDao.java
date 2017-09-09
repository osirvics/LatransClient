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


    @Query("select * from trip")
    LiveData<List<Trip>> getAllTrips();

    @Query("select * from trip where id = :id")
    LiveData<Trip> findAtripById(long id);

    @Query("select * from trip where traveling_from_city = :traveling_from_city")
    LiveData<List<Trip>> findAtripByTravelingFromCity(String traveling_from_city);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void saveTrips(Trip... trips);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTrip(Trip trip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     void insertTrips(List<Trip> repositories);










}
