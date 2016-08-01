package com.larry.cloundusb.cloundusb.Interneutil;

import android.util.Log;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 4/19/2016.
 * <p>
 * udp连接的接收方
 */
public class UdpReceive implements Runnable {


    //static HashMap<String, SendContactInfo> addressHashMap = new HashMap<>(); //保存接受扫描到的联系人信息
    static List<SendContactInfo> sendContactInfosList;                          //发送人信息列表
    final String MUITLCASTADDRESS = "255.255.255.255";                          //广播目的地址
    final int MUITLCASTPORT = 10009;
    final int PACKETlENGTH = 1024;
    boolean LIFE = true;   //线程的生存状态
    InetAddress inetaddress;

    DatagramPacket datagramPacket;
    DatagramSocket datagramSocket;
    SendContactInfo msendContactInfor;
    final static String TAG="udpreceive";


    public static int nowselectuserindex;                                        //获取当前选中的用户的下表

    @Override
    public void run() {

        try {
            if(sendContactInfosList!=null)
            {
                sendContactInfosList=null;
            }
            sendContactInfosList = new ArrayList<SendContactInfo>();
            inetaddress = InetAddress.getByName(MUITLCASTADDRESS);
            datagramSocket = new DatagramSocket(MUITLCASTPORT);
            datagramPacket = new DatagramPacket(new byte[PACKETlENGTH], PACKETlENGTH);

           WifiAdmin wifiAdmin=new WifiAdmin(GetContextUtil.getInstance());
            while (LIFE) {
                boolean type=true;                                                //为post则为真   为get则为假
                datagramSocket.receive(datagramPacket);
                String ipaddress =null;
                String selfAddress;
                if (datagramPacket != null) {
                    String a = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(),"UTF-8");      //是否是同一个手机如果是不添加
                    if(a.substring(0,3).equals("get"))
                    {
                      type=false;
                    }
                    ipaddress = StringUtil.getLastpartIpAddress(SendContactInfo.transformSendContactInfor(a,type).getIpAddress());
                    if (ScanWifi.isConnected||WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())|| wifiAdmin.getConnectWifiSsid().substring(1,3).equals("Ta"))
                    {
                        selfAddress="1";
                    }else
                    {
                        selfAddress = StringUtil.getLastpartIpAddress(String.valueOf(wifiAdmin.getIPAddress()));
                    }

                    if(ipaddress!=null)
                    {
                    if (!ipaddress.equals(selfAddress)) {
                        if (sendContactInfosList != null) {
                            SendContactInfo sendContactInfo = new SendContactInfo();
                            sendContactInfo = SendContactInfo.transformSendContactInfor(a,type);
                            sendContactInfo.setResourceId(R.drawable.temp_portrait);
                            if (sendContactInfosList.size() == 0) {
                                sendContactInfosList.add(sendContactInfo);
                                Log.e(TAG,"添加信息");
                            } else {
                                boolean isExit = false;
                                for (int position = 0; position < sendContactInfosList.size(); position++) {
                                    msendContactInfor = sendContactInfosList.get(position);
                                    if (msendContactInfor.getIpAddress().equals(sendContactInfo.getIpAddress())) {
                                        isExit = true;
                                    }
                                }
                                if (isExit == false) {
                                    sendContactInfosList.add(sendContactInfo);
                                }
                            }
                            String postMessage = "get_ip0macA" + InternetTool.getLocalIpaddress(GetContextUtil.getInstance()) + "_" + InternetTool.getLocalIpaddress(GetContextUtil.getInstance()) + "_" + CommonUtil.getPhoneName();
                            datagramPacket.setData(postMessage.getBytes());
                            datagramSocket.send(datagramPacket);
                        }


                    }
                    }
                }
            }
            datagramSocket.close();
        } catch (IOException e) {
            Log.e("udp receiver", e.getMessage()+"   "+e.getCause());
        }


    }

    /*
    *
    * 获取扫描到的待发送人信息
    *
    * */

    static public List<SendContactInfo> getSendContactInforList() {
        if (sendContactInfosList != null && sendContactInfosList.size() != 0) {
            return sendContactInfosList;

        }
        return  sendContactInfosList;

    }


    public int getSendContactListSize() {
        return sendContactInfosList.size();
    }
}
