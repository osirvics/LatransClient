package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity

@SuppressWarnings("all")
public class User {

    @PrimaryKey
    public long id;
    public String username;
    public String email;
    public String name;
    public String picture;
    public int ratings;
    public String surname;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public User() {
    }

    /**
     *
     * @param picture the picture
     * @param id the id
     * @param username the username
     * @param email person's email
     * @param name yeah, his/her name
     * @param surname obvious enough
     * @param ratings ratings for this user
     */
    public User(String email, long id, String name, String picture, int ratings, String surname, String username) {
        super();
        this.email = email;
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.ratings = ratings;
        this.surname = surname;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

