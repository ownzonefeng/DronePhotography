package com.example.zhaodong.dronephotography;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.parrot.arsdk.arcontroller.ARFrame;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class myARFrame extends ARFrame implements Serializable {
    protected ARFrame frame;

    public myARFrame(ARFrame frame){
        this.frame = frame;
    }


    public ARFrame getFrame(){
        return frame;
    }

    public DataMap toDataMap(){

        DataMap dataMap = new DataMap();
        Asset frame_asset = this.createAssetFromFrame();
        dataMap.putAsset("frame", frame_asset);
        return dataMap;
    }

    public Asset createAssetFromFrame() {

        if (frame != null) {
            byte[] frame_byte = frame.getByteData();
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream(frame_byte.length);
            byteStream.write(frame_byte, 0, frame_byte.length);
            return Asset.createFromBytes(byteStream.toByteArray());
        }
        return null;
    }
}
