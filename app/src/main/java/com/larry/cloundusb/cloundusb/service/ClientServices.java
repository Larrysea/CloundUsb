package com.larry.cloundusb.cloundusb.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.Interneutil.TcpClient;
import com.larry.cloundusb.cloundusb.Interneutil.WifiAdmin;
import com.larry.cloundusb.cloundusb.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 5/7/2016.
 *
 *
 * 服务端的services
 *
 */
public class ClientServices extends Service{

    private List<ScanResult> wifiList;
    private WifiManager wifiManager;
    private List<String> passableHotsPot;
    private WifiReceiver wifiReceiver;
    private boolean isConnected = false;
    private Button connect;
    private Button send;
    TcpClient mclient;
    InetSocketAddress mInetAddress;
    ImageView imageview;

    final int PORT=8779; //端口


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        if (wifiList == null || wifiList.size() == 0 || isConnected)
            return null;
        onReceiveNewNetworks(wifiList);

        return null;
    }



    /* 初始化参数 */
    public void init() {

        File file=new File("/storage/emulated/0/DCIM/Camera/20151124_192225.jpg");
        FileInputStream inputStream=null;
        try{

            inputStream=new FileInputStream(file);
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
        imageview.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();


        if (!wifiManager.isWifiEnabled())
            if (wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifiManager.setWifiEnabled(true);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

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



    /*连接到热点*/
    public void connectToHotpot() {

        if (passableHotsPot == null || passableHotsPot.size() == 0)
            return;
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifinfo =connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiAdmin admin=new WifiAdmin(this);
        WifiInfo contenInfo=wifiManager.getConnectionInfo();

      /*  if(wifinfo.isConnectedOrConnecting())
        {
            Log.e("断开连接","close"+contenInfo.getNetworkId());
            admin.disconnectWifi(contenInfo.getNetworkId());
            //admin.closeWifi();

        }*/
        admin.openWifi();
        admin.addNetwork(admin.CreateWifiInfo("sf20160427WEC7DE", "123456789", 3));
        // admin.getIPAddress();
        //String address=InternetTool.intToIp(admin.getIPAddress());
        Log.e("喜爱是收到的ip", InternetTool.intToIp(admin.getIPAddress())+"");
        File path=new File( Environment.getExternalStorageDirectory()+"/"+getBaseContext().getPackageName()+"/"+"wenben.txt");
        if(!path.exists())
            path.mkdirs();


        //   new FileClient(path);
        //startService(new Intent(getBaseContext(),FileClient.class));
        //mclient=new UdpClient(path);
        String.valueOf(InternetTool.intToIp(admin.getIPAddress()));
       // mclient=new TcpClient();


        try {
            // mInetAddress =new  InetSocketAddress(Integer.valueOf(InternetTool.intToIp(admin.getIPAddress())));

        }
        finally {

        }




    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }


    /*当搜索到新的wifi热点时判断该热点是否符合规格*/
    public void onReceiveNewNetworks(List<ScanResult> wifiList) {

        passableHotsPot = new ArrayList<String>();
        for (ScanResult result : wifiList) {

            if(StringUtil.checkWifiName(result.SSID))
            {
                passableHotsPot.add(result.SSID);

            }


        }
        synchronized (this) {
            connectToHotpot();
        }
    }






}
