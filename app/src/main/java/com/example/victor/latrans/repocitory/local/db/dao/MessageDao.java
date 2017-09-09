package com.example.victor.latrans.repocitory.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface MessageDao {
    @Query("select * from message")
    LiveData<List<Message>> getAllMessage();

    @Query("select * from message")
    List<Message> getAllMessageNL();

    @Query("select * from message where conversation_id = :conversation_id")
    LiveData<List<Message>> getAllConversation(long conversation_id);

//    @Query("SELECT Conversation.id, Message.sender_username, Message.message, Message.sender_picture, Message.time_sent From conversation " +
//            "INNER JOIN Message ON Conversation.id = Message.conversation_id ")
//       LiveData<List<ConversationAndMessage>> findConversationAndMessage();


    @Query("SELECT Conversation.id, Message.sender_username, Message.message, Message.sender_picture, Message.time_sent "
            + "FROM conversation, message "
            + "WHERE message.conversation_id = conversation.id")
    LiveData<List<ConversationAndMessage>> findConversationAndMessage();


    @Query("select * from message where id = :id")
    LiveData<Message> getAMessageById(long id);

    @Insert(onConflict = REPLACE)
    public void insertMesaage(Message message);



}
