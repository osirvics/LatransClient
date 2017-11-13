package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity( indices = @Index(value = "id", unique = true))

@SuppressWarnings("all")
public class User {

    @PrimaryKey
    public long id;
    public String first_name;
    public String last_name;
    public String email;
    public String picture;
    public String phone_no;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public User() {
    }


    public User(String email, long id, String first_name, String last_name, String picture,String phone_no) {
        super();
        this.email = email;
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.picture = picture;
        this.phone_no = phone_no;

    }


}

