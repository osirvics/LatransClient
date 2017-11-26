package com.example.victor.latrans.repocitory.local.db.entity


import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = "id", unique = true)))
data class Request

(@PrimaryKey var id: Long? = null, var user_id: Long? =null, var delivery_state: String? = null, var delivery_city: String?=null,
 var item_location_state: String? = null, var item_location_city: String? = null, var picture: String? = null,
 var offer_amount: String? = null, var deliver_before: String? = null, var posted_on: Long? = null, var time_updated: Long? = null,
 var item_name: String? = null, var profile_image: String? = null, var user_first_name: String? = null,
 var user_last_name: String? = null, var phone_no: String? = null)



