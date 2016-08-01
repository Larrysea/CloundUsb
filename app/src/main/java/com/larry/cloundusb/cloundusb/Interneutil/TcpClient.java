package com.larry.cloundusb.cloundusb.Interneutil;

import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by Larry on 4/4/2016.
 * <p/>
 * 发送文件客户端
 */
public class TcpClient implements Runnable {

    final static int PORT = 8779;
    final static int SEND_FILE = 3;//正式发送文件
    final static int PROTOCOL_OK = 1;
    final static String TAG = "tcpclient ";
    public static boolean IS_ALREADYEXIST = false;//用于验证线程是否存在，如果存在则不添加新线程，存在未true
    public static Socket mclientSocket;
    public static boolean CONNECTED = false;
    public static int filePosition = 0;//发送的文件位置  具体发送到哪一个文件每次正式发送文件的时候需要清零
    public static long fileLength = 2;//文件总长度
    public static long fileSendLength = 1;//已发送的长度
    public static long FileSendTime;   //文件正式发送时间
    static String close = "close_file";
    static boolean SERVERLIFE = true;  //服务器端是否存活
    static boolean CLIENTLIFE = true;   //客户端是否存活
    static List<String> totalFileInfor;//保存总的文件接受信息，做为发送完毕之后的验证作用
    static updateProgressbarInterface mupdateProgressbar;
    static DB_AceClound db_aceClound;           //数据库
    static FileHistoryInfo sendfileHistoryInfo;
    static FileHistoryInfo recvfileHistoryInfo;
    String msg;
    InetAddress minteaddress;
    SocketAddress msocketAddress;
    byte[] buffer;
    int flag;
    SendFileInform content;
    OutputStream outputStream;
    InputStream inputStream;
    InputStream copyInputStream;
    FileUtil tempFile = null;
    int isfirest;
    boolean isSelected = false;   //是否是选择的文件
    InetAddress connectedDevice;    //要连接设备的ip
    static updateProgressbar mupdateNextProgressbar;

    Handler handler = new Handler(GetContextUtil.getInstance().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROTOCOL_OK:
                    send(msg.arg1);
                    break;
                case SEND_FILE:
                    sendFileInform(outputStream, msg.arg1);
                    break;
                case 2:
                    try {
                        outputStream.write(InitSendListInfor().getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                case 4:           //取消发送文件
                    CLIENTLIFE = false;
                    break;


            }
        }
    };


    public TcpClient() {
        buffer = new byte[1024 * 60];
        sendfileHistoryInfo = new FileHistoryInfo();
        recvfileHistoryInfo = new FileHistoryInfo();
        db_aceClound = new DB_AceClound(GetContextUtil.getInstance());
        IS_ALREADYEXIST = true;
        SendProgressActivity.setStartReceive(new SendProgressActivity.startReceiveIn() {
            @Override
            public void receive(int filePosition) {



            }

            @Override
            public void addFile(int startPosition) {
                sendFileInform(outputStream, startPosition);
            }
        });


    }

    static public void setUpdateInterFace(updateProgressbarInterface parm) {
        mupdateProgressbar = parm;

    }

    /*
    * 初始化发送文件信息
    * 发送文件名字信息
    * return  name.apk|b.txt|ad.doc  示例
    * */
    public String InitSendListInfor() {
        String result = "_sflist";
        for (SendFileInform sendFileInform : FileBox.getInstance().getSendList()) {
            result += String.valueOf(sendFileInform.getName() + "|");

        }
        return result.substring(0);//去除首四位的null
    }

    public void connect(InetAddress inetAddress) {
        minteaddress = inetAddress;
        try {

            mclientSocket = new Socket(minteaddress, PORT);
            //  mclientSocket.connect(new InetSocketAddress(minteaddress,PORT));
            Log.e("显示ip地址地址  连接成功", minteaddress + " ");
            CONNECTED = true;
            outputStream = mclientSocket.getOutputStream();
            inputStream = mclientSocket.getInputStream();
            receiveProtocol();

        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }

    }

    /*
    *
    * 发送文件函数
    *
    *  parm1  保存的文件路径
    *
    *  parm2 将要发送的socketaddress
    *
    *  parm3  发送第几个文件
    *
    * */
    public void send(final int position) {
        Log.e(TAG, "tcpclient 正式发送文件");
        FileSendTime = System.currentTimeMillis();
        if (FileBox.getInstance().getSendList() != null &&
                FileBox.getInstance().getSendListSize() != 0 && minteaddress != null) {
            buffer = new byte[1460 * 8];
            List<SendFileInform> sendFileInforms = FileBox.getInstance().getSendList();
            int endJudge = 0;
            int fileSize = FileBox.getInstance().getSendListSize();
            content = sendFileInforms.get(filePosition);
            fileLength = sendFileInforms.get(position).getFilesize();
            String file_path;
            try {
                file_path = sendFileInforms.get(filePosition).getPath();
                File file = new File(file_path);
                file.createNewFile();
                fileLength = file.length();
                FileInputStream fileInputStream = new FileInputStream(file);
                Thread.sleep(2000);
                while ((endJudge = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, endJudge);
                    fileSendLength += endJudge;
                    mupdateProgressbar.updateProgressbar(filePosition, fileSendLength, fileLength);
                }
                outputStream.flush();
                //添加传输记录
                sendfileHistoryInfo.SetItemValue(UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getResourceId(),
                        UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getName(),
                        file_path,
                        sendFileInforms.get(filePosition).getName(),
                        sendFileInforms.get(filePosition).getFilesize(),
                        Long.toString(CommonUtil.GetTime()), 1, UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getType()
                );

                db_aceClound.SaveTFInfo(sendfileHistoryInfo);
                fileInputStream.close();
                filePosition++;
                int filelistsize = FileBox.getInstance().getSendListSize();
                if (filePosition < filelistsize) {
                    Thread.sleep(1000);
                    sendFileInform(outputStream, filePosition);
                }
            } catch (Exception e) {
                Log.e("udpclient error", e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        Log.e(TAG, minteaddress + "");
        connect(minteaddress);
    }

    public Handler getHandler() {

        return handler;
    }

    /*
    * 发送详细文件对象其中包括Sendfileinform
    *
    * 将文件信息发送过去
    *
    * */

    /*
    *
    *发送文件件信息
    * */
    public void sendFileInform(OutputStream outputStream, int filePosition) {

        SendFileInform sendFileInform = FileBox.getInstance().getSendList().get(filePosition);
        {
            if (filePosition == 0) {
                isfirest = 1;
            } else {
                isfirest = 0;
            }
            String sendinfor;
            if (sendFileInform.getParentFileSize() == 0) {
                sendinfor = "open_handle" + isfirest + "" + filePosition + "|" + sendFileInform.getName() + "|" + sendFileInform.getFilesize() + "|" + sendFileInform.getFilesize();
            } else {
                sendinfor = "open_handle" + isfirest + "" + filePosition + "|" + sendFileInform.getName() + "|" + sendFileInform.getFilesize() + "|" + sendFileInform.getParentFileSize();
            }
            Log.e(TAG, sendinfor);
            try {

                outputStream.write(sendinfor.getBytes());
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
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

        byte buffer[] = new byte[1460 * 8];
        filePosition = 0;
        String protocol;
        int now_recv_length = 0;
        int receiveLength = 0;
        Bundle buffer_bundle = new Bundle();
        try {
            while (SERVERLIFE) {
                //  copyInputStream = inputStream;
                buffer_bundle = StringUtil.inputStreamToByteArray(inputStream);
                buffer = buffer_bundle.getByteArray("streamvalue");
                receiveLength = buffer_bundle.getInt("streamlength");
                if (receiveLength < 500) {                      /* 协议的分析部分   */
                    protocol = new String(buffer);
                    if ((protocol.substring(0, 7)).equals("_sflist")) {
                        totalFileInfor = StringUtil.Muitlsplit(protocol.substring(7), "|");
                        outputStream.write("rec_ls_ok".getBytes());                          //发送所有文件信息
                        outputStream.flush();
                    }
                    if ((protocol.substring(0, 7)).equals("post_ip")) {


                    } else if (protocol.substring(0, 9).equals("sure_link")) {
                        String myInfor;
                        if (MainActivity.user.isLogin()) {
                            if(MainActivity.user.getName().trim().equals(""))
                            {

                                myInfor = "_send_user_name" +"xiechuan"+ MainActivity.user.getPortraitId() + "0|";
                            }else
                            {
                                myInfor = "_send_user_name" + MainActivity.user.getName() + MainActivity.user.getPortraitId() + "0|";
                            }
                        } else {
                            myInfor = "_send_user_name" + CommonUtil.getPhoneName() + "00|";
                        }
                        outputStream.write(myInfor.getBytes());
                        outputStream.flush();
                    } else if ((protocol.substring(0, 9)).equals("rec_ls_ok")) {
                        sendFileInform(outputStream, filePosition);                        //发送单个文件信息
                    } else if (protocol.equals("open_handle_ok")) {
                        send(filePosition);
                    } else if (protocol.equals("open_handler_no")) {
                        filePosition++;
                        //跳过本次发送发送下一个

                    } else if ((protocol.substring(0, 11)).equals("open_handle")) {
                        now_recv_length = 0;
                        List<String> result;
                        result = StringUtil.Muitlsplit(protocol, "|");
                        filePosition = Integer.valueOf(result.get(0).substring(12));
                        String leng = result.get(2);
                        filePosition++;
                        String file_name = result.get(1);
                        tempFile = new FileUtil(file_name, Long.parseLong(result.get(2)));
                        //tempFile = FileUtil.createFile(totalFileInfor.get(filePosition - 1));
                        outputStream.write("open_handle_ok".getBytes());
                        outputStream.flush();


                    } else if (protocol.substring(0, 13).equals("my_head_picid")) {

                        outputStream.write(InitSendListInfor().getBytes());
                        outputStream.flush();
                    } else if (protocol.substring(0, 15).equals("_send_user_name")) {
                        outputStream.write(InitSendListInfor().getBytes());
                        outputStream.flush();

                    } else {
                        if (receiveLength > 0)             //防止空数据
                        {
                            now_recv_length += receiveLength;
                            if (FileUtil.copyFile(buffer, tempFile, receiveLength, now_recv_length)) {
                                recvfileHistoryInfo.SetItemValue(UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getResourceId(),
                                        UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getName(),
                                        tempFile.getDefaultPath(),
                                        tempFile.FileName,
                                        tempFile.file_length,
                                        Long.toString(CommonUtil.GetTime()),
                                        0,
                                        UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getType()
                                );
                                db_aceClound.SaveTFInfo(recvfileHistoryInfo);
                            }
                        }
                    }

                } else {//正式接收文件
                    now_recv_length += receiveLength;
                    if(filePosition>0)
                    {
                        mupdateNextProgressbar.updateNextProgressbar();
                    }
                    if (FileUtil.copyFile(buffer, tempFile, receiveLength, now_recv_length)) {
                        recvfileHistoryInfo.SetItemValue(UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getResourceId(),
                                UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getName(),
                                tempFile.getDefaultPath(),
                                tempFile.FileName,
                                tempFile.file_length,
                                Long.toString(CommonUtil.GetTime()),
                                0,
                                UdpReceive.getSendContactInforList().get(UdpReceive.nowselectuserindex).getType()
                        );
                        db_aceClound.SaveTFInfo(recvfileHistoryInfo);
                    }
                }
            }

        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }


    }

    /*
       * 初始化ip地址
       *
       * */
    public void initInetAddress(InetAddress inetAddress) {

        minteaddress = inetAddress;
        isSelected = true;

    }

    /*
    * 设置要连接的ip是来自于  局域网wifi热点  还是来自于自己创建的热点
    *
    * fromtype    1  表示来自于局域网热点信息
    *
    *             2  表示来自己创建的热点连接信息
    *
    *
    * */
    public void setConnecteIpFrom(int fromtype) {
        String ip = null;
        if (fromtype == 1) {
            ip = UdpReceive.getSendContactInforList().get(0).getIpAddress();
        } else if (fromtype == 3) {
            if (WifiUtil.readClientList() != null && WifiUtil.readClientList().size() > 0) {
                ip = UdpReceive.getSendContactInforList().get(0).getIpAddress();
            }
        }
        else if(fromtype==4)
        {
            ip="192.168.43.61";
        }else  if(fromtype==2)
        {
            ip = UdpReceive.getSendContactInforList().get(0).getIpAddress();
        }
        try {
            connectedDevice = InetAddress.getByName(ip);
            minteaddress = connectedDevice;
        } catch (UnknownHostException e) {

            e.printStackTrace();
        }

    }


   public  interface updateProgressbar
    {

        void updateNextProgressbar();
    }

    /*
    * 更新下一个progressbar
    *
    * */
    static public void setUpdateProgressbar(updateProgressbar updateProgressbar)
    {
        mupdateNextProgressbar=updateProgressbar;


    }


}








