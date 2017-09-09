package com.example.victor.latrans.repocitory.local.model;

import com.example.victor.latrans.repocitory.local.db.entity.Conversation;

import java.util.List;

/**
 * Created by Victor on 9/8/2017.
 */

public class ConversationResponse {
    private List<Conversation> conversations = null;

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }
}
