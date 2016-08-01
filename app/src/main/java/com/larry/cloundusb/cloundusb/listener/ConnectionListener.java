package com.larry.cloundusb.cloundusb.listener;

import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;

/**
 * Created by Larry on 5/6/2016.
 */
public class ConnectionListener implements ConnectionClassManager.ConnectionClassStateChangeListener{


    @Override
    public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
        Log.e("显示实时网速",bandwidthState+"");
    }
}
