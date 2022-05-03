package com.datechnologies.androidtest.model;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;

    public LoginBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

}
