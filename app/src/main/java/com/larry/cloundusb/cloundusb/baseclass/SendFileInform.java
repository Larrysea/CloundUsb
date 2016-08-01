package com.larry.cloundusb.cloundusb.baseclass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Larry on 5/2/2016.
 * <p/>
 * 发送文件的大概信息
 */
public class SendFileInform implements Parcelable{

    int file_index;
    long now_recv_length;            //当前接收文件长度
    long now_send_length;           //当前发送的文件长度
    String name;           //文件名
    Bitmap portrait;     //头像
    String path;          //文件路径
    double time;           //预计时间长度
    int position;          //记录位置方便删除
    long filesize;          //文件的大小
    boolean isFile;        //文件或者路径标志  true 代表是文件  false代表路径
    long ParentFileSize;    //父类文件夹大小

    String fileUnit;//文件大小单位可读类型   例如 8mb
    String type;//文件类型

    String specialInfo;

    public int getFile_index() {
        return file_index;
    }

    public void setFile_index(int file_index) {
        this.file_index = file_index;
    }

    public String getSpecialInfo() {
        return specialInfo;
    }

    public void setSpecialInfo(String specialInfo) {
        this.specialInfo = specialInfo;
    }

    public String getFileUnit() {
        return fileUnit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFileUnit(String fileUnit) {
        this.fileUnit = fileUnit;
    }


    public long getParentFileSize() {
        return ParentFileSize;
    }

    public void setParentFileSize(long parentFileSize) {
        ParentFileSize = parentFileSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPortrait() {
        return portrait;
    }

    public void setPortrait(Bitmap portrait) {
        this.portrait = portrait;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public void setNow_recv_length(long recv_length){this.now_recv_length=recv_length;}

    public void setNow_send_length(long send_length){this.now_send_length=send_length;}

    public long getNow_recv_length(){return this.now_recv_length;}

    public long getNow_send_length(){return this.now_send_length;}
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        portrait.writeToParcel(dest, 0);
        dest.writeString(path);
        dest.writeDouble(time);
        dest.writeInt(position);
        dest.writeLong(filesize);
        dest.writeByte((byte)(isFile?1:0));


    }


    public  final static  Creator CREATOR =new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            SendFileInform sendFileInform=new SendFileInform();
            sendFileInform.setName(source.readString());
            sendFileInform.setPortrait(Bitmap.CREATOR.createFromParcel(source));
            sendFileInform.setPath(source.readString());
            sendFileInform.setTime(source.readDouble());
            sendFileInform.setPosition(source.readInt());
            sendFileInform.setFilesize(source.readLong());
            sendFileInform.setFile(source.readByte()==1?true:false);

            return sendFileInform;
        }

        @Override
        public Object[] newArray(int size) {
            return new SendFileInform[size];
        }
    };

}
