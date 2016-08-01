package com.larry.cloundusb.cloundusb.baseclass;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Larry on 3/20/2016.
 * 用于保存视频文件相关的信息
 *
 */
public class VideoInform extends FileInform implements Serializable {
    String totaltime;


    Bitmap thumb;


    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }





}
