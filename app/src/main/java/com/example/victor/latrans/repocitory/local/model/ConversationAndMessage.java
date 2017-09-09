package com.example.victor.latrans.repocitory.local.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.example.victor.latrans.repocitory.local.db.entity.Conversation;

/**
 * Created by Victor on 9/8/2017.
 */

@Entity(indices = @Index(value = "id", unique = true), foreignKeys = @ForeignKey(entity = Conversation.class,
        parentColumns = "id",
        childColumns = "id"), tableName = "dialogue")
public class ConversationAndMessage {

    @PrimaryKey
    public long id;
    public String sender_username;
    public String message;
    public String sender_picture;
    public long time_sent;

    @Ignore
    public ConversationAndMessage(){

    }

    public ConversationAndMessage(long id, String sender_username, String message, String sender_picture, long time_sent) {
        this.id = id;
        this.sender_username = sender_username;
        this.message = message;
        this.sender_picture = sender_picture;
        this.time_sent = time_sent;
    }
}
