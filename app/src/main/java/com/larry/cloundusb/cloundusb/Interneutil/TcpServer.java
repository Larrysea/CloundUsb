package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.activity.SendProgressActivity;
import com.larry.cloundusb.cloundusb.appinterface.updateProgressbarInterface;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.FileHistoryInfo;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.database.DB_AceClound;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Larry on 4/4/2016.
 */
public class TcpServer implements Runnable {
    final static int PORT = 8779;
    static final int OK = 1;
    final static int SEND_FILE = 3;                                     //正式发送文件
    final static int PROTOCOL_OK = 1;
    final static String TAG = "  TCP Server";
    public static boolean CONNECTED = false;                           //现在已经成功连接上客户端。false代表不存在连接，true代表现在存在连接情况
    public static long fileLength = 2;                                        //当前接收文件总的长度
    public static long fileReceiveLength = 1;                                 //当前文件接收的长度
    public static updateProgressbarInterface mupdateinterface;
    static long size = 0;
    static long totalTime = 0;                                          //总计时间
    static String errorMessage;
    static boolean IS_ALREADYEXIST = false;
    static long now_recv_length = 0;                                     //当前接受文件长度
    static DB_AceClound db_aceClound;                                      //数据库
    static FileHistoryInfo sendfileHistoryInfo;
    static FileHistoryInfo recvfileHistoryInfo;
    public SendContactInfo sendContactInfo;
    boolean life = true;
    byte[] buffer;
    boolean result = true;
    long startTime;
    long endTime;
    File tempPath = null;                                                //临时文件
    List<Bitmap> bitmapList;                                            //保存发送过来的图片
    ServerSocket serverSocket;
    Socket msocket;
    List<String> totalFileInfor;                                        //保存总的文件接受信息，做为发送完毕之后的验证作用
    String is_ok = "rec_ls_ok";
    OutputStream outputStream;
    InputStream inputStream;
    File tempFile = null;
    int serversendfile_position = 0;
    SendFileInform content;
    /*临时变量*/
    int time = 0;
    long fileTotalSizel = 0;                                                  //文件总长度
    Handler mhandler = new Handler(GetContextUtil.getInstance().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case OK:
                    Toast.makeText(GetContextUtil.getInstance(), "收到消息" + size, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(GetContextUtil.getInstance(), "接受失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(GetContextUtil.getInstance(), "接受陈宫且保存" + size + " " + errorMessage, Toast.LENGTH_SHORT).show();
                    break;
                case 3:         //反向发送文件
                  /*  try {
                        outputStream.write(InitSendListInfor().getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        ;
                    }*/
                    break;
            }
        }
    };

    public TcpServer() {
        // buffer = new byte[1024 * 60];
        sendContactInfo = new SendContactInfo();
        sendfileHistoryInfo = new FileHistoryInfo();
        recvfileHistoryInfo = new FileHistoryInfo();
        db_aceClound = new DB_AceClound(GetContextUtil.getInstance());
        try {
            totalFileInfor = new ArrayList<String>();
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(999999999);
            // serverSocket.setReceiveBufferSize(8192000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    static public void setUpdateInterface(updateProgressbarInterface updateInterface) {
        mupdateinterface = updateInterface;


    }

    public String InitSendListInfor() {
        String result = "_sflist";
        for (SendFileInform sendFileInform : (FileBox.getInstance().getSendList())) {
            result += String.valueOf(sendFileInform.getName() + "|");

        }
        return result.substring(0);//去除首四位的null
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }
    /*
    * 初始化一些变量
    *
    * */

    //返回每次接受是否成功的消息
    public boolean getState() {
        return result;
    }

    /*
    * 保存发送过来的文件
    *
    *
    * */
    public void saveFile(DatagramPacket packet) {


    }

    @Override
    public void run() {
        waitConnect();
    }

    /*
    *
    * 等待连接发送返回结果
    *
    * */
    public void waitConnect() {
        try {
            msocket = serverSocket.accept();
            Log.e(TAG, "已连接");
            CONNECTED = true;
            outputStream = msocket.getOutputStream();
            inputStream = msocket.getInputStream();
            outputStream.write("sure_link".getBytes());
            outputStream.flush();
            receiveProtocol();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    /*
    * 接受文件
    *
    * */
    public void receiveSendFileinform() {

        try {
            totalFileInfor = StringUtil.Muitlsplit(StringUtil.inputStreamToString(inputStream), "|");
            if (!totalFileInfor.isEmpty()) {
                for (String parm : totalFileInfor) {

                }
            }
            outputStream.write(is_ok.getBytes()); //返回成功消息
        } catch (IOException e) {
            Log.e("显示error信息", e.getMessage() + " " + e.getCause());
            e.printStackTrace();
        }
    }

    /*
        *
        *发送文件件信息
        * */
    public void sendFileInform(OutputStream outputStream, int filePosition) {

        int size = FileBox.getInstance().getSendListSize();
        SendFileInform sendFileInform = FileBox.getInstance().getSendList().get(size - 1 - filePosition);
        {
            String sendinfor = "open_handle11|" + sendFileInform.getName() + "|" + sendFileInform.getFilesize() + "|" + sendFileInform.getParentFileSize();
            try {

                outputStream.write(sendinfor.getBytes());
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(final int position) {

        if (FileBox.getInstance().getSendList() != null &&
                FileBox.getInstance().getSendList().size() != 0) {

            buffer = new byte[1460 * 8];
            List<SendFileInform> sendFileInforms = FileBox.getSendList();
            int fileSendLength = 0;//文件已经发送的长度
            long fileLength;//文件总长度
            int endJudge = 0;
            int fileSize = FileBox.getInstance().getSendListSize();
            content = sendFileInforms.get(serversendfile_position);
            fileLength = sendFileInforms.get(position).getFilesize();
            String file_path = "";
            try {
                file_path = sendFileInforms.get(serversendfile_position).getPath();
                File file = new File(file_path);

                file.createNewFile();
                fileLength = file.length();
                FileInputStream fileInputStream = new FileInputStream(file);
                while ((endJudge = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, endJudge);
                    fileSendLength += endJudge;
                   /* if (mupdateProgressbar != null)
                    {
                       mupdateProgressbar.updateProgressbar(filePosition,fileSendLength,(int)fileLength);
                    }*/

                }
                fileInputStream.close();

                sendfileHistoryInfo.SetItemValue(sendContactInfo.getResourceId(),
                        sendContactInfo.getName(),
                        file.getPath(),
                        file.getName(),
                        file.length(),
                        Long.toString(CommonUtil.GetTime()),
                        0,
                        sendContactInfo.getType()
                );
                db_aceClound.SaveTFInfo(recvfileHistoryInfo);


                int filelistsize = FileBox.getInstance().getSendListSize();
                serversendfile_position++;
                if (serversendfile_position < filelistsize) {

                    serversendfile_position++;
                    Thread.sleep(1000);
                    sendFileInform(outputStream, serversendfile_position);
                } else {
                    //--serversendfile_position;
                }
            } catch (Exception e) {
                Log.e("udpclient error", e.getMessage());
            }
        }
    }

    /*
    *
    * 处理协议内容然后进行相应操作
    *
    *
    * */
    public void receiveProtocol() {
        try {
            byte buffer[] = new byte[1460 * 8];
            int receiveLength = 0;
            int filePosition = 0;
            filePosition = -1;                //在客户端断开连接后还原
            Bundle buffer_bund = new Bundle();
            while (true) {
                if (tempFile == null) {
                    buffer_bund = StringUtil.inputStreamToByteArray(inputStream);
                    receiveLength = buffer_bund.getInt("streamlength");
                    buffer = buffer_bund.getByteArray("streamvalue");

                      /* 协议的分析部分   */
                    if (receiveLength < 500) {
                        String protocol = new String(buffer, "UTF-8");
                        //  String  protocol=new String(buffer,"unicode");
                        if ((protocol.substring(0, 7)).equals("_sflist")) {

                            totalFileInfor = StringUtil.Muitlsplit(protocol.substring(7), "|");
                            outputStream.write("rec_ls_ok".getBytes());
                            outputStream.flush();
                            stroageReceiveInform(totalFileInfor);
                            MainActivity.getHandler().sendEmptyMessage(2);

                        } else if ((protocol.substring(0, 7)).equals("post_ip")) {
                            ;

                        } else if ((protocol).equals("open_handle_ok")) {
                            send(filePosition);
                        } else if ((protocol).equals("open_handle_no")) {
                            filePosition++;

                        } else if (protocol.substring(0, 9).equals("rec_ls_ok")) {

                            sendFileInform(outputStream, serversendfile_position);              //发送文件信息

                        } else if (protocol.substring(0, 11).equals("open_handle")) {
                            now_recv_length = 0;
                            List<String> result;
                            result = StringUtil.Muitlsplit(protocol, "|");
                            filePosition = Integer.valueOf(result.get(0).substring(12));
                            String length = result.get(2);
                            filePosition++;
                            String file_name = result.get(1);
                            tempFile = FileUtil.createFile(file_name);
                            FileBox.getInstance().getReceiveList().get(filePosition - 1).setPath(tempFile.getPath());
                            outputStream.write("open_handle_ok".getBytes());
                            outputStream.flush();
                            fileLength = Long.parseLong(length);
                            time++;
                            //    mupdateinterface.addFile(filePosition);
                        } else if (protocol.substring(0, 15).equals("_send_user_name")) {

                            //解析信息
                            protocol = protocol.substring(15);
                            int length = protocol.length();
                            sendContactInfo.setType(Integer.parseInt(protocol.substring(length - 2, length - 1)));
                            protocol = StringUtil.Muitlsplit(protocol, "|").get(0);
                            length = protocol.length();
                            String portraitId = protocol.substring(length - 1);
                            sendContactInfo.setResourceId(Integer.parseInt(portraitId));
                            sendContactInfo.setName(protocol.substring(0, length - 3));
                            String infor;
                            if (MainActivity.user.isLogin()) {
                                infor = "my_head_picid" + MainActivity.user.getPortraitId() + "0";
                            } else {
                                infor = "my_head_picid00";
                            }
                            outputStream.write(infor.getBytes());
                            outputStream.flush();
                        }
                    }
                } else//正式接收文
                {

                    Log.e(TAG, "正式接收文件copyfile");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());

                    }
                    FileUtil.copyFile(inputStream, fileLength, tempFile, filePosition - 1, mupdateinterface);
                    recvfileHistoryInfo.SetItemValue(
                            sendContactInfo.getResourceId(),
                            sendContactInfo.getName(),
                            tempFile.getPath(),
                            tempFile.getName(),
                            tempFile.length(),
                            Long.toString(CommonUtil.GetTime()),
                            0,
                            sendContactInfo.getType());
                    db_aceClound.SaveTFInfo(recvfileHistoryInfo);
                    tempFile = null;

                }

            }
        } catch (IOException e) {
            Log.e("tcpserver", e.getMessage() + "  " + e.getCause());

        }
    }

    /*
    * 发送文件的头像
    *
    * */
    public void sendPortrailt(InputStream inputStream, OutputStream outputStream) {

        List<Bitmap> bitmaplist = FileBox.getInstance().getSendPortraitList();
        int count = 0;
        while (count < bitmaplist.size()) {
            count++;
            inputStream = GraphicsUtil.bitmapToByte(bitmaplist.get(count));
            try {
                outputStream.write(inputStream.read());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }


    }

    /*
    *
    * 发送单个文件信息
    * */
    public void sendFileInfor() {

    }

    public Handler gethandler() {
        return mhandler;
    }

    /*
    *
    *
    * */
    public void stroageReceiveInform(List<String> receiveInform) {
        for (String filename : receiveInform) {
            SendFileInform sendFileInform = new SendFileInform();
            sendFileInform.setName(filename);
            FileBox.getInstance().storageReceiveFileItem(sendFileInform);
        }

    }

}
