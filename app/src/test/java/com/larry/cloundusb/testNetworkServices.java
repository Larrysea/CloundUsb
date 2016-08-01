package com.larry.cloundusb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.listener.ConnectionListener;

/**
 * Created by Larry on 5/11/2016.
 *
 *
 *
 * 测试facebook 网络的质量的代码
 */
public class testNetworkServices extends Activity {

    ConnectionListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        listener=new ConnectionListener();
        ConnectionQuality quality=InternetTool.getNetWorkState(MainActivity.sexecutorService,getBaseContext());
        Log.e("显示网络质量",quality+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionClassManager.getInstance().register(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionClassManager.getInstance().remove(listener);
    }
}
