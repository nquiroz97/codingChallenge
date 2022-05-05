package com.datechnologies.androidtest.util;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;

import javax.net.SocketFactory;

public class NetworkConnectionLiveData extends LiveData<Boolean> {

    public NetworkConnectionLiveData(Context context) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    }

    private static final String TAG = "Network-Live";
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;
    private HashSet<Network> validNetworks = new HashSet<>();

    private void checkValidNetworks() {
        postValue(validNetworks.size() > 0);
    }

    @Override
    protected void onActive() {
        super.onActive();
        networkCallback = createNetworkCallback();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        connectivityManager.unregisterNetworkCallback(networkCallback);
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
                                    validNetworks.add(network);
                                    checkValidNetworks();
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
                validNetworks.remove(network);
                checkValidNetworks();
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
                                validNetworks.add(network);
                                checkValidNetworks();
                            });
                        }
                    }).start();
                }
            }
        };
    }

    public static class DoesNetworkHaveInternet {

        // execute this on a background/async thread.
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
}
