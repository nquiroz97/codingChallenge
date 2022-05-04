package com.datechnologies.androidtest.viewmodels;

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

        // retrofit call when viewModel is initialized
        getChats();
    }

    private void getChats(){
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
