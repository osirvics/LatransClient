package com.example.victor.latrans.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Message> mMessages;
    private Context mContext;
    private long mSenderId;

    public MessageAdapter(List<Message> messages, Context context, long senderId){
        this.mMessages = messages;
        this.mContext = context;
        this.mSenderId = senderId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater
                        .inflate(R.layout.item_message_sent, parent, false);
                return new SendMessageHolder(view);
            case VIEW_TYPE_MESSAGE_RECEIVED:
                LayoutInflater layoutInflater2 = LayoutInflater.from(parent.getContext());
                View view2 = layoutInflater2
                        .inflate(R.layout.item_message_recived, parent, false);
                return new RecievedMessageHolder(view2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SendMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((RecievedMessageHolder)holder).bind(message);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (mMessages == null) ? 0: mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        if(message.getId()== mSenderId) return VIEW_TYPE_MESSAGE_SENT;
        else return VIEW_TYPE_MESSAGE_RECEIVED;
    }


    public class SendMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_message_body) TextView mTextViewMessageBody;
        @BindView(R.id.text_message_time) TextView mTextViewSentTime;
        public SendMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Message message){
            mTextViewMessageBody.setText(message.getMessage());
            mTextViewSentTime.setText(String.valueOf(DateUtils.formatDateTime(message.getTimeSent())));
        }
    }

    public class RecievedMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_message_profile)
        ImageView mImageViewSenderImage;
        @BindView(R.id.text_message_name) TextView mTextViewSenderName;
        @BindView(R.id.text_message_body) TextView mTextViewSenderMessage;
        @BindView(R.id.text_message_time) TextView mTextViewSenderTime;
        public RecievedMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        private void bind(Message message){
            mTextViewSenderName.setText(message.getSenderUsername());
            mTextViewSenderMessage.setText(message.getMessage());
            mTextViewSenderTime.setText(String.valueOf(DateUtils.formatDateTime(message.getTimeSent())));


        }
    }
}
