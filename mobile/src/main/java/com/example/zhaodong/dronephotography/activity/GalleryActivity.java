package com.example.zhaodong.dronephotography.activity;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.zhaodong.dronephotography.R;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {
    GridView gridView;
    File[] files;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File dir = Environment.getExternalStoragePublicDirectory("ARSDKMedias");
        if(!dir.exists()){
            dir.mkdir();
        }
        files = dir.listFiles();
        if(files==null){
            setContentView(R.layout.nomedia_layout);
        }
        else{
            setContentView(R.layout.activity_gallery);
            gridView = findViewById(R.id.gview);


        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(files!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageAdapter imageAdapter = new ImageAdapter(GalleryActivity.this, files);
                    gridView.setAdapter(imageAdapter);
                }
            }).run();
        }
    }
}
