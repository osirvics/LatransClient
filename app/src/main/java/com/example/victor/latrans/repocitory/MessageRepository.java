package com.example.victor.latrans.repocitory;


import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;

import java.util.List;

public interface MessageRepository {

    //LiveData<Resource<List<ConversationAndMessage>>> getConversation(long id);
    LiveData<Resource<List<Message>>> getMessages(long id);
    LiveData<Resource<List<ConversationAndMessage>>> getMessagesAndConversation(long id);
}
