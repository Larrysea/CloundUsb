package com.larry.cloundusb.cloundusb.baseclass;

/**
 * Created by LARRYSEA on 2016/7/26.
 */
public class CopyFile {
    public String sourceFilePath;//原始文件地址
    public String targetFilePath;//目的地址

    public String getSourceFilePath() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    public String getTargetFilePath() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath = targetFilePath;
    }
}
