package com.larry.cloundusb.cloundusb.Interneutil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.PictureInfo;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LARRYSEA on 2016/7/21.
 * 同步照片工具线程
 * <p/>
 * <p/>
 * 协议流程   开始发送所有 图片信息
 * <p/>
 * 然后开始发送单个图片信息
 * <p/>
 * <p/>
 * 然后提示对方发送所有图片信息
 */
public class SyncPitctureUtil implements Runnable {

    final static String listOk = "list_ok";              //首次连接返回值
    final static String pictureOk = "picture_ok";        //发送单个文件信息成功接收的标志
    final static String sendToMe = "send_to_me_next";    //提示对方开始发送图片信息
    final static String TAG = "syncpictureutil";         //tag标记
    public static long pictureSendLength;                //图片已发送的长度
    public static long pictureReceiveLength;             //图片已接收长度
    public static long pictureTotalLength;               //文件总的长度
    public static int percent = 0;                         //显示进度百分比
    static int PORT = 9889;                              //端口
    boolean ISLIFE = false;                              //线程是否已经存在
    List<SendContactInfo> sendContactInfoList;           //保存共享池内所有与的联系人
    InputStream minputStream;                            //接收的输入流
    OutputStream moutputStream;                          //输出流
    byte mbuffer[];                                      //缓冲字符数组
    int protocolLength;                                  //协议内容长度
    String protocol;                                     //协议内容
    List<String> protocolList;                           //协议处理后的链表
    List<PictureInfo> sendPictureInfoList;               //保存图片信息的链表
    List<PictureInfo> receiveInfoList;                   //接收图片信息的链表
    File tempFile;                                       //临时性文件
    FileOutputStream mfileOutputStream;                  //文件输出流
    FileInputStream mfileInputStream;                    //文件输入流
    int fileSendPosition;                                //文件发送位置
    int fileReceivePosition;                             //文件接收位置
    PictureInfo mpictureInfo;                            //图片信息
    boolean receiveFlag = false;                         //文件正式接收标记
    updateProgressInterface mupdateProgressInterface;    //更新界面接口
    int receiverPostion = 0;                             //接收者的位置
    String mrootPath;                                    //存放图片的根路径
    PictureInfo tempPictureInfo;                         //临时图片信息
    ServerSocket mserverSocket;                          //服务端socket
    Socket msocket;                                      //接收文件socket
    int type;                                            //类型是服务端还是客户端  如果为1 则表示为客户端  如果为2 则是则表示服务端
    Bundle mbundle;                                      //保存返回数据使用
    Handler mhandler;                                    //保存syncprogressactivity的handler
    int fileTotalLength;                                 //文件总长度


    public SyncPitctureUtil(String rootPath, int type, Handler handler) {
        this.mrootPath = rootPath;
        this.type = type;
        this.mhandler = handler;
        transToPictureList(MultiMediaUtil.recentPictureList);
    }

    /*
    *
    * 建立连接
    *
    * */

    @Override
    public void run() {

        while (sendContactInfoList == null) {
            initSendContactInfor();
            if (sendContactInfoList != null) {
                try {
                    connect(InetAddress.getByName("192.168.43.1"));
                } catch (UnknownHostException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }


        }
    }

    public void connect(InetAddress inetAddress) {
        try {

            if (type == 1) {
                msocket = new Socket(inetAddress, PORT);
                minputStream = msocket.getInputStream();
                moutputStream = msocket.getOutputStream();
                receiver();
            } else if (type == 2) {
                waitConnect();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }


    }

    /*
    *
    * 等待连接
    *
    * */
    public void waitConnect() {
        try {
            mserverSocket = new ServerSocket(PORT);
            msocket = mserverSocket.accept();
            Log.e(TAG, "接收成功");
            moutputStream = msocket.getOutputStream();
            minputStream = msocket.getInputStream();
            moutputStream.write("hello_send_to_me_list".getBytes());
            moutputStream.flush();
            receiver();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }


    }

    /*
    *
    * 协议接收者
    *
    * */
    public void receiver() {
        mbuffer = new byte[1460 * 8];
        try {
            while (true) {

                if (!receiveFlag)      //分析协议
                {
                    mbundle = StringUtil.inputStreamToByteArray(minputStream);
                    protocol = new String(mbundle.getByteArray("streamvalue"), "UTF-8");
                    if (protocol.substring(0, 7).equals("list_ok")) {
                        if (fileSendPosition < sendPictureInfoList.size()) {
                            sendPictiureInfo(sendPictureInfoList.get(fileSendPosition), moutputStream);
                        }
                        Log.e(TAG, "picture_list_ok");
                    } else if (protocol.substring(0, 10).equals("picture_ok")) {
                        sendPicture(moutputStream, sendPictureInfoList.get(fileSendPosition).getPath(), mhandler);
                        if (fileSendPosition < sendPictureInfoList.size()) {
                            sendPictiureInfo(sendPictureInfoList.get(fileSendPosition), moutputStream);  //发送下一个图片信息
                        }
                        Log.e(TAG, "picture_ok");
                    } else if (protocol.substring(0, 12).equals("picture_list")) {
                        bufferToPictureList(protocol, mrootPath);
                        moutputStream.write("list_ok".getBytes());
                        moutputStream.flush();
                        Log.e(TAG, "picture_list");

                    } else if (protocol.substring(0, 12).equals("picture_info")) {
                        receiveInfoList.get(receiverPostion).setFileSize(Long.parseLong(StringUtil.Muitlsplit(protocol,"|").get(2)));
                        moutputStream.write(pictureOk.getBytes());
                        moutputStream.flush();
                        receiveFlag = true;
                        Log.e(TAG, "picture_info");


                    } else if (protocol.substring(0, 15).equals("send_to_me_next")) {
                        sendPictureInfoList();

                        Log.e(TAG, "send_to_me");
                    } else if (protocol.substring(0, 21).equals("hello_send_to_me_list")) {
                        Log.e(TAG, "已接收到消息链表");
                        sendPictureInfoList();

                    } else {
                        Log.e(TAG, protocol);
                    }

                } else {

                    Log.e(TAG, "正式接收文件" + receiveFlag);
                    mpictureInfo = receiveInfoList.get(fileReceivePosition);
                    receivePicture(minputStream, mpictureInfo, tempFile);
                    if (fileReceivePosition == receiveInfoList.size())    //当自己讲图片发送完毕时开始提示对方发送图片
                    {
                        moutputStream.write(sendToMe.getBytes());
                        moutputStream.flush();
                    }

                }

            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }


    }

    /*
    *
    *parm1 协议内容，
    *
    * parm 2 图片存放地址   sdcard\  示例
    *
    * */
    public void bufferToPictureList(String protocol, String rootPicturePath) {
        protocolList = StringUtil.Muitlsplit(protocol, "|");
        receiveInfoList = new ArrayList<PictureInfo>();
        for (int position = 1; position < protocolList.size(); position++) {
            mpictureInfo = new PictureInfo();
            mpictureInfo.setName(protocolList.get(position));
            mpictureInfo.setPath(rootPicturePath + protocolList.get(position));
            receiveInfoList.add(mpictureInfo);  //将图片信息保存到链表
        }

    }

    /*
    *
    * 发送单个图片文件信息
    *
    * */
    public void sendPictiureInfo(PictureInfo pictureInfo, OutputStream outputStream) {


        try {
            String protocol = "picture_info|" + pictureInfo.getName() + "|" + pictureInfo.getFileSize();
            outputStream.write(protocol.getBytes());
            outputStream.flush();
            fileSendPosition++;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }

    }

    /*
    *  发送总的图片文件信息
    *
    * */
    public void sendPictureInfoList() {
        String pictureInfoList = "picture_list";

        try {
            for (int sendPosition = 0; sendPosition < sendPictureInfoList.size(); sendPosition++) {
                pictureInfoList += "|" + FileUtil.getFileNameFromPath(sendPictureInfoList.get(sendPosition).getPath());
            }
            moutputStream.write(pictureInfoList.getBytes());
            moutputStream.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }


    }

    /*
   *
   * 正式接收图片文件
   *
   * */
    public void receivePicture(InputStream inputStream, PictureInfo pictureInfo, File tempFile) {
        try {
            tempFile = new File(pictureInfo.getPath());
            int receiveLength = 0;                             //接收长度
            File path = new File("/storage/sdcard0/xiechuan/测试/");
            if (!tempFile.exists()) {
                path.mkdirs();
                tempFile = new File(path, pictureInfo.getName());
                tempFile.createNewFile();

            }
            mfileOutputStream = new FileOutputStream(tempFile);
            while ((protocolLength = inputStream.read(mbuffer)) != -1) {
                receiveLength += protocolLength;
                mfileOutputStream.write(mbuffer, 0, protocolLength);
                if (receiveLength == pictureInfo.getFileSize()) {
                    Log.e(TAG, "跳出 文件接收结束");
                    break;
                }
             /*   if(mupdateProgressInterface!=null)
                {
                    mupdateProgressInterface.updateSqurebar(fileReceivePosition);
                }*/

            }
            fileReceivePosition++;
            receiveFlag = false;
            mfileOutputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }
    }

    public void setUpdateInterface(updateProgressInterface updateInterface) {
        mupdateProgressInterface = updateInterface;
    }

    /*
    * 输出流
    *
    * 图片路径
    * */
    public void sendPicture(OutputStream outputStream, String picturePath, Handler handler) {

        try {
            int length = 0;           //内容长度
            int sendLength = 0;         //已发送长度
            tempFile = new File(picturePath);
            tempFile.createNewFile();
            Message message = new Message();
            if (tempFile.exists()) {
                pictureTotalLength = tempFile.length();
                mfileInputStream = new FileInputStream(tempFile);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                if (mupdateProgressInterface != null) {

                }
                pictureTotalLength = tempFile.length();
                while ((length = mfileInputStream.read(mbuffer)) != -1) {
                    outputStream.write(mbuffer, 0, length);
                    pictureSendLength = sendLength += length;
                    mupdateProgressInterface.updateSqurebar(fileSendPosition - 1);
                    Log.e(TAG, "显示文件长度" + length + "   " + pictureTotalLength);
                }

                outputStream.flush();
                mfileOutputStream.close();   //关闭文件输出流
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());

        }


    }

    /*
    * 初始化 联系人列表
    *

    * */
    public void initSendContactInfor() {

        if (UdpReceive.getSendContactInforList() != null) {
            if (UdpReceive.getSendContactInforList().size() >= 0)
                sendContactInfoList = UdpReceive.getSendContactInforList();
        }
    }

    /*
    *
    *
    *
    * */
    public void transToPictureList(List<String> filePathList) {
        sendPictureInfoList = new ArrayList<PictureInfo>();
        for (int position = 0; position < filePathList.size(); position++) {
            tempFile = new File(filePathList.get(position));
            tempPictureInfo = new PictureInfo();
            tempPictureInfo.setPath(filePathList.get(position));
            tempPictureInfo.setFileSize(tempFile.length());
            tempPictureInfo.setName(FileUtil.getFileName(filePathList.get(position)));
            tempPictureInfo.setPlace(mrootPath);
            sendPictureInfoList.add(tempPictureInfo);

        }


    }


    /*
    *
    * Syncprogressactivity中实现此接口用于更新图片
    *
    * */
    public interface updateProgressInterface {
        void updateSqurebar(int picturePosition);
    }


}
