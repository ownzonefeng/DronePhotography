package com.example.zhaodong.dronephotography;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_STREAM_CODEC_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;

import java.util.Map;

public class MainActivity extends WearableActivity implements OnMapReadyCallback{

    private double takePic = 0;
    private GoogleMap mMap;
    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";
    public static final String RECEIVED_LOCATION = "RECEIVE_LOCATION";
    public static final String RECEIVED_CODEC = "RECEIVED_CODEC";
    public static final String RECEIVED_FRAME = "RECEIVED_FRAME";

    private final String TAG = this.getClass().getSimpleName();

    private LocationBroadcastReceiver locationBroadcastReceiver;
    //private CodecBroadcastReceiver CodecBroadcastReceiver;
    private FrameBroadcastReceiver FrameBroadcastReceiver;


    private H264VideoView mVideoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (H264VideoView) findViewById(R.id.h264VideoView);
        ARCONTROLLER_STREAM_CODEC_TYPE_ENUM type = ARCONTROLLER_STREAM_CODEC_TYPE_ENUM.ARCONTROLLER_STREAM_CODEC_TYPE_H264;
        ARControllerCodec codec = new ARControllerCodec(type);
        mVideoView.configureDecoder(codec);

        //SupportMapFragment mapFragment = (SupportMapFragment)
        //mapFragment.getMapAsync(this);


        // Enables Always-on
        setAmbientEnabled();


    }


    private class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Update TextViews
            double longitude = intent.getDoubleExtra(LONGITUDE, -1);
            double latitude = intent.getDoubleExtra(LATITUDE, -1);


            // Update map
            LatLng currentLocation = new LatLng(latitude, longitude);
            Log.e(TAG, "Current location: " + currentLocation);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Here"));
        }
    }
/*
    private class CodecBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        int value = intent.getIntExtra(WearService.SEND_CODEC_VALUE, -1);
        String comment = intent.getStringExtra(WearService.SEND_CODEC_COMMENT);
        ARCONTROLLER_STREAM_CODEC_TYPE_ENUM type = ARCONTROLLER_STREAM_CODEC_TYPE_ENUM.ARCONTROLLER_STREAM_CODEC_TYPE_H264;
        ARControllerCodec codec = new ARControllerCodec(type);
        mVideoView.configureDecoder(codec);
        }
    }*/

    private  class FrameBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int  size = intent.getIntExtra(WearService.SEND_FRAME_SIZE, 1);
            byte[] frame_byte = intent.getByteArrayExtra(WearService.SEND_FRAME_BYTE);
            mVideoView.displayFrame(frame_byte, size);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the location data back from the watch
        locationBroadcastReceiver = new LocationBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver, new IntentFilter(RECEIVED_LOCATION));
        //LocalBroadcastManager.getInstance(this).registerReceiver(CodecBroadcastReceiver, new IntentFilter(RECEIVED_CODEC));
        FrameBroadcastReceiver = new FrameBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(FrameBroadcastReceiver, new IntentFilter(RECEIVED_FRAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(CodecBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(FrameBroadcastReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Lausanne
        double latitude = 46.5197;
        double longitude = 6.6323;
        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(latitude, longitude);
        Log.e(TAG, "Current location: " + currentLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Here"));
    }

    @Override
// Activity
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_STEM_1) {
                // Do stuff
                takePic = 1;
                Intent intent = new Intent(MainActivity.this, WearService.class);
                intent.setAction(WearService.ACTION_SEND.SHOT_STATUS.name());
                intent.putExtra(WearService.SHOT_STATUS, takePic);
                startService(intent);
                Toast.makeText(this, R.string.Photo_taken, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
