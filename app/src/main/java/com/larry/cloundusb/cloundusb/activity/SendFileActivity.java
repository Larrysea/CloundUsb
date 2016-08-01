package com.larry.cloundusb.cloundusb.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.larry.cloundusb.R;


import com.larry.cloundusb.cloundusb.broadcast.WifiDirectReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Larry on 3/27/2016.
 * 用于传输文件的的发送还有接收工作
 *
 *
 */

public class SendFileActivity extends AppCompatActivity  implements WifiP2pManager.ChannelListener,WifiP2pManager.PeerListListener{

    WifiP2pManager mWifiManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    Context mContext;
    IntentFilter mFilter;
    List<WifiP2pDevice> mWifiPeers;
    WifiP2pDevice device; //连接设备
    WifiP2pConfig config;  //连接设置
    List<String> peers;
    TextView progressView;  //显示进度textview

    @Override
    public void onCreate(Bundle savedInstancestate)
    {
        super.onCreate(savedInstancestate);
        setContentView(R.layout.activity_send);
        initWifiManager();

    }

    public void initWifiManager()
    {
        peers=new ArrayList<String>();
        mWifiManager=(WifiP2pManager)getSystemService(WIFI_P2P_SERVICE);

        mChannel=mWifiManager.initialize(this,getMainLooper(),null);
        mReceiver=new WifiDirectReceiver(mWifiManager,mChannel,this);
        mFilter=new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
       // progressView=(TextView)findViewById(R.id.progressText);
        progressView.setOnClickListener(new listener());

    }

  /*  @Override
    public void connect(WifiP2pConfig config)
    {

        config.deviceAddress=device.deviceAddress;
        mWifiManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.e("scan failure", "failure");


            }

            @Override
            public void onFailure(int reason) {

                Toast.makeText(SendFileActivity.this, "connect failure", Toast.LENGTH_SHORT).show();
            }
        });



    }*/

    /**
     *
     */
    public void discoveryPeers()
    {



        mWifiManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("scan scuessed","succedss top");
            }

            @Override
            public void onFailure(int reason) {

                if(reason==WifiP2pManager.P2P_UNSUPPORTED)
                    Log.e("scan failure","failure   unsuppoerted");
                else
                  if(reason==WifiP2pManager.ERROR)
                {
                    Log.e("scan failure","failure   error");
                }
                if (reason==WifiP2pManager.BUSY)
                {
                    Log.e("scan failure","failure   busy");
                }


            }
        });
    }




    @Override
    public void onResume()
    {
        super.onResume();
        mReceiver=new WifiDirectReceiver(mWifiManager,mChannel,this);
        registerReceiver(mReceiver, mFilter);



    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }



    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

        Log.e("pees 可以用了","enter the devide");
        Collection<WifiP2pDevice > deviceList=peers.getDeviceList();
        if(deviceList!=null&&deviceList.size()!=0)
        {

            for(int i=0;i<deviceList.size();i++)
                Log.e("显示设备信息", deviceList.toString());
        }



    }

    @Override
    public void onChannelDisconnected() {

    }


    class listener implements View.OnClickListener
  {

      @Override
      public void onClick(View v) {

          Log.e("xianshi dianji ","dianji dianjia");
          discoveryPeers();

      }
  }






}
