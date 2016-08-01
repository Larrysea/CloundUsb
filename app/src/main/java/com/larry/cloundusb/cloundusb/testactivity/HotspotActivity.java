package com.larry.cloundusb.cloundusb.testactivity;

/**
 * Created by Larry on 4/3/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.TcpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HotspotActivity extends Activity {
    WifiManager wifiManager;
    private Button open;
    Button clientButton;//客户断按钮
    private boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //获取wifi管理服务
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        open = (Button) findViewById(R.id.button);
        clientButton = (Button) findViewById(R.id.button2);
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ClientActivity.class));


            }
        });
        //通过按钮事件设置热点
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是打开状态就关闭，如果是关闭就打开
                flag = !flag;
                setWifiApEnabled(flag);
            }
        });

    }

    // wifi热点开关
    public boolean setWifiApEnabled(boolean enabled) {
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

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        // setWifiApEnabled(false);


    }
    /*
    * 初始化分享热点的相关信息
    *
    *
    * */

    public boolean initHostSpotConfig(boolean enabled) throws IllegalAccessException, InvocationTargetException {


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

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        netConfig.SSID = "sf20160427WEC7DE";
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

        TcpServer serverSocket = new TcpServer();


        return (Boolean) method.invoke(wifiManager, netConfig, enabled);


    }


}