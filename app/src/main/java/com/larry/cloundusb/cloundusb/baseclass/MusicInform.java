package com.larry.cloundusb.cloundusb.baseclass;

import java.io.Serializable;

/**
 * Created by Larry on 3/20/2016.
 * 音乐类的详细信息
 *
 *
 *
 */
public class MusicInform extends FileInform implements Serializable{

    String author;//歌曲作者
    String albumName;//专辑名
    String  totalTime;   //播放时长


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String  getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
