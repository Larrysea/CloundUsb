package com.larry.cloundusb.cloundusb.baseclass;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Larry on 3/22/2016.
 */
public class ApkInform extends FileInform implements Serializable {
    String typeNumber;
    int versionNumber;
    String packageName;
    Drawable thumbPath;
    String specialInfor; //每个文件的特殊信息

    public String getSpecialInfor() {
        return specialInfor;
    }

    public void setSpecialInfor(String specialInfor) {
        this.specialInfor = specialInfor;
    }

    @Override
    public String getType() {
        return type;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(String typeNumber) {
        this.typeNumber = typeNumber;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    public int getVersion() {
        return versionNumber;
    }

    public void setVersion(int version) {
        this.versionNumber = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(Drawable thumbPath) {
        this.thumbPath = thumbPath;
    }

  /*  public ApkInform(String name, String type, String size, String path, long modifydate, String typeNumber,
                     String versionNumber, String packagename, Drawable thumbPath)
    {
        super(name,type,size,path,modifydate);
        setPackageName(packagename);
        setTypeNumber(typeNumber);
        setVersionNumber(versionNumber);
        setPackageName(packagename);
        setThumbPath(thumbPath);

    }*/


}
