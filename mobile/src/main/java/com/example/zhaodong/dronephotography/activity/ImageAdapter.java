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

public class ImageAdapter extends BaseAdapter {
    private File[] ImageFiles;
    private Context mcontext;
    private List<Bitmap> bitmapList;
    public ImageAdapter(Context context, File [] files){
        mcontext = context;
        ImageFiles = files;
        bitmapList = new ArrayList<>();
        for(File f: ImageFiles){
            String fileName = f.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            switch (suffix){
                case "jpg":
                    bitmapList.add(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 512, 384));
                    break;
                case "mp4":
                    bitmapList.add(ThumbnailUtils.createVideoThumbnail(f.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND));
                    break;
            }
        }
    }
    @Override
    public int getCount() {
        return ImageFiles.length;
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
        ImageView imageView;
        if(convertView==null){
            imageView = new ImageView(mcontext);
            imageView.setImageBitmap(bitmapList.get(position));
        }
        else{
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
}
