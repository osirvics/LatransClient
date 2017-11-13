package com.example.victor.latrans.repocitory;


import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.Profile;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;

import java.io.File;
import java.util.List;

public interface MessageRepository {

    //Retrieves list of messages of a user in a conversation
    LiveData<Resource<List<Message>>> getMessagesInConversation(long conversationId);
    LiveData<Resource<List<ConversationAndMessage>>> getMessagesAndConversation(long userId);
    // Gets user data for initialisation
    LiveData<Resource<User>> getUser();
    //Updates a user data
    LiveData<Resource<User>> updateUser(long id, Profile profile);
    // Upload file to amazon
    LiveData<Resource<UploadResponse>> beginUpload(File file);
    // Send message to server
    LiveData<Resource<Message>> postMessage(Message message);
}
