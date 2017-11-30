package com.example.victor.latrans.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.victor.latrans.R
import com.example.victor.latrans.repocitory.local.db.entity.Trip
import com.example.victor.latrans.util.ActionDial
import com.example.victor.latrans.util.DateUtils
import com.example.victor.latrans.util.OnItemClick
import timber.log.Timber

class TripAdapter(private val mTrips: MutableList<Trip>?, private val mContext: Context,
                  private val mOnItemClick: OnItemClick, private val onDial: ActionDial, private val userId: Long) : RecyclerView.Adapter<TripAdapter.TripHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.trip_item, parent, false)
        return TripHolder(view)
    }

    override fun onBindViewHolder(holder: TripHolder, position: Int) {
//        val (_, _, posted_on, _, profile_image, traveling_date, travelFromCity, travelFromState,
//                travelToCity, travelToState, user_id, user_first_name) = mTrips[position]


        with(mTrips!![position]){

            if (user_id == userId) {
                holder.mButtonSendMessage!!.visibility = View.GONE
                holder.mButtonCall.visibility = View.GONE
            }
            val from = traveling_from_city + ", " + traveling_from_state
            val to = traveling_to_city + ", " + traveling_to_state
            holder.mTextViewTimePosted.text = DateUtils.formatDateTime(posted_on!!).toString()
            holder.mTextViewTravellingDate.text = traveling_date
            holder.mTextViewTravelingTo.text = to
            holder.mTextViewTravellinfFrom!!.text = from
            holder.mUserName.text = mContext.getString(R.string.trip_travelling, user_first_name)
            //loadImage(profile_image, holder)
            Glide.with(mContext).load(profile_image).placeholder(R.drawable.ic_person_grey_600_24dp)
                    .error(R.drawable.ic_person_grey_600_24dp).centerCrop().crossFade().into(holder.mImageViewProfile)
        }


    }

    override fun getItemCount(): Int {
        return mTrips?.size ?: 0
    }

    inner class TripHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mTextViewTravelingTo =  itemView.findViewById<TextView>(R.id.traveling_to)
        var mTextViewTravellinfFrom = itemView.findViewById<TextView>(R.id.travelling_from)
        var mTextViewTravellingDate = itemView.findViewById<TextView>(R.id.traveling_date)
        var mTextViewTimePosted = itemView.findViewById<TextView>(R.id.time_posted)
        var mImageViewProfile = itemView.findViewById<ImageView>(R.id.profile_image)
        var mButtonSendMessage = itemView.findViewById<TextView>(R.id.send_message)
        var mButtonCall = itemView.findViewById<TextView>(R.id.call)
        var mUserName = itemView.findViewById<TextView>(R.id.user_name)

        init {
           // ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
            mButtonSendMessage!!.setOnClickListener(this)
            mButtonCall!!.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.send_message -> {
                    val userId = mTrips!![layoutPosition].user_id!!
                    //passing -1 here because we can't tell if a conversation exists between the users
                    mOnItemClick.onClick(-1, userId)
                }
                R.id.call -> {
                    val trip = mTrips!![layoutPosition]
                    val fullName = trip.user_first_name + " " + trip.user_last_name
                    Timber.e("Name " + fullName )
                    onDial.dial(trip.phone_no!!, fullName)
                }
            }
        }
    }

    fun loadImage(url : String, holder : TripHolder ){
                Glide.with(mContext).load(url)
                .placeholder(R.color.placeholder_grey_20)
                        .error(R.color.placeholder_grey_20)
                .fitCenter()
                        .crossFade()
                        .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(object : RequestListener<String, GlideDrawable> {
                    override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }
                    override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(holder.mImageViewProfile)
    }


    fun addTrips(trips: List<Trip>?) {
        if (trips == null)
            return
        mTrips!!.clear()
        mTrips.addAll(trips)
        notifyDataSetChanged()
    }

    fun clearTrips() {
        mTrips!!.clear()
        notifyDataSetChanged()
    }



}
