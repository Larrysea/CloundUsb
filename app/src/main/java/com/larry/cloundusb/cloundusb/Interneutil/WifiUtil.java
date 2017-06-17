package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LARRYSEA on 2016/7/16.
 * wifi设置的基本工具类
 * <p/>
 * <p/>
 * 使用说明 调用openwifi设置打开wifi热点即可
 */
public class WifiUtil {
    static WifiManager wifiManager;                                    //wifimanager用来获取wifimanager的服务
    static public boolean Openflag = false;                            //判断是否带来热点，已经打开则为true 否则为false
    static ArrayList<String> connectedIP = new ArrayList<String>();         // 已连接上的设备
    static final String TAG = WifiUtil.class.toString();


    /*
    *
    * 调用openwifi打开热点
    * */
    static public void openWifi(Context context, String wifiName, String wifiPassword) {

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //如果是打开状态就关闭，如果是关闭就打开
        if (!Openflag) {
            Openflag = !Openflag;
            setWifiApEnabled(wifiName, wifiPassword, Openflag);
        }

    }


    // wifi热点开关
    static public boolean setWifiApEnabled(String wifiName, String wifiPassword, boolean enabled) {
        if (enabled) {
            // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
            wifiManager.getWifiState();
        }
        try {
            return initHostSpotConfig(wifiName, wifiPassword, enabled);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    /*
    * 初始化分享热点的相关信息
    *
    * 参数wifi名称
    *
    * wifi密码
    *
    *
    * */

    static public boolean initHostSpotConfig(String wifiName, String wifiPassword, boolean enabled) throws IllegalAccessException, InvocationTargetException {


        //热点的配置类
        WifiConfiguration netConfig = new WifiConfiguration();

        //获取wifi管理服务

        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        netConfig.SSID = wifiName;
        netConfig.preSharedKey = wifiPassword;
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


        return (Boolean) method.invoke(wifiManager, netConfig, enabled);


    }


    /*
    * 获取已连接wifi热点设备ip
    *
    * */







    /*
    *
    * 获取连接上热点的设备信息
    *
    * 返回包含ip地址的链表
    *
    * */

    static public List<String> readClientList() {


        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader("/data/misc/dhcp/dnsmasq.leases"));
            String line;
            String ipAddress;
            int waitTime;  //等待时间
            String[] splitted;
            for (waitTime = 0; waitTime < 3; waitTime++) {
                while ((line = br.readLine()) != null) {
                    splitted = line.split(" ");
                    if (splitted != null && splitted.length >= 4) {
                        String ip = splitted[2];
                        ipAddress = ip.substring(0, ip.lastIndexOf("."));
                        if (ipAddress.equals("192.168.43"))
                            if (connectedIP.size() == 0) {
                                connectedIP.add(ip);
                            } else if (checkAddressIsAdded(ipAddress)) {
                                connectedIP.add(ip);
                            }
                    }

                }
            }

        } catch (IOException e) {

        }
        return connectedIP;
    }


    /*
    * 检查wifi地址是否已经添加
    *
    *
    * */
    static public boolean checkAddressIsAdded(String ipaddress) {
        boolean isadded = false;
        for (String address : connectedIP) {
            if (address == ipaddress)
                isadded = true;

        }
        return isadded;


    }


    /*
    *
    * 检查wifi热点是否打开
    *
    * 打开返回 true
    *
    * 否则返回false
    *
    *
    * */
    public static Boolean isWifiApEnabled(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(manager);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
    * 判断wifi 是否连接
    *
    *
    * */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }


}
