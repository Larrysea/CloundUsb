package com.larry.cloundusb.cloundusb.baseclass;

/**
 * Created by LARRYSEA on 2016/7/21.
 */
public class PictureInfo {
    String path;         // 图片路径
    String name;         //图片名称
    long fileSize;       //图片大小
    String place;       //图片的拍摄地点

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
