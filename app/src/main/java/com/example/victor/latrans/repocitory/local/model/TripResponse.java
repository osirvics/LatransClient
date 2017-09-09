package com.example.victor.latrans.repocitory.local.model;

import com.example.victor.latrans.repocitory.local.db.entity.Trip;

import java.util.List;

/**
 * Created by Victor on 9/6/2017.
 */

public class TripResponse {

    private int totalResulst;
    private List<Trip> trips = null;

    public int getTotalResulst() {
        return totalResulst;
    }

    public void setTotalResulst(int totalResulst) {
        this.totalResulst = totalResulst;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}