package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LARRYSEA on 2016/7/16.
 * <p>
 * 扫描外部wifi类  监听外部是否有用户
 */
public class ScanWifi implements Runnable {

    static WifiManager wifiManager;
    private List<ScanResult> wifiList;  //扫描到的wifi结果
    WifiReceiver wifiReceiver;
    public static boolean isConnected = false;    //是否连接上别人创建的wifi   如果已经连接上则当扫描到新的符合标准的wifi时不进行连接
    static private List<String> passableHotsPot;  //符合标准的host

    @Override
    public void run() {


        while (true) {
            connectWifi();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

        }


    }


    /*
    * 初始化相关配置
    *
    *
    * */
    public ScanWifi() {
        wifiManager = (WifiManager) GetContextUtil.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();


        if (!wifiManager.isWifiEnabled())
            if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifiManager.setWifiEnabled(true);

        GetContextUtil.getInstance().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    /* 监听热点变化 */
    private final class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            wifiList = wifiManager.getScanResults();
            if (wifiList == null || wifiList.size() == 0 || isConnected)
                return;
            // onReceiveNewNetworks(wifiList);

        }
    }


    /*当搜索到新的wifi热点时判断该热点是否符合规格*/
    public void onReceiveNewNetworks(List<ScanResult> wifiList) {

        passableHotsPot = new ArrayList<String>();
        for (ScanResult result : wifiList) {
            if ((result.SSID).substring(0, 2).equals("Ta")) {
                checkWifiIsAdded(result.SSID);
            }

        }
        synchronized (this) {
            connectToHotpot(passableHotsPot.get(0), "123456789");
        }
    }


    /*连接到热点*/
    static public void connectToHotpot(String wifiName, String wifiPassword) {

        if (passableHotsPot == null || passableHotsPot.size() == 0) {
            passableHotsPot = new ArrayList<String>();
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) GetContextUtil.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifinfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiAdmin admin = new WifiAdmin(GetContextUtil.getInstance());
        WifiInfo contenInfo = wifiManager.getConnectionInfo();

        if (wifinfo.isConnectedOrConnecting()) {
            Log.e("断开连接", "close" + contenInfo.getNetworkId());
            admin.disconnectWifi(contenInfo.getNetworkId());
            admin.closeWifi();

        }
        admin.openWifi();
        admin.addNetwork(admin.CreateWifiInfo(wifiName, wifiPassword, 3));
        isConnected = true;

        Log.e("喜爱是收到的ip", InternetTool.intToIp(admin.getIPAddress()) + "");

    }


    /*
    *
    * 连接wifi
    *
    * */
    public void connectWifi() {
        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        if (wifiList == null || wifiList.size() == 0 || isConnected)
            return;
        onReceiveNewNetworks(wifiList);
    }

    /*
    *
    * 检查wifi是否已经添加
    *
    *
    * 为添加则添加wifi到连接链
    * */
    public void checkWifiIsAdded(String wifiSsid) {

        for (String wifiSSID : passableHotsPot) {
            if (wifiSSID != wifiSsid) {
                passableHotsPot.add(wifiSSID);
            } else {
                return;
            }

        }

    }

}
