package com.datechnologies.androidtest.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

//used context wrapper to avoid context being null every time
public class NetworkConnectionManager extends ContextWrapper{

    public NetworkConnectionManager(Context context) {
        super(context);
    }

    private static final String TAG = "Connection-Manager";
    public Context ctx = getApplicationContext();
    private final NetworkConnectionLiveData connectionLiveData = new NetworkConnectionLiveData(ctx);


    // observe this in ui
    public boolean isNetworkAvailable = false;

    public void registerConnectionObserver(LifecycleOwner lifecycleOwner){
        connectionLiveData.observe(lifecycleOwner, isConnected ->
                { if(isConnected != null){
                    isNetworkAvailable = isConnected;
                    Log.d(TAG, "observing if connected to network: " + isNetworkAvailable);
                }}
        );
    }

    public void unregisterConnectionObserver(LifecycleOwner lifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner);
        Log.d(TAG, "not observing anymore");
    }


}

