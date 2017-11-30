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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.util.DateUtils;
import com.example.victor.latrans.util.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<Request> mRequests;
    private Context mContext;
    private long userId;
    private OnItemClick mOnItemClick;


    public OrderAdapter(List<Request> requests, Context context, long userId, OnItemClick onItemClick) {
        this.mRequests = requests;
        this.mContext = context;
        this.userId = userId;
        this.mOnItemClick = onItemClick;
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
        if (request.getUser_id() == userId)
            holder.mButtonDeliverItem.setVisibility(View.GONE);
//        Glide.with(mContext).load(request.getProfile_image()).placeholder(R.drawable.ic_person_grey_600_24dp)
//                .crossFade().into(holder.mProfileImage);
//        Glide.with(mContext).load(request.getPicture()).placeholder(R.drawable.nike)
//                .crossFade().into(holder.mItemImage);
        loadItemImage(request.getPicture(), holder);
        loadImage(request.getProfile_image(), holder);
        String deliveryLocation = request.getDelivery_city() + ", " + request.getDelivery_state();
        holder.mDeliveryStateCity.setText(deliveryLocation);
        String itemLocation = request.getItem_location_city() + ", " + request.getItem_location_state();
        holder.mItemLocationStateCity.setText(itemLocation);
        holder.mDeliveryTime.setText(request.getDeliver_before());
        holder.mTimePosted.setText(DateUtils.formatDateTime(request.getPosted_on()));
        holder.mItemName.setText(request.getItem_name());
        holder.mRewardStartingAmount.setText(mContext.getString(R.string.reward_amount,request.getOffer_amount()) );
        holder.mUserName.setText(request.getUser_first_name());


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

    public class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        //RatingView amount;
        @BindView(R.id.button_deliver_item)
        Button mButtonDeliverItem;
        @BindView(R.id.reward_starting_amount)
        TextView mRewardStartingAmount;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
          //  amount = (RatingView)itemView.findViewById(R.id.floatingActionButton);
            itemView.setOnClickListener(this);
            mButtonDeliverItem.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_deliver_item:
                    final long user_id = mRequests.get(getLayoutPosition()).getUser_id();
                        //passing -1 here because we can't tell if a conversation exists between the users
                        mOnItemClick.onClick(-1, user_id);
                        break;

            }
        }
    }
    void loadItemImage(String url, OrderHolder holder){
                Glide.with(mContext).load(url)
                .placeholder(R.color.placeholder_grey_20)
                .fitCenter()
                        .crossFade()
                        .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.mItemImage);
    }
    void loadImage(String url, OrderHolder holder) {
        Glide.with(mContext).load(url)
                .placeholder(R.color.placeholder_grey_20)
                .fitCenter()
                .crossFade()
                .animate(android.R.anim.fade_in)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.mProfileImage);
    }

}
