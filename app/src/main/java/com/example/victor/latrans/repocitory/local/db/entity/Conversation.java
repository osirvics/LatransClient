package com.example.victor.latrans.repocitory.local.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;



//indices = {@Index("id"), @Index("user_one_id"), @Index("user_two_id")}
//primaryKeys = {"id", "user_one_id", "user_two_id"}, indices = @Index("id")
@Entity(indices = {@Index(value = {"user_one_id", "user_two_id"},
        unique = true), @Index(value = "id", unique = true)}, primaryKeys = {"id", "user_one_id", "user_two_id"})
@SuppressWarnings("all")
public class Conversation {
    public long id;
    public long user_one_id;
    public long user_two_id;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Conversation() {
    }

    /**
     *
     * @param id
     * @param user_two_id
     * @param user_one_id
     */
    public Conversation(long id, long user_one_id, long user_two_id) {
        super();
        this.id = id;
        this.user_one_id = user_one_id;
        this.user_two_id = user_two_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserOneId() {
        return user_one_id;
    }

    public void setUserOneId(long userOneId) {
        this.user_one_id = userOneId;
    }

    public long getUserTwoId() {
        return user_two_id;
    }

    public void setUserTwoId(long userTwoId) {
        this.user_two_id = userTwoId;
    }

}