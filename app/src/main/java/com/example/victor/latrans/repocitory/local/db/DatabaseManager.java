package com.example.victor.latrans.repocitory.local.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Victor on 27/08/2017.
 */

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

    private DatabaseManager(Context context) {
        mDb =  Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }
}
