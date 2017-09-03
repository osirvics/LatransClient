package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

@Entity(primaryKeys = {"conversation_id", "id", "sender_id", "recipient_id"}, foreignKeys = {
        @ForeignKey(entity = Conversation.class,
                parentColumns = {"id", "user_one_id","user_two_id"},
                childColumns = {"conversation_id", "sender_id","recipient_id"},
                onUpdate = ForeignKey.CASCADE,
                onDelete= ForeignKey.NO_ACTION,
                deferred = true)
                })
@SuppressWarnings("all")
public class Message {

    public int id;
    public int sender_id;
    public int recipient_id;
    public String sender_username;
    public String message;
    public long time_sent;
    public int conversation_id;

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
    public Message(int id, int sender_id, int recipient_id, String sender_username, String message, long time_sent, int conversation_id) {
        super();
        this.id = id;
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.sender_username = sender_username;
        this.message = message;
        this.time_sent = time_sent;
        this.conversation_id = conversation_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return sender_id;
    }

    public void setSenderId(int senderId) {
        this.sender_id = senderId;
    }

    public int getRecipientId() {
        return recipient_id;
    }

    public void setRecipientId(int recipientId) {
        this.recipient_id = recipientId;
    }

    public String getSenderUsername() {
        return sender_username;
    }

    public void setSenderUsername(String senderUsername) {
        this.sender_username = senderUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeSent() {
        return time_sent;
    }

    public void setTimeSent(int timeSent) {
        this.time_sent = timeSent;
    }

    public int getConversationId() {
        return conversation_id;
    }

    public void setConversationId(int conversationId) {
        this.conversation_id = conversationId;
    }

}
