package com.example.zhaodong.dronephotography;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class UploadBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "UploadBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        //Check whether connected:
        boolean isConnected = (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = false;

        if (isConnected) {
            Log.i(TAG, "Network " + activeNetwork.getTypeName() + " connected");
            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(mConnectivityManager.getActiveNetwork());
            if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) && isWiFi){
                Toast.makeText(context.getApplicationContext(), "Internet Connected", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_LONG).show();
            }
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d(TAG, "There's no network connectivity");
            Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_LONG).show();
        }



    }
    }
