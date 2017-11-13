package com.example.victor.latrans.repocitory.local.model;

import com.example.victor.latrans.repocitory.local.db.entity.Message;

/**
 * Created by Victor on 9/24/2017.
 */

public class SingleMessageResponse {
    public Message data;

    public Message getMessage() {
        return data;
    }

    public void setMessage(Message data) {
        this.data = data;
    }

    public SingleMessageResponse(Message data) {
        this.data = data;
    }
}
