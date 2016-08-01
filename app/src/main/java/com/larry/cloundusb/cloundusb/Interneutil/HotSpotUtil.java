package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CommonUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Larry on 5/7/2016.
 * 开启wifi 热点工具类
 *
 *
 */
public class HotSpotUtil {

    static private WifiManager wifiManager;
    static public  boolean wifiLifeFlag = false;  //热点是否被开启
    String wifiName;//初始化的wifi名称



    /*
    * 初始化分享热点的相关信息
    *
    *
    * */

    static public boolean initHostSpotConfig(boolean enabled) throws IllegalAccessException,InvocationTargetException
    {


        wifiManager = (WifiManager) GetContextUtil.getInstance().getSystemService(Context.WIFI_SERVICE);
        //热点的配置类
        WifiConfiguration netConfig = new WifiConfiguration();
        //配置热点的名称(可以在名字后面加点随机数什么的)
           /* apConfig.SSID = "YRCCONNECTION";
            //配置热点的密码
            apConfig.preSharedKey = "12122112";*/
        //apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);


        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);

        }catch(NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        netConfig.SSID=initWifiname();
        netConfig.preSharedKey = "123456789";
        netConfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement
                .set(WifiConfiguration.KeyMgmt.WPA_PSK);
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.CCMP);
        netConfig.allowedPairwiseCiphers
                .set(WifiConfiguration.PairwiseCipher.TKIP);
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.CCMP);
        netConfig.allowedGroupCiphers
                .set(WifiConfiguration.GroupCipher.TKIP);



        /*ExecutorService service= Executors.newCachedThreadPool();
        TcpServer serverSocket=new TcpServer();
        //   service.execute(serverSocket);
        serverSocket.execute("ddsc");*/
        return (Boolean) method.invoke(wifiManager, netConfig, enabled);



    }

    // 设置wifi打开还是关闭  true打开wifi
     static   public boolean setWifiApEnabled(boolean enabled) {
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        try {
            return initHostSpotConfig(enabled);
        } catch (Exception e) {
            return false;
        }
    }



    /*
    * 关闭wifi网络
    *
    * */

   static  public void closeWifi(String wifiname)
    {
        setWifiApEnabled(false);
    }


    /*
    *
    * 初始化wifi的名称
    *
    *
    * */

    static  public String initWifiname() {

        wifiLifeFlag=false;
        WifiAdmin admin=new WifiAdmin(GetContextUtil.getInstance());
        String macAddress=admin.getMacAddress();
        String thirdMac= String.valueOf(macAddress.charAt(macAddress.length()-1)).
                concat(String.valueOf(macAddress.charAt(macAddress.length()-2))
                        .concat(String.valueOf(macAddress.charAt(macAddress.length()-4))));
        return "携传A"+thirdMac+ CommonUtil.getPhoneName();

    }


    /*
    * 获取mac地址
    *
    * */
  static   public String getMacAddress()
    {
        WifiManager wifi = (WifiManager)GetContextUtil.getInstance().getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }



      /*
      *
      * 这个方法有时候会获取不到地址
      *
      *
      *
      * */

       @Deprecated
       static public  String getLastThreeMac()
        {
            String macSerial = null;
            String str = "";

            try
            {
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);

                for (; null != str;)
                {
                    str = input.readLine();
                    if (str != null)
                    {
                        macSerial = str.trim();            // 去空格
                        break;
                    }
                }
            } catch (IOException ex) {
                // 赋予默认值
                ex.printStackTrace();
            }
            WifiAdmin wifiAdmin=new WifiAdmin(GetContextUtil.getInstance());
            wifiAdmin.getMacAddress();

            String  lastMac=String.valueOf((char)macSerial.getBytes()[13]);
            lastMac+=String.valueOf((char)macSerial.getBytes()[15]);
            lastMac+=String.valueOf((char)macSerial.getBytes()[16]);


            return lastMac;
        }



}
