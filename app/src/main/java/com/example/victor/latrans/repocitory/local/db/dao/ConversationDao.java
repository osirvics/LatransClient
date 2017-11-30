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

    @Query("select * from conversation")
    List<Conversation> getAllConversationNL();

    @Query("select * from conversation where id = :id")
    LiveData<Conversation> getAConversationByID(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConversation(Conversation conversation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Conversation> conversations);

    @Query("select * from conversation where user_one_id = :userId OR user_two_id = :userId  ")
    LiveData<Conversation> getAConversationByUserId(long userId);

    @Query("DELETE FROM conversation")
    void deleteAll();


}
