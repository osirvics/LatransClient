package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.Message;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface MessageDao {
    @Query("select * from message")
    LiveData<List<Message>> getAllMessage();

    @Query("select * from message where conversation_id = :conversation_id")
    LiveData<List<Message>> getAllMessagesInAConversation(int conversation_id);

    @Query("select * from message where id = :id")
    LiveData<Message> getAMessageById(int id);

    @Insert(onConflict = REPLACE)
    public void insertMesaage(Message message);



}
