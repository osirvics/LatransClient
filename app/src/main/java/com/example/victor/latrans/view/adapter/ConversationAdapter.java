package com.example.victor.latrans.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.util.DateUtils;
import com.example.victor.latrans.util.OnItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationHolder> {
    private List<ConversationAndMessage> mConversations;
    private Context mContext;
    private OnItemClick mOnItemClick;
    private long userId;

    public ConversationAdapter(List<ConversationAndMessage> conversations, Context context, long userId){
        this.mConversations = conversations;
        this.mContext = context;
        this.mOnItemClick = ((OnItemClick) context);
        this.userId = userId;
    }


    @Override
    public ConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_converstaion, parent, false);
        return  new ConversationHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationHolder holder, int position) {
        ConversationAndMessage conversation = mConversations.get(position);
        holder.mTextViewSenderName.setText(conversation.sender_first_name);
        holder.mTextViewLastMessage.setText(conversation.message);
        holder.mTextViewMessageTime.setText(String.valueOf(DateUtils.formatDateTime(conversation.time_sent)));
        Glide.with(mContext).load(conversation.sender_picture).placeholder(R.drawable.ic_person_grey_600_24dp)
                .error(R.drawable.ic_person_grey_600_24dp).centerCrop().into(holder.mImageViewProfileImage);
    }

    @Override
    public int getItemCount() {
        return (mConversations == null) ? 0: mConversations.size();
    }

    public void addConversation(List<ConversationAndMessage> trips) {
        mConversations.clear();
        mConversations.addAll(trips);
        notifyDataSetChanged();
    }

    public void clearConversations() {
        mConversations.clear();
        notifyDataSetChanged();
    }

    public class ConversationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.sender_username)TextView mTextViewSenderName;
        @BindView(R.id.last_message)TextView mTextViewLastMessage;
        @BindView(R.id.profile_image)
        ImageView mImageViewProfileImage;
        @BindView(R.id.last_message_time) TextView mTextViewMessageTime;
        public ConversationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            long conversationId = mConversations.get(getLayoutPosition()).id;
            long senderId = mConversations.get(getLayoutPosition()).sender_id;
            long recipientId = mConversations.get(getLayoutPosition()).recipient_id;
            if(userId == senderId)
                mOnItemClick.onClick(conversationId, recipientId);
            else
                mOnItemClick.onClick(conversationId, senderId);
        }
    }
}
