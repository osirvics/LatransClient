package com.example.victor.latrans.repocitory.local.db.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = @Index(value = "id", unique = true))
public class Request {

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Request() {
    }

    @PrimaryKey
    public long id;
    public long user_id;
    public String delivery_state;
    public String delivery_city;
    public String item_location_state;
    public String item_location_city;
    public String picture;
    public String offer_amount;
    public String deliver_before;
    public long posted_on;
    public long time_updated;
    public String item_name;
    public String profile_image;
    public String user_first_name;
    public String user_last_name;
    public String phone_no;

    public Request(long id, long user_id, String delivery_state, String delivery_city, String item_location_state,
                   String item_location_city, String picture, String offer_amount, String deliver_before,
                   long posted_on, long time_updated,
                   String item_name, String profile_image, String user_first_name, String user_last_name, String phone_no) {

        this.id = id;
        this.user_id = user_id;
        this.delivery_state = delivery_state;
        this.delivery_city = delivery_city;
        this.item_location_state = item_location_state;
        this.item_location_city = item_location_city;
        this.picture = picture;
        this.offer_amount = offer_amount;
        this.deliver_before = deliver_before;
        this.posted_on = posted_on;
        this.time_updated = time_updated;
        this.item_name = item_name;
        this.profile_image = profile_image;
        this.user_first_name = user_first_name;
        this.user_last_name = user_last_name;
        this.phone_no = phone_no;
    }






}
