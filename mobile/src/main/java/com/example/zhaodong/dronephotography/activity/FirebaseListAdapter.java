package com.example.zhaodong.dronephotography.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FirebaseListAdapter extends BaseAdapter {
    private File[] ImageFiles;
    private Context mcontext;
    private List<String> nameList;
    public FirebaseListAdapter(Context context, File[] files){
        mcontext = context;
        ImageFiles = files;
        nameList = new ArrayList<>();
        for(File file:ImageFiles){
            nameList.add(file.getName());
        }
    }
    @Override
    public int getCount() {
        return nameList.size();
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
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            Context context;
            LayoutInflater inflater = LayoutInflater.from(mcontext);
            rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            // configure view holder
            DeviceListActivity.ViewHolder viewHolder = new DeviceListActivity.ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(android.R.id.text1);
            rowView.setTag(viewHolder);
        }

        // fill data
        DeviceListActivity.ViewHolder holder = (DeviceListActivity.ViewHolder) rowView.getTag();
        holder.text.setText(nameList.get(position));

        return rowView;
    }
}
