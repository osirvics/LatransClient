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
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<Request> mRequests;
    private Context mContext;
    private long userId;


    public OrderAdapter(List<Request> requests, Context context, long userId) {
        this.mRequests = requests;
        this.mContext = context;
        this.userId = userId;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_order, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        Request request = mRequests.get(position);
        if (request.user_id == userId)
            holder.mButtonDeliverItem.setVisibility(View.GONE);
        Glide.with(mContext).load(request.profile_image).placeholder(R.drawable.ic_person_grey_600_24dp)
                .crossFade().into(holder.mProfileImage);
        Glide.with(mContext).load(request.picture).placeholder(R.drawable.nike)
                .crossFade().into(holder.mItemImage);
        String deliveryLocation = request.delivery_city + ", " +  request.delivery_state;
        holder.mDeliveryStateCity.setText(deliveryLocation);
        String itemLocation = request.item_location_city + ", " + request.item_location_state;
        holder.mItemLocationStateCity.setText(itemLocation);
        holder.mDeliveryTime.setText(request.deliver_before);
        holder.mTimePosted.setText(DateUtils.formatDateTime(request.posted_on));
        holder.mItemName.setText(request.item_name);
        holder.mRewardStartingAmount.setText(request.offer_amount);
        holder.mUserName.setText(request.user_first_name);


    }

    @Override
    public int getItemCount() {
        return (mRequests == null) ? 0 : mRequests.size();
    }

    public void addOrder(List<Request> requests){
        if(requests == null)
            return;
        mRequests.clear();
        mRequests.addAll(requests);
        notifyDataSetChanged();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name) TextView mUserName;
        @BindView(R.id.profile_image)
        CircleImageView mProfileImage;
        @BindView(R.id.time_posted)
        TextView mTimePosted;
        @BindView(R.id.delivery_state_city)
        TextView mDeliveryStateCity;
        @BindView(R.id.item_location_state_city)
        TextView mItemLocationStateCity;
        @BindView(R.id.item_image)
        ImageView mItemImage;
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.dleivery_time)
        TextView mDeliveryTime;
        @BindView(R.id.button_deliver_item)
        Button mButtonDeliverItem;
        @BindView(R.id.reward_starting_amount)
        TextView mRewardStartingAmount;
        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
