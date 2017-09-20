package com.example.victor.latrans.repocitory.local.model;

import java.util.List;

public class Region {
    public Region(){

    }
    private List<State> state = null;

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }
}