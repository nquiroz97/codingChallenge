package com.datechnologies.androidtest.network;


import com.datechnologies.androidtest.model.ChatResponse;
import com.datechnologies.androidtest.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroFitInterface {

    @FormUrlEncoded
    @POST("/login.php")
    Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);


    @GET("/chat_log.php")
    Call <ChatResponse> getMessages();
}
