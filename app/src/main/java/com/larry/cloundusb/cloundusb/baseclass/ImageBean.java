package com.larry.cloundusb.cloundusb.baseclass;

/**
 * Created by Larry on 3/16/2016.
 */
public class ImageBean {

    String firstImagePath;//第一张图片路径
    String parentPath;//父类文件路径
    int count;//图片数量

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
