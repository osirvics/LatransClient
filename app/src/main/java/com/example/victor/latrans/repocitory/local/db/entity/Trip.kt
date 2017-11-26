package com.example.victor.latrans.repocitory.local.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = "id", unique = true)))
 data class Trip(@PrimaryKey var id: Long? = null, var phone_no: String? = null,
            var posted_on: Long? = null, var time_updated: Long? = null,
            var profile_image: String? = null, var traveling_date: String? = null,
            var traveling_from_city: String? = null, var traveling_from_state: String? = null,
            var traveling_to_city: String? = null, var traveling_to_state: String? = null,
            var user_id: Long? = null, var user_first_name: String? = null,
            var user_last_name: String? = null)


