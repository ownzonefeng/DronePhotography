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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TransferQueue;

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
                Toast.makeText(context.getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();

                TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
                ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);
                ImageView imageView0 = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon0);

                textView.setText("Synchronizing");
                textView.setTextColor(Color.GREEN);
                imageView.setImageResource(R.drawable.firebase_yes);
                imageView0.setImageResource(R.drawable.firebase_yes);

                fireBaseUpload(context);
            }else{
                Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_SHORT).show();

                TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
                ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);

                textView.setText("NonSync");
                textView.setTextColor(Color.BLACK);
                imageView.setImageResource(R.drawable.firebase_not);

            }
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d(TAG, "There's no network connectivity");
            Toast.makeText(context.getApplicationContext(), "Internet Not Connected", Toast.LENGTH_SHORT).show();

            TextView textView = (TextView)((DeviceListActivity)context).findViewById(R.id.FirebaseConnect);
            ImageView imageView = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon);
            ImageView imageView0 = (ImageView)((DeviceListActivity)context).findViewById(R.id.FirebaseIcon0);

            textView.setText("NonSync");
            textView.setTextColor(Color.BLACK);
            imageView.setImageResource(R.drawable.firebase_not);
            imageView0.setImageResource(R.drawable.firebase_not);

        }
    }
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
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
                                                Toast.makeText(context.getApplicationContext(), "fail to upload one file", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(context.getApplicationContext(), "succeed to upload one file", Toast.LENGTH_SHORT).show();
                                                File file1 = new File(Environment.getExternalStoragePublicDirectory("ARSDKMediasFirebase").getAbsolutePath()+File.separator+f.getName());
                                                try {
                                                    copyFileUsingStream(f, file1);
                                                } catch (IOException e1) {
                                                    e1.printStackTrace();
                                                }
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
