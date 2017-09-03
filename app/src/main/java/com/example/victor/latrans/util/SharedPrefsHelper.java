package com.example.victor.latrans.util;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class SharedPrefsHelper {

    private SharedPreferences mSharedPreferences;
    private static SharedPrefsHelper sInstance;

    public static String PREF_KEY_ACCESS_TOKEN = "access-token";
    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";

    public static synchronized SharedPrefsHelper getInstance(Context context) {
        if (sInstance == null) {
            //TODO use app.getContext
            sInstance = new SharedPrefsHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    @Inject
    public SharedPrefsHelper( SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }



    public SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences("latrans-prefs", Context.MODE_PRIVATE);
    }


    public void setAccessToken(String accessToken) {
        mSharedPreferences.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }


    public String getAccessToken() {
        return mSharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null);
    }


    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }
}