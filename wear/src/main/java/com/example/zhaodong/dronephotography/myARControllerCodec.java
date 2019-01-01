package com.example.zhaodong.dronephotography;

import com.parrot.arsdk.arcontroller.ARControllerCodec;

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
