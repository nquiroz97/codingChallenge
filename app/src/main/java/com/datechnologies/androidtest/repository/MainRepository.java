package com.datechnologies.androidtest.repository;

import androidx.annotation.NonNull;

import com.datechnologies.androidtest.model.ChatResponse;
import com.datechnologies.androidtest.model.LoginBody;
import com.datechnologies.androidtest.model.LoginResponse;
import com.datechnologies.androidtest.network.RetroFitClientService;
import com.datechnologies.androidtest.network.RetroFitInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private static final int UNAUTHORIZED_CODE = 401;

    public MainRepository() {
    }

    public void loginRemote(LoginBody loginBody, OnLoginResponse loginResponse){
        RetroFitInterface loginInterface = RetroFitClientService.getRetrofitInstance().create(RetroFitInterface.class);
        Call<LoginResponse> startLogin = loginInterface.login(loginBody.getEmail(), loginBody.getPassword());

        startLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                long elapsedTime = response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis();
                if(response.isSuccessful()){
                    assert response.body() != null;
                    loginResponse.onResponse(new LoginResponse(response.body().getCode(), response.body().getMessage(), elapsedTime));
                }else if(response.code() == UNAUTHORIZED_CODE){
                    try {
                        assert response.errorBody() != null;
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        loginResponse.onResponse(new LoginResponse(errorObject.getString("code"), errorObject.getString("message"), elapsedTime));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    loginResponse.onFailure(new Throwable(response.message()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResponse.onFailure(t);
            }


        });
    }

    public void fetchChatMessages(OnChatResponse chatResponse){
        RetroFitInterface chatInterface = RetroFitClientService.getRetrofitInstance().create(RetroFitInterface.class);
        Call<ChatResponse> startLogin = chatInterface.getMessages();

        startLogin.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatResponse> call, @NonNull Response<ChatResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    chatResponse.onResponse(response.body());
                }else{
                    chatResponse.onFailure(new Throwable(response.message()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<ChatResponse> call, @NonNull Throwable t) {
                chatResponse.onFailure(t);
            }


        });
    }

    public interface OnLoginResponse{
        void onResponse(LoginResponse loginResponse);
        void onFailure(Throwable throwable);
    }

    public interface OnChatResponse{
        void onResponse(ChatResponse chatResponse);
        void onFailure(Throwable throwable);
    }
}
