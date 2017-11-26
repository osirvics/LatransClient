package com.example.victor.latrans.repocitory.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(indices = arrayOf(Index(value = "id", unique = true)),
        foreignKeys = arrayOf(ForeignKey(entity = Conversation::class, parentColumns = arrayOf("id"),
                childColumns = arrayOf("id"))), tableName = "dialogue")

data class ConversationAndMessage(@PrimaryKey var id: Long? = null, var recipient_id: Long? = null, var sender_id: Long? = null,
                             var sender_first_name: String? = null, var sender_last_name: String? = null,
                             var recipient_first_name: String? = null, var recipient_last_name: String? = null,
                             var message: String? = null,
                             var sender_picture: String? = null, var time_sent: Long? = null)
