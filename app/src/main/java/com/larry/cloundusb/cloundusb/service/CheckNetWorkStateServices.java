package com.larry.cloundusb.cloundusb.service;

import android.app.Service;
import android.content.Intent;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.Interneutil.ScanWifi;
import com.larry.cloundusb.cloundusb.Interneutil.TcpServer;
import com.larry.cloundusb.cloundusb.Interneutil.UdpFind;
import com.larry.cloundusb.cloundusb.Interneutil.UdpReceive;
import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.listener.ConnectionListener;

/**
 * Created by Larry on 5/3/2016.
 * <p/>
 * 用于检查网络连接状况的服务
 * 用于计算所处环境的网络状况，来作出网络连接的选择是否创建wifi热点还是用本地网络
 */
public class CheckNetWorkStateServices extends Service {

    final static int UPDATE_UI = 1;
    public static TcpServer tcpServer;
    public static ScanWifi scanWifi;
    static updateUi mupdateUi;
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI:
                    if (mupdateUi != null) {
                        Log.e("根性uo", "ududu");
                        mupdateUi.updateUi(MultiMediaUtil.getAmount());
                    }
                    break;

            }
        }
    };
    ConnectionListener connectionListener;
    boolean SCAN_OK = false;

    /*
    * 更新ui接口
    *
    * */
    static public void setUpdateUi(updateUi parm) {
        mupdateUi = parm;
    }

    static public Handler getHandler() {
        return handler;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    * 整个应用的下相关初始化工作
    *
    *
    * */
    @Override
    public void onCreate() {
        super.onCreate();
        connectionListener = new ConnectionListener();
        ConnectionClassManager.getInstance().register(connectionListener);
        CheckNetWork();//检查网络状况然后作出判断是否创建wifi
        if(MainActivity.sexecutorService!=null)
        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                if (FileUtil.checkSDCard())
                    MultiMediaUtil.initSendInformList();
                    MultiMediaUtil.getTypeFile(Environment.getExternalStorageDirectory());
                if (mupdateUi != null) {
                    Log.e("根性uo", "ududu");
                    mupdateUi.updateUi(MultiMediaUtil.getAmount());
                }

            }
        });  //搜扫描手机中的文件


    }

    /*
         *
         * 用于判断网络是否阻塞
         * 如果阻塞则创建热点，如果未阻塞则使用本地网络
         *
         * * */
    public void CheckNetWork()  {
        new Thread(new Runnable() {
            @Override
            public void run() {


                ConnectionQuality quality = InternetTool.getNetWorkState(MainActivity.sexecutorService, getBaseContext());
                if (quality == ConnectionQuality.UNKNOWN) {
                    quality = InternetTool.getNetWorkState(MainActivity.sexecutorService, getBaseContext());

                }
                //  if(quality!=null&&quality!= ConnectionQuality.POOR&&quality!=ConnectionQuality.UNKNOWN)
                if (true) {


                    if(MainActivity.sexecutorService!=null)
                    {
                        scanWifi=new ScanWifi();
                        MainActivity.sexecutorService.submit(scanWifi);

                        UdpFind find = new UdpFind();
                        MainActivity.sexecutorService.execute(find);

                        UdpReceive receive = new UdpReceive();
                        MainActivity.sexecutorService.execute(receive);

                        tcpServer = new TcpServer();
                        MainActivity.sexecutorService.submit(tcpServer);
                    }




                } else {
                    try {
                        Log.e("网络质量不好", "hah ");
                        //   HotSpotUtil.initHostSpotConfig(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConnectionClassManager.getInstance().remove(connectionListener);

    }


    public interface updateUi {
        void updateUi(int amount[]);
    }


}
