package com.example.victor.latrans.repocitory.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


//indices = {@Index("id"), @Index("user_one_id"), @Index("user_two_id")}
//primaryKeys = {"id", "user_one_id", "user_two_id"}, indices = @Index("id")
//, primaryKeys = {"id", "user_one_id", "user_two_id"}
@Entity(indices = arrayOf(Index(value = *arrayOf("user_one_id", "user_two_id"), unique = true), Index(value = "id", unique = true)))
data class Conversation(@PrimaryKey var id: Long? = null, var user_one_id: Long? = null, var user_two_id: Long? = null)