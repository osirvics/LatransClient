package com.example.victor.latrans.repocitory.remote.api.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

//a generic class that describes a data with a status
public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable public final String message;
    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

//    public static <T> Resource<T> success(@NonNull T data) {
//        return new Resource<>(SUCCESS, data, null);
//    }
//
//    public static <T> Resource<T> error(String msg, @Nullable T data) {
//        return new Resource<>(ERROR, data, msg);
//    }
//
//    public static <T> Resource<T> loading(@Nullable T data) {
//        return new Resource<>(LOADING, data, null);
//    }
}