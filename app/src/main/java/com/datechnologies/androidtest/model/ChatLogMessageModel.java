package com.datechnologies.androidtest.model;

import com.google.gson.annotations.SerializedName;

/**
 * A data model that represents a chat log message fetched from the D & A Technologies Web Server.
 */

public class ChatLogMessageModel
{
    @SerializedName("user_id")
    public int userId;
    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("name")
    public String username;
    @SerializedName("message")
    public String message;
}
