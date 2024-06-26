package com.example.cinerama.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeObserver extends BroadcastReceiver {
    private final NetworkChangeListener listener;
    public NetworkChangeObserver(NetworkChangeListener listener){
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        listener.onNetworkChange(isConnected);
    }
    ///Network listener
    public interface NetworkChangeListener {
        void onNetworkChange(boolean isConnected);
    }
}
