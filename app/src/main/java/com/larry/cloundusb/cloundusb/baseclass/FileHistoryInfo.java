package com.larry.cloundusb.cloundusb.baseclass;

import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;

/**
 * Created by rxw on 2016/6/23.
 * 历史文件信息
 */
public class FileHistoryInfo {

    private int TFUserPicId;   //发送者用户头像id
    private String TFUserName;//发送者的名称
    private String TFFileName;//文件名
    private String TFFilePath;//文件路径
    private String TFFileSize;//发送地址
    private String TFTime;  //发送时间
    private int TFtype;     //发送方0 接收方为1
    private int fileid;        //主键
    private int TFPlatform;   //平台为 1电脑 0是手机

    public FileHistoryInfo() {
        ;
    }

    public void SetItemValue(int TFUserPicId,
                             String TFUserName,
                             String TFFilePath,
                             String TFFileName,
                             String TFFileSize,
                             String TFTime,
                             int TFtype,
                             int TFPlatform) {
        this.TFUserPicId = TFUserPicId;
        this.TFUserName = TFUserName;
        this.TFFileName = TFFileName;
        this.TFFilePath = TFFilePath;
        this.TFFileSize = TFFileSize;
        this.TFTime = TFTime;
        this.TFtype = TFtype;
        this.TFPlatform = TFPlatform;
    }

    public void SetItemValue(int TFUserPicId,
                             String TFUserName,
                             String TFFilePath,
                             String TFFileName,
                             long TFFileSize,
                             String TFTime,
                             int TFtype,
                             int TFPlatform) {
        this.TFUserPicId = TFUserPicId;
        this.TFUserName = TFUserName;
        this.TFFileName = TFFileName;
        this.TFFilePath = TFFilePath;
        this.TFFileSize = FileSizeUtil.convertFileSize(TFFileSize);
        this.TFTime = TFTime;
        this.TFtype = TFtype;
        this.TFPlatform = TFPlatform;
    }

    public int getTFUserPicId() {
        return this.TFUserPicId;
    }

    public String getTFUserName() {
        return this.TFUserName;
    }

    public String getTFFilePath() {
        return this.TFFilePath;
    }

    public String getTFFileName() {
        return this.TFFileName;
    }

    public String getTFFileSize() {
        return this.TFFileSize;
    }

    public String getTFTime() {
        return this.TFTime;
    }

    public int getTFtype() {
        return this.TFtype;
    }

    public void setFileid(int id) {
        this.fileid = id;
    }

    public int getFileid() {
        return this.fileid;
    }

    public int getTFPlatform() {
        return this.TFPlatform;
    }
}
