package com.datechnologies.androidtest.chat;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.datechnologies.androidtest.model.ChatLogMessageModel;
import com.datechnologies.androidtest.model.ChatResponse;
import com.datechnologies.androidtest.repository.MainRepository;

import java.util.ArrayList;
import java.util.List;

public class ChatActivityViewModel extends ViewModel {

    MutableLiveData<ArrayList<ChatLogMessageModel>> chatLiveData;
    ArrayList<ChatLogMessageModel> chatArrayList = new ArrayList<>();

    MainRepository mainRepository = new MainRepository();

    public ChatActivityViewModel() {
        chatLiveData = new MutableLiveData<>();

        // call your Rest API in init method
        getChats();
    }

    public void getChats(){
        mainRepository.fetchChatMessages(new MainRepository.OnChatResponse() {
            @Override
            public void onResponse(ChatResponse chatResponse) {
                if(!chatResponse.getChats().isEmpty()){
                    populateList(chatResponse.getChats());
                    chatLiveData.setValue(chatArrayList);
                    Log.v("success", String.valueOf(chatResponse.getChats().get(0).avatarUrl));
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    public MutableLiveData<ArrayList<ChatLogMessageModel>> getChatMutableLiveData(){
        return chatLiveData;
    }

    public void populateList(List<ChatLogMessageModel> fetchedChats){
        chatArrayList.addAll(fetchedChats);
    }


}
