package com.example.victor.latrans.repocitory.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = "id", unique = true)))
data class User

(var email: String? = null, @PrimaryKey var id: Long? =null, var first_name: String? = null, var last_name: String? = null,
 var picture: String? = null, var phone_no: String? = null)




