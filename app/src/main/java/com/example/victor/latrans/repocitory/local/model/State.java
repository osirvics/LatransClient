package com.example.victor.latrans.repocitory.local.model;

import java.util.List;

public class State {

    public State(){
    }
    private String name;
    private int id;
    private List<Local> locals = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Local> getLocals() {
        return locals;
    }

    public void setLocals(List<Local> locals) {
        this.locals = locals;
    }

}