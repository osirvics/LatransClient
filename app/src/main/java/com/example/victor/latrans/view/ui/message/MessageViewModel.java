package com.example.victor.latrans.view.ui.message;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.util.Util;

import java.util.List;

import javax.inject.Inject;

public class MessageViewModel extends ViewModel implements AppComponent.Injectable {

    @Inject
    MessageRepository mMessageRepository;

    long senderId;
    long recipientId;
    String mMessage;

    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    private LiveData<Resource<List<Message>>> mLiveData;
    private MutableLiveData<Long> dialogueId = new MutableLiveData<>();
    private LiveData<Resource<User>> mUserData;
    private LiveData<Resource<Message>> mSendMessage;

     public void setDialogueId(long id){
         dialogueId.setValue(id);
    }

    public LiveData<Resource<List<Message>>> getResponse(){
        mLiveData = new MutableLiveData<>();
        processResponse();
        return mLiveData;
    }

    private void processResponse(){
        mLiveData = Transformations.switchMap(dialogueId, input -> {
//            if (input == -1){
//                return AbsentLiveData.create();
//            }
            return  mMessageRepository.getMessagesInConversation(input);
        });
    }

    public LiveData<Resource<User>> getUserData(){
            mUserData = new MutableLiveData<>();
            mUserData = mMessageRepository.getUser();
        return mUserData;
    }

    public LiveData<Resource<Message>> sendMessage(){
        mSendMessage = new MutableLiveData<>();
        mSendMessage = mMessageRepository.postMessage(buildMessage());
        return mSendMessage;
    }

    public Message buildMessage(){
        Message message = new Message();
        message.setSender_id(senderId);
        message.setRecipient_id(recipientId);
        message.setTime_sent(System.currentTimeMillis());
        message.setSent_status(Util.MESSAGE_PENDING);
        message.setMessage(mMessage.trim());
        return  message;
    }

}
