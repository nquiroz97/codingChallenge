package com.datechnologies.androidtest.views;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.util.NetworkConnectionManager;
import com.datechnologies.androidtest.viewmodels.ChatActivityViewModel;
import com.datechnologies.androidtest.adapter.ChatAdapter;
import com.datechnologies.androidtest.databinding.ActivityChatBinding;
import com.datechnologies.androidtest.model.ChatLogMessageModel;
import java.util.ArrayList;

/**
 * Screen that displays a list of chats from a chat log.
 */
public class ChatActivity extends CommonActivity {

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private ChatAdapter chatAdapter;
    private ProgressBar loadingIndicator;
    private TextView messageText;
    NetworkConnectionManager networkConnectionManager;
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
    protected void onStart() {
        super.onStart();
        networkConnectionManager.registerConnectionObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkConnectionManager.unregisterConnectionObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        networkConnectionManager = new NetworkConnectionManager(this);
        setupUI();

        // DONE: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.

        // DONE: Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php
        // DONE: Parse this chat data from JSON into ChatLogMessageModel and display it.
    }

    private void handleNetworkDisplay() {
        loadingIndicator.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            if(networkConnectionManager.isNetworkAvailable){
                chatActivityViewModel.getChats();
                new Handler().postDelayed(() -> {
                    if(chatActivityViewModel.chatArrayList.size() == 0){
                        messageText.setVisibility(View.VISIBLE);
                        messageText.setText(R.string.no_chats);
                    }
                }, 2000);
            }else{
                messageText.setVisibility(View.VISIBLE);
                messageText.setText(R.string.no_internet);
            }
            loadingIndicator.setVisibility(View.GONE);
        }, 5000);
    }

    private void setupUI() {
        setupActionBar();


        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(ChatActivity.this, R.layout.activity_chat);
        activityChatBinding.setLifecycleOwner(this);
        chatActivityViewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        activityChatBinding.setChatActivityViewModel(chatActivityViewModel);
        loadingIndicator = activityChatBinding.loadingIndicator;
        messageText = activityChatBinding.messageTextView;
        RecyclerView recyclerView = activityChatBinding.idRecyclerView;

        handleNetworkDisplay();

        chatAdapter = new ChatAdapter();

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false));

        chatActivityViewModel.getChatMutableLiveData().observe(this, chatListUpdateObserver);
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

