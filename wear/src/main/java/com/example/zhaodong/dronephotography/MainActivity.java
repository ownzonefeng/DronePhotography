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

import java.util.Map;

public class MainActivity extends WearableActivity implements OnMapReadyCallback{

    private double takePic = 0;
    private GoogleMap mMap;
    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";
    public static final String RECEIVED_LOCATION = "RECEIVE_LOCATION";
    private final String TAG = this.getClass().getSimpleName();

    private LocationBroadcastReceiver locationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView wear_map = findViewById(R.id.mapView);
        wear_map.getMapAsync(this);

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

    @Override
    protected void onResume() {
        super.onResume();

        // Get the location data back from the watch
        locationBroadcastReceiver = new LocationBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver, new
                IntentFilter(RECEIVED_LOCATION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver);
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
