package com.example.victor.latrans.repocitory.remote.api.response;

import android.support.annotation.Nullable;

import com.example.victor.latrans.repocitory.local.model.NewUser;

/**
 * Created by Victor on 31/08/2017.
 */

public class NewUserResponse {
    @Nullable private NewUser mNewUser;
    @Nullable private Throwable error;
    @Nullable private String message;

    public NewUserResponse(){

    }


    public NewUserResponse(@Nullable NewUser newUser){
        this.mNewUser = newUser;
        this.error = null;
        this.message = null;
    }

    public NewUserResponse(@Nullable Throwable error) {
        this.error = error;
        this.message = null;
        this.mNewUser = null;
    }

    public NewUserResponse(@Nullable String message) {
        this.message = message;
        this.mNewUser = null;
        this.error = null;

    }




    public NewUser getNewUser() {
        return mNewUser;
    }

    public Throwable getError() {
        return error;
    }

    public String getMessage(){
        return message;
    }



}
