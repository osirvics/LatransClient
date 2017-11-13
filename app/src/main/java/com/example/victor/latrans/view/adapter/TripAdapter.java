package com.example.victor.latrans.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.util.DateUtils;
import com.example.victor.latrans.util.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private List<Trip> mTrips;
    private Context mContext;
    private OnItemClick mOnItemClick;
    private long userId;

    public TripAdapter(List<Trip> trips, Context context, long id) {
        this.mTrips = trips;
        this.mContext = context;
        this.mOnItemClick = (OnItemClick) context;
        this.userId = id;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trip_item, parent, false);
        return new TripHolder(view);
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {

        Trip trip = mTrips.get(position);
        if (trip.user_id == userId) {
            holder.mButtonSendMessage.setVisibility(View.GONE);
        }
        String travelFromState = trip.traveling_from_state;
        String travelFromCity = trip.traveling_from_city;
        String from = travelFromCity + ", " + travelFromState;
        String travelToState = trip.traveling_to_state;
        String travelToCity = trip.traveling_to_city;
        String to = travelToCity + ", " + travelToState;
        holder.mTextViewTimePosted.setText(String.valueOf(DateUtils.formatDateTime(trip.posted_on)));
        holder.mTextViewTravellingDate.setText(trip.traveling_date);
        holder.mTextViewTravelingTo.setText(to);
        holder.mTextViewTravellinfFrom.setText(from);
        holder.mUserName.setText(mContext.getString(R.string.trip_travelling, trip.user_first_name));
        Glide.with(mContext).load(trip.profile_image).placeholder(R.drawable.ic_person_grey_600_24dp)
                .error(R.drawable.ic_person_grey_600_24dp).centerCrop().into(holder.mImageViewProfile);


    }

    @Override
    public int getItemCount() {
        return (mTrips == null) ? 0 : mTrips.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.traveling_to)
        TextView mTextViewTravelingTo;
        @BindView(R.id.travelling_from)
        TextView mTextViewTravellinfFrom;
        @BindView(R.id.traveling_date)
        TextView mTextViewTravellingDate;
        @BindView(R.id.time_posted)
        TextView mTextViewTimePosted;
        @BindView(R.id.profile_image)
        ImageView mImageViewProfile;
        @BindView(R.id.send_message)
        Button mButtonSendMessage;
        @BindView(R.id.user_name)
        TextView mUserName;

        public TripHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mButtonSendMessage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.send_message:
                    long userId = mTrips.get(getLayoutPosition()).user_id;
                    //passing -1 here because we can't tell if a conversation exists between the users
                    mOnItemClick.onClick(-1, userId);
                    break;
            }
        }
    }

    public void addTrips(List<Trip> trips) {
        if(trips == null )
            return;
        mTrips.clear();
        mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public void clearTrips() {
        mTrips.clear();
        notifyDataSetChanged();
    }

}
