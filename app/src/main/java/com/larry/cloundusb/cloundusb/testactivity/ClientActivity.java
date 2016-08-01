package com.larry.cloundusb.cloundusb.testactivity;

/**
 * Created by Larry on 4/3/2016.
 */

import android.app.Activity;
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
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.Interneutil.TcpClient;
import com.larry.cloundusb.cloundusb.Interneutil.WifiAdmin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends Activity {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tets);
        init();
    }

    /* 初始化参数 */
    public void init() {


        connect = (Button) findViewById(R.id.button3);
        send=(Button)findViewById(R.id.button4);
        imageview=(ImageView)findViewById(R.id.test_image_view);
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
        connect.setOnClickListener(new Listener());
        send.setOnClickListener(new Listener());
    }



    /*当搜索到新的wifi热点时判断该热点是否符合规格*/
    public void onReceiveNewNetworks(List<ScanResult> wifiList) {

        passableHotsPot = new ArrayList<String>();
        for (ScanResult result : wifiList) {

            if ((result.SSID).contains("sf20160427WEC7DE"))
            {
                passableHotsPot.add(result.SSID);

            }


        }
        synchronized (this) {
            connectToHotpot();
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
    protected void onDestroy() {
        super.onDestroy();
        /*销毁时注销广播*/
        unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

     class Listener implements  View.OnClickListener
     {
         @Override
         public void onClick(View v) {
             switch(v.getId())
             {
                 case R.id.button3:
                     wifiManager.startScan();
                     wifiList = wifiManager.getScanResults();
                     if (wifiList == null || wifiList.size() == 0 || isConnected)
                         return;
                     onReceiveNewNetworks(wifiList);
                     break;

                 case R.id.button4:
                   //  mclient.execute(new InetSocketAddress("192.168.43.1",PORT));

                     break;

             }
         }
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



}
