package com.example.victor.latrans.repocitory.local.model;

import com.example.victor.latrans.repocitory.local.db.entity.Request;

import java.util.List;

/**
 * Created by Victor on 10/3/2017.
 */

public class RequestResponse {

    private int totalResulst;
    private List<Request> requests = null;

    public int getTotalResulst() {
        return totalResulst;
    }

    public void setTotalResulst(int totalResulst) {
        this.totalResulst = totalResulst;
    }

    public List<Request> getRequest() {
        return requests;
    }

    public void setTrips(List<Request> requests) {
        this.requests = requests;
    }
}