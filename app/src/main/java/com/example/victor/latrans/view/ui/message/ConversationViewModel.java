package com.example.victor.latrans.view.ui.message;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;

import java.util.List;

import javax.inject.Inject;


public class ConversationViewModel extends ViewModel implements AppComponent.Injectable{
    @Inject
    MessageRepository mMessageRepository;

    private LiveData<Resource<List<ConversationAndMessage>>> mLiveData;


    @Override
    public void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public LiveData<Resource<List<ConversationAndMessage>>> getResponse(){
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return mLiveData;
    }

    private void queryResponse(){
        //TODO remove hardcoded id here
        mLiveData = mMessageRepository.getMessagesAndConversation(1);
    }
}