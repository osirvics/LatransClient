package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(indices = @Index(value = "id", unique = true), foreignKeys = @ForeignKey(entity = Conversation.class,
        parentColumns = "id",
        childColumns = "id"), tableName = "dialogue")
public class ConversationAndMessage {


    //TODO add colunm for recipient first and last name;

    @PrimaryKey
    public long id;
    public long sender_id;
    public long recipient_id;
    public String sender_first_name;
    public String sender_last_name;
    public String message;
    public String sender_picture;
    public long time_sent;

    @Ignore
    public ConversationAndMessage(){

    }

    public ConversationAndMessage(long id, long recipient_id, long sender_id, String sender_first_name, String sender_last_name,
                                  String message, String sender_picture, long time_sent) {
        this.id = id;
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.sender_first_name = sender_first_name;
        this.sender_last_name = sender_last_name;
        this.message = message;
        this.sender_picture = sender_picture;
        this.time_sent = time_sent;

    }
}
