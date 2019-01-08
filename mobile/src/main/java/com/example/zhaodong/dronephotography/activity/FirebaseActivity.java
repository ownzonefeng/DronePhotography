package com.example.zhaodong.dronephotography.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhaodong.dronephotography.BuildConfig;
import com.example.zhaodong.dronephotography.R;

import java.io.File;
import java.util.Arrays;

public class FirebaseActivity extends AppCompatActivity {

    ListView listView;
    File[] files;
    File dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        dir = Environment.getExternalStoragePublicDirectory("ARSDKMediasFirebase");
        if(!dir.exists()){
            dir.mkdir();
        }
        listView = findViewById(R.id.lviewFirebase);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = FileProvider.getUriForFile(FirebaseActivity.this, BuildConfig.APPLICATION_ID + ".provider",files[i]);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        File[] new_files = dir.listFiles();
        if(!Arrays.equals(new_files,files)){
            files = new_files;
            FirebaseListAdapter firebaseListAdapter = new FirebaseListAdapter(FirebaseActivity.this, files);
            listView.setAdapter(firebaseListAdapter);
        }

        if(files.length!=0){
            TextView tv = findViewById(R.id.noMediaFirebase);
            tv.setText("");
        }
        else{
            TextView tv = findViewById(R.id.noMediaFirebase);
            tv.setText("No media");
        }
    }
}
