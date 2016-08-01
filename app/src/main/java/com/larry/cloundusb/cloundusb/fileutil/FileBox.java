package com.larry.cloundusb.cloundusb.fileutil;


import android.graphics.Bitmap;
import android.os.Handler;

import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 4/21/2016.
 * <p/>
 * 获得选择的待发送文件类
 */
public class FileBox {

    static FileBox instance = null;//类实例
    List<SendFileInform> sendList = null;    //发送文件栈
    List<SendFileInform> receiveList = null;  //接受文件栈
    List<Bitmap> sendPortraitList;                      //保存文件头像的链表
    List<SendFileInform> usbImageList;            //usb文件中的list
    List<SendFileInform> usbMusicList;               //usb中的音乐list
    List<SendFileInform> usbVideoList;              //usb中的视频list
    List<SendFileInform> usbDocumentList;             //usb中的文档list
    List<SendFileInform> usbotherList;               //usb中 的其他文件list

    SendFileInform msendFileInform;

    public FileBox() {

    }

    /*
    *
    * 存放接受文件信息
    * */

    static public FileBox getInstance() {

        if (instance == null) {
            instance = new FileBox();
            if (instance.sendList == null || instance.receiveList == null) {
                instance.sendList = new ArrayList<SendFileInform>();
                instance.receiveList = new ArrayList<SendFileInform>();
            }


        }


        return instance;
    }

    /*
    * 清空保存instance保存的静态实例
    *
    *
    * */
    static public void cleanInsatance() {
        if (instance != null) {
            instance.sendList = null;
            instance.receiveList = null;
            instance = null;
        }


    }

    /*
    * 存放单个待发送文件
    *
    * */
    public void storageSendFileItem(SendFileInform fileitem) {

        boolean flag = false;
        if (sendList != null)
            if (sendList.size() == 0) {
                sendList.add(fileitem);

            } else {
                for (int position = 0; position < sendList.size(); position++) {
                    msendFileInform = sendList.get(position);
                    if (msendFileInform.getName() == fileitem.getName()) {
                        flag = true;
                    }
                }
                if (flag == false) {
                    sendList.add(fileitem);

                }

            }


    }


    /*
    *
    * 取消文件发送
    *
    * */

    public void storageReceiveFileItem(SendFileInform fileitem) {
        if (receiveList != null) {
            receiveList.add(fileitem);
        }
    }





    /*
    *取消要发送的文件
    *
    * */


    public void cancelSendFile(String name) {
        if (sendList != null && sendList.size() != 0)
            for (int position = 0; position < sendList.size(); position++) {
                msendFileInform = sendList.get(position);
                if (name == msendFileInform.getName()) {
                    sendList.remove(position);
                }
            }
    }






    /*
    *
    * 将文件选择的hashmap转换为Linkedlist <T>
    *  返回
    *
    *
    * */

    public void cancelReceiveFile(String name) {

        if (receiveList != null && receiveList.size() != 0)
            receiveList.remove(name);
    }


    public List<SendFileInform> getList(HashMap<String, SendFileInform> hashMap) {
        Iterator<Map.Entry<String, SendFileInform>> iterator = hashMap.entrySet().iterator();
        List<SendFileInform> result = new ArrayList<SendFileInform>();
        if (hashMap.size() == 0) {
            return null;
        }
        while (iterator.hasNext()) {
            Map.Entry<String, SendFileInform> map = iterator.next();
            SendFileInform sendFileInform = map.getValue();

            result.add(sendFileInform);

        }
        return result;

    }

    /*
    * 获取文件的长度
    *
    * */
    public int getSendListSize() {

        return sendList.size();
    }

    /*
    *
    * 获取接受文件list的长度
    * */
    public int getReceiveListSize() {
        if (receiveList != null && receiveList.size() > 0) {
            return receiveList.size();
        } else {
            return -1;
        }

    }


    /*
    * 获取文件头像链表
    *
    * */
    public List<Bitmap> getSendPortraitList() {
        if (sendPortraitList != null) {
            sendPortraitList.clear();
            for (SendFileInform parm : FileBox.getInstance().getSendList()) {
                sendPortraitList.add(parm.getPortrait());
            }

        } else {
            sendPortraitList = new ArrayList<Bitmap>();
            for (SendFileInform parm : FileBox.getInstance().getSendList()) {
                sendPortraitList.add(parm.getPortrait());
            }
        }
        return sendPortraitList;

    }


    /*
    * 初始化usb文件清单
    *
    * */
    public void initUsbFileList() {
        if (instance.usbImageList == null || instance.usbDocumentList == null || instance.usbotherList == null || instance.usbVideoList == null || instance.usbMusicList == null) {
            instance.usbImageList = new ArrayList<SendFileInform>();
            instance.usbDocumentList = new ArrayList<SendFileInform>();
            instance.usbotherList = new ArrayList<SendFileInform>();
            instance.usbVideoList = new ArrayList<SendFileInform>();
            instance.usbMusicList = new ArrayList<SendFileInform>();
        }


    }

    /*
    * 添加图片文件
    *
    * */
    public void addUsbImageItem(SendFileInform sendFileInform) {
        instance.usbImageList.add(sendFileInform);

    }

    /*
    *
    * 添加音乐类型文件
    * */

    public void addUsbMusicItem(SendFileInform sendFileInform) {
        instance.usbMusicList.add(sendFileInform);
    }



    /*
    *
    * 添加视频类型文件
    *
    * */

    public void addUsbVideoItem(SendFileInform sendFileInform) {
        instance.usbVideoList.add(sendFileInform);
    }

    /*添加文档类型文件*/
    public void addUsbDocumentItem(SendFileInform sendFileInform) {

        instance.usbDocumentList.add(sendFileInform);

    }


    /*
    * 添加其他类型文件
    *
    * */
    public void addUsbOtherItem(SendFileInform sendFileInform) {
        instance.usbotherList.add(sendFileInform);

    }

    public List<SendFileInform> getUsbImageList() {
        return usbImageList;
    }

    public void setUsbImageList(List<SendFileInform> usbImageList) {
        this.usbImageList = usbImageList;
    }

    public List<SendFileInform> getUsbMusicList() {
        return usbMusicList;
    }

    public void setUsbMusicList(List<SendFileInform> usbMusicList) {
        this.usbMusicList = usbMusicList;
    }

    public List<SendFileInform> getUsbVideoList() {
        return usbVideoList;
    }

    public void setUsbVideoList(List<SendFileInform> usbVideoList) {
        this.usbVideoList = usbVideoList;
    }

    public List<SendFileInform> getUsbDocumentList() {
        return usbDocumentList;
    }

    public void setUsbDocumentList(List<SendFileInform> usbDocumentList) {
        this.usbDocumentList = usbDocumentList;
    }

    public List<SendFileInform> getUsbotherList() {
        return usbotherList;
    }

    public void setUsbotherList(List<SendFileInform> usbotherList) {
        this.usbotherList = usbotherList;
    }


    static public List<SendFileInform> getSendList() {
        if (instance.sendList != null && instance.sendList.size() > 0) {
            return instance.sendList;
        } else {
            return null;
        }

    }


    /*
    *
    * 获取接收方进度条的内容信息
    *
    * */
    static public List<SendFileInform> getReceiveList() {
        if (instance.receiveList != null && instance.receiveList.size() != 0)
            return instance.receiveList;
        else return null;

    }

    /*
    * 删除已经保存的usb数据
    *
    * */
    public void cleanAllUsbItem() {
        instance.usbImageList = new ArrayList<SendFileInform>();
        instance.usbDocumentList = new ArrayList<SendFileInform>();
        instance.usbotherList = new ArrayList<SendFileInform>();
        instance.usbVideoList = new ArrayList<SendFileInform>();
        instance.usbMusicList = new ArrayList<SendFileInform>();

    }



}
