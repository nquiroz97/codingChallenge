package com.datechnologies.androidtest.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;

    long requestCallTime;

    public LoginResponse(String code, String message, long requestCallTime) {
        this.code = code;
        this.message = message;
        this.requestCallTime = requestCallTime;
    }

    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

    public long getRequestCallTime() {
        return requestCallTime;
    }

}
