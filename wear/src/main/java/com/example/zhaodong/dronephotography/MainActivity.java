package com.example.zhaodong.dronephotography;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private double takePic = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enables Always-on
        setAmbientEnabled();


    }

    public void TakePic(View view) {
        takePic = 1;
        Intent intent = new Intent(MainActivity.this, WearService.class);
        intent.setAction(WearService.ACTION_SEND.SHOT_STATUS.name());
        intent.putExtra(WearService.SHOT_STATUS, takePic);
        startService(intent);
        takePic = 0;
    }
}
