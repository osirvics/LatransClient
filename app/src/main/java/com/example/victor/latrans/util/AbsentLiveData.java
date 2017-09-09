package com.example.victor.latrans.util;

import android.arch.lifecycle.LiveData;

/**
 * Created by Victor on 9/5/2017.
 */

public class AbsentLiveData extends LiveData {
    private AbsentLiveData() {
        postValue(null);
    }
    public static <T> LiveData<T> create() {
        //noinspection unchecked
        return new AbsentLiveData();
    }
}