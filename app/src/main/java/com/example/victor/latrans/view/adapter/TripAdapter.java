package com.example.victor.latrans.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 9/2/2017.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {


    private List<Trip> mTrips;
    private Context mContext;

    public TripAdapter(List<Trip> trips, Context context){
        this.mTrips = trips;
        this.mContext = context;
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
        String travelFromState = trip.traveling_from_state;
        String travelFromCity = trip.traveling_from_city;
        String from = travelFromCity + ", " + travelFromState;
        String travelToState = trip.traveling_to_state;
        String travelToCity = trip.traveling_to_city;
        String to = travelToCity + ", " + travelToState;
        holder.mTextViewTimePosted.setText(String.valueOf(DateUtils.formatDateTime(trip.getPosted_on())));
        holder.mTextViewTravellingDate.setText(trip.traveling_date);
        holder.mTextViewTravelingTo.setText(to);
        holder.mTextViewTravellinfFrom.setText(from);

    }

    @Override
    public int getItemCount() {
        return (mTrips == null) ? 0: mTrips.size();
    }

    public class TripHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.traveling_to)
        TextView mTextViewTravelingTo;
        @BindView(R.id.travelling_from) TextView mTextViewTravellinfFrom;
        @BindView(R.id.traveling_date) TextView mTextViewTravellingDate;
        @BindView(R.id.time_posted) TextView mTextViewTimePosted;
        @BindView(R.id.profile_image)
        ImageView mImageViewProfile;
        public TripHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void addTrips(List<Trip> trips) {
        mTrips.clear();
        mTrips.addAll(trips);
        notifyDataSetChanged();
    }

    public void clearTrips() {
        mTrips.clear();
        notifyDataSetChanged();
    }

}
