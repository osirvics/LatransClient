package com.example.victor.latrans.repocitory;


import android.arch.lifecycle.LiveData;

import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.ConversationAndMessage;
import com.example.victor.latrans.repocitory.local.model.Profile;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;

import java.io.File;
import java.util.List;

public interface MessageRepository {

    LiveData<Resource<List<Message>>> getMessages(long id);
    LiveData<Resource<List<ConversationAndMessage>>> getMessagesAndConversation(long id);

    LiveData<Resource<User>> getUser();
    LiveData<Resource<User>> updateUser(long id, Profile profile);
    LiveData<Resource<UploadResponse>> beginUpload(File file);
}
