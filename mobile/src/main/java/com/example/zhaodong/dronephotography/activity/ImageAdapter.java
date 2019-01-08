package com.example.zhaodong.dronephotography.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageAdapter extends BaseAdapter {
    private File[] ImageFiles;
    private Context mcontext;
    private Bitmap[] bitmapList;
    ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
    public ImageAdapter(Context context, File [] files){
        mcontext = context;
        ImageFiles = files;
        bitmapList = new Bitmap[ImageFiles.length];
        for(int i=0;i<ImageFiles.length;i++){
            int finalI = i;
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    File f = ImageFiles[finalI];
                    String fileName = f.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    switch (suffix){
                        case "jpg":
                            bitmapList[finalI] = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 512, 288);
                            break;
                        case "mp4":
                            bitmapList[finalI] = ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                            break;
                    }
                }
            });
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getCount() {
        return bitmapList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mcontext);
        imageView.setImageBitmap(bitmapList[position]);
        return imageView;
    }
}
