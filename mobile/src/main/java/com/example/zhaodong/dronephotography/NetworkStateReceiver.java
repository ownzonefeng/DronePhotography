package com.example.zhaodong.dronephotography;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "Network connectivity change");

        if (intent.getExtras() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//
//            if (ni != null && ni.isConnectedOrConnecting()) {
//                Log.i(TAG, "Network " + ni.getTypeName() + " connected");
//            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
//                Log.d(TAG, "There's no network connectivity");
//            }

            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            if(isConnected && isWiFi){
                if(hasInternetAccess()){


                }else{

                }

            }

        }

    }

    public static boolean hasInternetAccess() {

        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            //Connection is successful:
            if (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0){
                Log.d(TAG, "Network connection is sucessful!");
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error checking internet connection", e);
        }

        return false;
    }

}
