package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;

import java.util.List;

/**
 * Created by Victor on 9/9/2017.
 */

@Dao
public interface MessAndConvDoa {

    @Query("select * from dialogue ORDER BY id DESC")
    LiveData<List<ConversationAndMessage>> getAllDialogue();

    @Query("select * from dialogue")
    List<ConversationAndMessage> getAllDialogueNL();

    @Query("select * from dialogue where id = :id")
    LiveData<ConversationAndMessage> getADialgueByID(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDialogue(ConversationAndMessage dialogue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertdialogueList(List<ConversationAndMessage> dialogue);

    @Query("DELETE FROM dialogue")
    void deleteAll();
}
