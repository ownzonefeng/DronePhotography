package com.example.zhaodong.dronephotography.view;

import com.parrot.arsdk.arcontroller.ARFrame;

import java.io.Serializable;

public class myARFrame implements Serializable {
    ARFrame frame;

    public myARFrame(ARFrame frame){
        this.frame = frame;
    }


    public ARFrame extractFrame(){
        return frame;
    }
}
