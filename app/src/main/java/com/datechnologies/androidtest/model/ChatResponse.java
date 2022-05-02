package com.datechnologies.androidtest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatResponse {
    @SerializedName("data")
    List<ChatLogMessageModel> chats;

    public List<ChatLogMessageModel> getChats() {
        return chats;
    }

    public void setChats(List<ChatLogMessageModel> chats) {
        this.chats = chats;
    }
}
