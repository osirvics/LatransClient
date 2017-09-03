package com.example.victor.latrans.repocitory.local.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

import com.example.victor.latrans.di.ApplicationContext;

import javax.inject.Singleton;

/**
 * Created by Victor on 27/08/2017.
 */
@Singleton
public class DatabaseManager {
    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    @Nullable
    public AppDatabase getDatabase() {
        return mDb;
    }

    private AppDatabase mDb;
    private Context mContext;


    public DatabaseManager(@ApplicationContext Context context, AppDatabase database){
        this.mDb = database;
        mContext = context;
    }




    private DatabaseManager(Context context) {
        mDb =  Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }
}
