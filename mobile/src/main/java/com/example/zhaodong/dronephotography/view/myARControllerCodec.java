package com.example.zhaodong.dronephotography.view;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMap;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_STREAM_CODEC_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class myARControllerCodec implements Serializable {
    ARControllerCodec codec;

    public myARControllerCodec(ARControllerCodec codec){
        this.codec = codec;
    }


    public ARControllerCodec getCodec(){
        return codec;
    }

    public DataMap toDataMap(){

        DataMap dataMap = new DataMap();
        ARCONTROLLER_STREAM_CODEC_TYPE_ENUM codec_type = codec.getType();
        int value = codec_type.getValue();
        String comment = null;
        if(codec_type.toString() != null){
            comment = codec_type.toString();
        }
        dataMap.putInt("value", value);
        dataMap.putString("comment", comment);
        return dataMap;
    }

}
