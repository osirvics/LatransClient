package com.example.victor.latrans.repocitory.local.model;


import com.example.victor.latrans.repocitory.local.db.entity.Message;

import java.util.List;

public class MessageResponse {
    private List<Message> messages = null;
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
