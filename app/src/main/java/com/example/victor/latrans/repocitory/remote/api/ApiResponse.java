package com.example.victor.latrans.repocitory.remote.api;


import android.support.annotation.Nullable;

import com.example.victor.latrans.repocitory.local.model.Token;



public class ApiResponse {
    @Nullable private Token token;
    @Nullable private Throwable error;
    @Nullable private String message;

    public ApiResponse (){
    }

    public ApiResponse(@Nullable Token token) {
        this.token = token;
        this.error = null;
        this.message = null;
    }

    public ApiResponse(@Nullable Throwable error) {
        this.error = error;
        this.message = null;
        this.token = null;
    }

    public ApiResponse(@Nullable String message) {
        this.message = message;
        this.error = null;
        this.token = null;
    }

    public Token getToken() {
        return token;
    }

    public Throwable getError() {
        return error;
    }

    public String getMessage(){
        return message;
    }
}