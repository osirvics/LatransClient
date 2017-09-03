package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
@SuppressWarnings("all")
public class Trip {
    @PrimaryKey
    public int id;
    public String phone_no;
    public long posted_on;
    public long time_updated;
    public String traveling_date;
    public String traveling_from_city;
    public String traveling_from_state;
    public String traveling_to_city;
    public String traveling_to_state;
    public int user_id;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Trip() {
    }

    /**
     *
     * @param phone_no i
     * @param id i
     * @param traveling_to_state i
     * @param traveling_to_city i
     * @param traveling_from_city ik
     * @param userId  k
     * @param time_updated k
     * @param traveling_from_state kj
     * @param traveling_date j
     * @param posted_on k
     */
    public Trip(int id, String phone_no, long posted_on, long time_updated, String traveling_date, String traveling_from_city, String traveling_from_state, String traveling_to_city, String traveling_to_state, int user_id) {
        super();
        this.id = id;
        this.phone_no = phone_no;
        this.posted_on = posted_on;
        this.time_updated = time_updated;
        this.traveling_date = traveling_date;
        this.traveling_from_city = traveling_from_city;
        this.traveling_from_state = traveling_from_state;
        this.traveling_to_city = traveling_to_city;
        this.traveling_to_state = traveling_to_state;
        this.user_id = user_id;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getPhone_no() {
//        return phone_no;
//    }
//
//    public void setPhone_no(String phone_no) {
//        this.phone_no = phone_no;
//    }
//
//    public long getPosted_on() {
//        return posted_on;
//    }
//
//    public void setPosted_on(long posted_on) {
//        this.posted_on = posted_on;
//    }
//
//    public long getTime_updated() {
//        return time_updated;
//    }
//
//    public void setTime_updated(long time_updated) {
//        this.time_updated = time_updated;
//    }
//
//    public String getTraveling_date() {
//        return traveling_date;
//    }
//
//    public void setTraveling_date(String traveling_date) {
//        this.traveling_date = traveling_date;
//    }
//
//    public String getTraveling_from_city() {
//        return traveling_from_city;
//    }
//
//    public void setTraveling_from_city(String traveling_from_city) {
//        this.traveling_from_city = traveling_from_city;
//    }
//
//    public String getTraveling_from_state() {
//        return traveling_from_state;
//    }
//
//    public void setTraveling_from_state(String traveling_from_state) {
//        this.traveling_from_state = traveling_from_state;
//    }
//
//    public String getTraveling_to_city() {
//        return traveling_to_city;
//    }
//
//    public void setTraveling_to_city(String traveling_to_city) {
//        this.traveling_to_city = traveling_to_city;
//    }
//
//    public String getTraveling_to_state() {
//        return traveling_to_state;
//    }
//
//    public void setTraveling_to_state(String traveling_to_state) {
//        this.traveling_to_state = traveling_to_state;
//    }
//
//    public long getUserId() {
//        return user_id;
//    }
//
//    public void setUserId(int userId) {
//        this.user_id = user_id;
//    }

}