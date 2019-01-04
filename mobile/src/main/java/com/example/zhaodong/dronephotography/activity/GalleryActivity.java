package com.example.zhaodong.dronephotography.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaodong.dronephotography.BuildConfig;
import com.example.zhaodong.dronephotography.R;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    GridView gridView;
    File[] files;
    File dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        dir = Environment.getExternalStoragePublicDirectory("ARSDKMedias");
        if(!dir.exists()){
            dir.mkdir();
        }
        gridView = findViewById(R.id.gview);
    }
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        files = dir.listFiles();
        if(files.length!=0){
            ImageAdapter imageAdapter = new ImageAdapter(GalleryActivity.this, files);
            gridView.setAdapter(imageAdapter);
            TextView tv = findViewById(R.id.noMedia);
            tv.setText("");
        }
        else{
            ImageAdapter imageAdapter = new ImageAdapter(GalleryActivity.this, files);
            gridView.setAdapter(imageAdapter);
            TextView tv = findViewById(R.id.noMedia);
            tv.setText("No media");
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = FileProvider.getUriForFile(GalleryActivity.this, BuildConfig.APPLICATION_ID + ".provider",files[i]);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
}
