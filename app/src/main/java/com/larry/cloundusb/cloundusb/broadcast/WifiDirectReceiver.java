package com.larry.cloundusb.cloundusb.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


/**
 * Created by Larry on 3/27/2016.
 *
 *
 * Wifidirect 接受广播
 */
public class WifiDirectReceiver extends BroadcastReceiver {

    String action;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    WifiP2pManager.PeerListListener peerListListener;
    Activity mactivity;
    @Override
    public void onReceive(Context context, Intent intent) {



        action=intent.getAction();
        Log.e("显示进入广播", "intent");
      //  JudgeWifiState(intent);

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {


        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {




        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

            if(mManager!=null)
                mManager.requestPeers(mChannel, (WifiP2pManager.PeerListListener)mactivity);
            Log.e("状态改变","change state");
        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {


            Log.e("第四个","第四个");
        }






    }

    public WifiDirectReceiver(WifiP2pManager manager,WifiP2pManager.Channel chanel,Activity activity)
    {
        super();
        mManager=manager;
        mChannel=chanel;
        mactivity=activity;

    }

    public void JudgeWifiState(Intent intent)
    {






    }




}
