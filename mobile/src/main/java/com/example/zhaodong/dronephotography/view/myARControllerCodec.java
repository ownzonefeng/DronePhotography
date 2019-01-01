package com.example.zhaodong.dronephotography.view;

import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;

import java.io.Serializable;

public class myARControllerCodec implements Serializable {
    ARControllerCodec codec;

    public myARControllerCodec(ARControllerCodec codec){
        this.codec = codec;
    }


    public ARControllerCodec extractCodec(){
        return codec;
    }
}
