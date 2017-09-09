package com.example.victor.latrans.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;
import com.example.victor.latrans.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationHolder> {
    private List<ConversationAndMessage> mConversations;
    private Context mContext;

    public ConversationAdapter(List<ConversationAndMessage> conversations, Context context){
        this.mConversations = conversations;
        this.mContext = context;
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
        holder.mTextViewSenderName.setText(conversation.sender_username);
        holder.mTextViewLastMessage.setText(conversation.message);
        holder.mTextViewMessageTime.setText(String.valueOf(DateUtils.formatDateTime(conversation.time_sent)));
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

    public class ConversationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sender_username)TextView mTextViewSenderName;
        @BindView(R.id.last_message)TextView mTextViewLastMessage;
        @BindView(R.id.profile_image)
        ImageView mImageViewProfileImage;
        @BindView(R.id.last_message_time) TextView mTextViewMessageTime;
        public ConversationHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
