package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(value = "id", unique = true))
@SuppressWarnings("all")
public class Trip {
    @PrimaryKey
    public long id;
    public String phone_no;
    public long posted_on;
    public long time_updated;
    public String traveling_date;
    public String traveling_from_city;
    public String traveling_from_state;
    public String traveling_to_city;
    public String traveling_to_state;
    public String profile_image;
    public long user_id;
    public String user_first_name;
    public String user_last_name;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Trip() {
    }

    public Trip(long id, String phone_no, long posted_on, long time_updated, String profile_image, String traveling_date, String traveling_from_city, String traveling_from_state,
                String traveling_to_city, String traveling_to_state, long user_id, String user_first_name, String user_last_name) {
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
        this.profile_image = profile_image;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
    }

}