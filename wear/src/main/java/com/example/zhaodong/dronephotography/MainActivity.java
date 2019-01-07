package com.example.zhaodong.dronephotography;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class MainActivity extends WearableActivity{

    private double takePic = 0;
    public static final String RECEIVED_IMAGE = "RECEIVED_IMAGE";

    private final String TAG = this.getClass().getSimpleName();
    private ImageBroadcastReceiver ImageBroadcastReceiver;
    private ConstraintLayout myLayout;
    Bitmap bmpProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView guide = findViewById(R.id.ImagePreview);
        guide.setText("pressing here --->>");

        myLayout = findViewById(R.id.boxInsetLayout);

        // Enables Always-on
        setAmbientEnabled();


    }

    private class ImageBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ImageView imageView = findViewById(R.id.PreviewWindow);
            TextView textView = findViewById(R.id.ImagePreview);
            TextView clear_view = findViewById(R.id.hint_text);

            clear_view.setText("");

            byte[] byteArray = intent.getByteArrayExtra(RECEIVED_IMAGE);
            bmpProfile = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bmpProfile);

            textView.setText("Image Preview");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageBroadcastReceiver = new ImageBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(ImageBroadcastReceiver, new IntentFilter(RECEIVED_IMAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(ImageBroadcastReceiver);
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

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        updateDisplay();
    }

    private void updateDisplay() {

        if(isAmbient()){
            setContentView(R.layout.ambient_main);
        }
        else {
            setContentView(R.layout.activity_main);
            if(bmpProfile != null)
            {
                ImageView imageView = findViewById(R.id.PreviewWindow);
                TextView textView = findViewById(R.id.ImagePreview);
                TextView clear_view = findViewById(R.id.hint_text);

                clear_view.setText("");
                imageView.setImageBitmap(bmpProfile);

                textView.setText("Image Preview");
            }
        }
    }
}
