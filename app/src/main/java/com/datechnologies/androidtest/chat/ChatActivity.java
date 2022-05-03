package com.datechnologies.androidtest.chat;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.databinding.ActivityChatBinding;
import com.datechnologies.androidtest.model.ChatLogMessageModel;

import java.util.ArrayList;

/**
 * Screen that displays a list of chats from a chat log.
 */
public class ChatActivity extends AppCompatActivity {

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private ChatAdapter chatAdapter;
    ChatActivityViewModel chatActivityViewModel;


    //==============================================================================================
    // Static Class Methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent starter = new Intent(context, ChatActivity.class);
        context.startActivity(starter);
    }

    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(ChatActivity.this, R.layout.activity_chat);
        activityChatBinding.setLifecycleOwner(this);
        activityChatBinding.setChatActivityViewModel(chatActivityViewModel);

        RecyclerView recyclerView = activityChatBinding.idRecyclerView;

        chatAdapter = new ChatAdapter();

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        chatActivityViewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        chatActivityViewModel.getChatMutableLiveData().observe(this, chatListUpdateObserver);

        // DONE: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.

        // DONE: Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php
        // DONE: Parse this chat data from JSON into ChatLogMessageModel and display it.
    }

    Observer<ArrayList<ChatLogMessageModel>> chatListUpdateObserver = new Observer<ArrayList<ChatLogMessageModel>>() {
        @Override
        public void onChanged(ArrayList<ChatLogMessageModel> userArrayList) {
            chatAdapter.setChatLogMessageModelList(userArrayList);
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
