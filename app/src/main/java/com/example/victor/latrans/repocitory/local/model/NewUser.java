package com.example.victor.latrans.repocitory.local.model;

import com.example.victor.latrans.repocitory.local.db.entity.User;

/**
 * Created by Victor on 31/08/2017.
 */

public class NewUser {
    String token;
    User user;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
