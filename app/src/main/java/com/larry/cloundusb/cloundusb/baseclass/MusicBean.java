package com.larry.cloundusb.cloundusb.baseclass;

/**
 * Created by Larry on 3/20/2016.
 * 存放父类音乐文件夹地址
 */
public class MusicBean  {


    int count;  //数量
    String parentPath;//父类文件路径


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }


}
