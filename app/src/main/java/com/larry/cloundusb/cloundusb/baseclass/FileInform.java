package com.larry.cloundusb.cloundusb.baseclass;

import java.io.Serializable;

/**
 * Created by Larry on 3/20/2016.
 * <p/>
 * <p/>
 * <p/>
 * 基础文件信息类
 */
public abstract class FileInform implements Serializable {
    String fileName;  //文件名
    String type;       //文件类型
    String absPath;    //文件绝对路径
    String fileSize;   //文件大小
    long modifydateDate; //文件修改日期
    boolean shareAble;   //文件是否可分享   false 默认为不可分享


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public long getModifydateDate() {
        return modifydateDate;
    }

    public void setModifydateDate(long modifydateDate) {
        this.modifydateDate = modifydateDate;
    }

    public boolean isShareAble() {
        return shareAble;
    }

    public void setShareAble(boolean shareAble) {
        this.shareAble = shareAble;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public FileInform(String name, String type, String size, String path, long modifydateDate) {


        if (name.equals(null) || type.equals(null) || size.equals(null) || modifydateDate == 0) {
            throw new NullPointerException("Fileinform parm lose  in  fileinform java");
        } else {
            this.fileName = name;
            this.type = type;
            this.fileSize = size;
            this.absPath = path;
            this.modifydateDate = modifydateDate;
            this.setShareAble(false);


        }


    }

    public FileInform() {

    }


}
