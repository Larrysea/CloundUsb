package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.Context;

import android.util.Log;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Larry on 4/19/2016.
 * <p/>
 * 通过udp发现网络设备类
 */
public class UdpFind implements Runnable {


    final static String TAG = "UDP  find";
    final String MUITLCASTADDRESS = "255.255.255.255";   //普通广播地址
    final String WIFIMUITLADDRESS = "192.168.43.255";   //wifi热点模式下的广播地址
    final int MUITLCASTPORT = 10009;
    String inetAddress;    //发送方的地址
    String message;//  接受到的消息
    DatagramSocket datagramSocket;
    WifiAdmin wifiAdmin;                                //wifidmin 工具
    String lastPartIp;                                          //ip地址

    /*
    * 初始化相关的属性
    *
    *
    * */
    public void initProperty(Context context) throws IOException, InterruptedException {

        java.net.InetAddress inetAddress = null;
        InetAddress wifiInetaddress = null;
        String msgIp = null;
        byte[] msg = null;
        String findProtocol = null;
        String protocol = null;
        inetAddress = InetAddress.getByName(MUITLCASTADDRESS);
        wifiInetaddress = InetAddress.getByName(WIFIMUITLADDRESS);
        datagramSocket = new DatagramSocket();
        DatagramPacket packet;
        DatagramPacket wifiPacket;
        int wifiType=0;                                             //连接上普通wifi 热点的情况下      wifiType 为1   连接上自家创建的热点情况下  wifitype为 2    如果wifi type 为3 表示没有连接 wifi
        WifiAdmin wifiAdmin = new WifiAdmin(GetContextUtil.getInstance());
        while (true) {
            msgIp = (String.valueOf(InternetTool.getWifiAddress(context))).substring(1);
            lastPartIp = StringUtil.getLastpartIpAddress(msgIp);
            if (WifiUtil.isWifiConnected(GetContextUtil.getInstance()))      //已经连接上wifi 的情况
            {
                if (wifiAdmin.getConnectWifiSsid().substring(1, 3).equals("Ta"))  //连接上自创热点的情况下
                {
                    if(lastPartIp !=null)
                    if (lastPartIp.equals("0")) {

                    } else {
                        protocol = "post_ip0" +wifiAdmin.getLastThreMac() + "A" + msgIp + "_" + msgIp + "_" + CommonUtil.getPhoneName();
                        wifiType = 2;
                    }
                } else                                                     //连接上普通wifi 的情况下
                {
                    if(lastPartIp !=null)
                    if (lastPartIp.equals("0")) {

                    } else {
                        protocol = "post_ip0" + wifiAdmin.getLastThreMac() + "A" + msgIp + "_" + msgIp + "_" + CommonUtil.getPhoneName();
                        wifiType = 1;
                    }


                }

            } else if (WifiUtil.isWifiApEnabled(GetContextUtil.getInstance()))  //自己创建热点的情况
            {
                protocol = "post_ip0" + wifiAdmin.getLastThreMac() + "A" + "192.168.43.1" + "_" + "192.168.43.1" + "_" + CommonUtil.getPhoneName();
                wifiType = 1;

            } else         //未连接上wifi 的情况
            {
                wifiType = 3;
                protocol=null;
                findProtocol=null;
            }
            if(protocol!=null)
            {
                findProtocol = new String(protocol.getBytes(), "UTF-8");
                msg = findProtocol.getBytes();

                if (wifiType == 1) {

                    packet = new DatagramPacket(msg, 0, msg.length, inetAddress, MUITLCASTPORT);
                    if (InternetTool.getWifiAddress(GetContextUtil.getInstance()) != null) {
                        datagramSocket.send(packet);
                    }

                } else if (wifiType == 2) {

                    wifiPacket = new DatagramPacket(msg, 0, msg.length, wifiInetaddress, MUITLCASTPORT);
                    if (InternetTool.getWifiAddress(GetContextUtil.getInstance()) != null) {
                        datagramSocket.send(wifiPacket);
                    }


                } else if (wifiType == 3) {

                }
            }
            Thread.sleep(1000);
        }


    }


    @Override
    public void run() {
        if (InternetTool.JudgeWifiState(GetContextUtil.getInstance()) || WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())) {

            try {
                initProperty(GetContextUtil.getInstance());

            } catch (IOException e) {
               Log.e(TAG,e.getMessage());
            } catch (InterruptedException e) {
               Log.e(TAG,e.getMessage());
            }
        }
    }
}
