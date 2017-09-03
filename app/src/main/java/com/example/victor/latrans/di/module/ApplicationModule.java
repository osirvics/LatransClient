package com.example.victor.latrans.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.victor.latrans.di.ApplicationContext;
import com.example.victor.latrans.di.DatabaseInfo;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by janisharali on 25/12/16.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

//    @Provides
//    @DatabaseInfo
//    String provideDatabaseName() {
//        return "demo-dagger.db";
//    }
//
//    @Provides
//    @DatabaseInfo
//    Integer provideDatabaseVersion() {
//        return 2;
//    }

    @Provides
    @DatabaseInfo
    AppDatabase provideLatransDatabase(){
     return    Room.databaseBuilder(mApplication,
                AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Provides
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences("latrans-prefs", Context.MODE_PRIVATE);
    }
}
