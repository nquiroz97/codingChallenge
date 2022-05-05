package com.datechnologies.androidtest.views;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datechnologies.androidtest.MainActivity;
import com.datechnologies.androidtest.R;
import com.datechnologies.androidtest.viewmodels.ChatActivityViewModel;
import com.datechnologies.androidtest.adapter.ChatAdapter;
import com.datechnologies.androidtest.databinding.ActivityChatBinding;
import com.datechnologies.androidtest.model.ChatLogMessageModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.SocketFactory;

/**
 * Screen that displays a list of chats from a chat log.
 */
public class ChatActivity extends CommonActivity {

    //==============================================================================================
    // Class Properties
    //==============================================================================================

    private ChatAdapter chatAdapter;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private NetworkRequest networkRequest;
    private ProgressBar loadingIndicator;
    private boolean hasInternetConnection;
    public static String TAG = "C-Manager";
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
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        setNetworkHelper();
        setupUI();

        // DONE: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation.

        // DONE: Retrieve the chat data from http://dev.rapptrlabs.com/Tests/scripts/chat_log.php
        // DONE: Parse this chat data from JSON into ChatLogMessageModel and display it.
    }

    private void setNetworkHelper(){
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = createNetworkCallback();
        networkRequest = new NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build();
    }

    private ConnectivityManager.NetworkCallback createNetworkCallback(){
        return new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.d(TAG, "onAvailable: " + network);
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if(networkCapabilities != null){
                    boolean hasInternetCapability = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET);
                    Log.d(TAG, "onAvailable: " + network + ", " + hasInternetCapability);
                    if (hasInternetCapability) {
                        // check if this network actually has internet
                        new Thread(() -> {
                            boolean hasInternet = DoesNetworkHaveInternet.execute(network.getSocketFactory());
                            if (hasInternet) {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    Log.d(TAG, "onAvailable: adding network: " + network);
                                    chatActivityViewModel.getChats();
                                });
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.d(TAG, "onLost: " + network);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                Log.d(TAG, "onAvailable: " + network);
                boolean hasInternetCapability = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET);
                Log.d(TAG, "onAvailable: " + network + ", " + hasInternetCapability);
                if (hasInternetCapability) {
                    // check if this network actually has internet
                    new Thread(() -> {
                        boolean hasInternet = DoesNetworkHaveInternet.execute(network.getSocketFactory());
                        if(hasInternet){
                            new Handler(Looper.getMainLooper()).post(() -> {
                                Log.d(TAG, "onAvailable: adding network: " + network);
                                hasInternetConnection = true;
                            });
                        }
                    }).start();
                }
            }
        };
    }

    public static class DoesNetworkHaveInternet {

        // Make sure to execute this on a background thread.
        static Boolean execute(SocketFactory socketFactory){
            try{
                Log.d(TAG, "PINGING google.");
                Socket socket = socketFactory.createSocket();
                if(socket != null){
                    socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                    socket.close();
                    Log.d(TAG, "PING success.");
                    return true;
                }
            }catch (IOException e){
                Log.e(TAG, "No internet connection. " + e);
            }
            return false;
        }
    }

    private void setupUI() {
        setupActionBar();


        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(ChatActivity.this, R.layout.activity_chat);
        activityChatBinding.setLifecycleOwner(this);
        chatActivityViewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        activityChatBinding.setChatActivityViewModel(chatActivityViewModel);
        // retrofit call when viewModel is initialized
        loadingIndicator = activityChatBinding.loadingIndicator;
        loadingIndicator.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = activityChatBinding.idRecyclerView;

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

