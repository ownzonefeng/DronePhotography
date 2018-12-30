package com.example.zhaodong.dronephotography;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaodong.dronephotography.activity.BebopActivity;
import com.example.zhaodong.dronephotography.activity.DeviceListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class UploadBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "UploadBroadcastReceiver";
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
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

                TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
                ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);

                textView.setText("Synchronizing");
                textView.setTextColor(Color.GREEN);
                imageView.setImageResource(R.drawable.firebase_yes);

                fireBaseUpload(context);
            }else{
                Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_LONG).show();

                TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
                ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);

                textView.setText("NonSync");
                textView.setTextColor(Color.BLACK);
                imageView.setImageResource(R.drawable.firebase_not);

            }
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d(TAG, "There's no network connectivity");
            Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_LONG).show();

            TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
            ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);

            textView.setText("NonSync");
            textView.setTextColor(Color.BLACK);
            imageView.setImageResource(R.drawable.firebase_not);

        }
    }

    public void fireBaseUpload(Context context) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mediaReference = mStorageRef.child("ARSDKMedias");
        File[] fileList = Environment.getExternalStoragePublicDirectory("ARSDKMedias").listFiles();
        if(fileList!=null){
            for(File f:fileList){
                singleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Uri file = Uri.fromFile(f);
                        StorageReference riversRef = mediaReference.child(file.getLastPathSegment());
                        riversRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        UploadTask uploadTask = riversRef.putFile(file);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(context.getApplicationContext(), "fail to upload one file", Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(context.getApplicationContext(), "succeed to upload one file", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });

                    }
                });


            }
        }

    }
    }
