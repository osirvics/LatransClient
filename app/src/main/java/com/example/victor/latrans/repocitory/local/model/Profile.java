package com.example.victor.latrans.repocitory.local.model;

/**
 * Created by Victor on 9/10/2017.
 */

public class Profile {
    public String name;
    public String picture;
    public String phone_no;

    public Profile(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneNo() {
        return phone_no;
    }

    public void setPhoneNo(String phone_no) {
        this.phone_no = phone_no;
    }
}
