package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity( foreignKeys = {
        @ForeignKey(entity = Conversation.class,
                parentColumns = {"id"},
                childColumns = {"conversation_id"},
                onUpdate = ForeignKey.CASCADE,
                onDelete= ForeignKey.NO_ACTION,
                deferred = true)
                }, indices = {@Index(value = "id", unique = true), @Index(value = "conversation_id")})
@SuppressWarnings("all")
public class Message {
    @PrimaryKey
    public long id;
    public long sender_id;
    public long recipient_id;
    public String sender_first_name;
    public String sender_last_name;
    public String message;
    public String sender_picture;
    public long time_sent;
    public long conversation_id;
    public String sent_status;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Message() {
    }

    /**
     *
     * @param message
     * @param id
     * @param recipient_id
     * @param conversation_id
     * @param sender_id
     * @param time_sent
     * @param sender_username
     */
    public Message(long id, long sender_id, long recipient_id, String sender_first_name,
                   String sender_last_name, String message, long time_sent,
                   long conversation_id, String sender_picture, String sent_status) {
        super();
        this.id = id;
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.sender_first_name = sender_first_name;
        this.sender_last_name = sender_last_name;
        this.message = message;
        this.time_sent = time_sent;
        this.conversation_id = conversation_id;
        this.sender_picture = sender_picture;
        this.sent_status = sent_status;
    }


}
