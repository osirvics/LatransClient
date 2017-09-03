package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.Conversation;

import java.util.List;


@Dao
public interface ConversationDao {
    @Query("select * from conversation")
    LiveData<List<Conversation>> getAllConversation();

    @Query("select * from conversation where id = :id")
    LiveData<Conversation> getAConversationByID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertConversation(Conversation conversation);
}
