package com.larry.cloundusb.cloundusb.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.UsbItemFragment;

import java.io.File;

/**
 * Created by Larry on 6/7/2016.
 * <p/>
 * 产看U盘文件的activity
 */
public class CheckUsbActivity extends AppCompatActivity {

    TextView imageTv;
    TextView musicTv;
    TextView videoTv;
    TextView documentTv;
    TextView otherTv;
    final static String TAG = "CheckUsbActivity";
    Toolbar myToolbar;
    LinearLayout linearlayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     /*   if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();

        }*/
        linearlayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_check_usb);
        initComponent();
    }

    /*
    * 初始化组件信息
    *
    * */
    public void initComponent() {

        linearlayout = (LinearLayout) findViewById(R.id.chck_usb_linearlayout);
        myToolbar = (Toolbar) findViewById(R.id.check_usb_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.clound_usb));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);

        imageTv = (TextView) findViewById(R.id.check_usb_image_size_textview);
        musicTv = (TextView) findViewById(R.id.check_usb_music_size_textview);
        videoTv = (TextView) findViewById(R.id.check_usb_video_size_textview);
        documentTv = (TextView) findViewById(R.id.check_usb_document_size_textview);
        otherTv = (TextView) findViewById(R.id.check_usb_document_size_textview);
        updateUsbItemSize();
        updateData();//更新每个文件类型的数据信息


    }


    public void check_image(View view) {
        linearlayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0);
        UsbItemFragment usbItemFragment = new UsbItemFragment();
        usbItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_usb_container, usbItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        updateUsbItemSize();
        Log.e(TAG, "点击了image");


    }

    public void check_music(View view) {
        linearlayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        UsbItemFragment usbItemFragment = new UsbItemFragment();
        usbItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_usb_container, usbItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        updateUsbItemSize();

    }

    public void check_document(View view) {
        linearlayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        UsbItemFragment usbItemFragment = new UsbItemFragment();
        usbItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_usb_container, usbItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        updateUsbItemSize();

    }

    public void check_video(View view) {
        linearlayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 2);
        UsbItemFragment usbItemFragment = new UsbItemFragment();
        usbItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_usb_container, usbItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        updateUsbItemSize();

    }


    public void check_other(View view) {
        linearlayout.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        UsbItemFragment usbItemFragment = new UsbItemFragment();
        usbItemFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.check_usb_container, usbItemFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        updateUsbItemSize();


    }


    public void check_local_item(View view) {
        linearlayout.setVisibility(View.GONE);


    }


    /*
    * 监听toolbar的返回事件
    *
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            linearlayout.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 更新usbitem的数据
    *
    *
    * */
    public void updateUsbItemSize() {


        if (FileBox.getInstance().getUsbImageList() != null) {
            imageTv.setText(String.valueOf(FileBox.getInstance().getUsbImageList().size()));
        }
        if (FileBox.getInstance().getUsbMusicList() != null) {
            musicTv.setText(String.valueOf(FileBox.getInstance().getUsbMusicList().size()));

        }
        if (FileBox.getInstance().getUsbVideoList() != null) {
            videoTv.setText(String.valueOf(FileBox.getInstance().getUsbVideoList().size()));
        }
        if (FileBox.getInstance().getUsbDocumentList() != null) {
            documentTv.setText(String.valueOf(FileBox.getInstance().getUsbDocumentList().size()));
        }
        if (FileBox.getInstance().getUsbotherList() != null) {
            otherTv.setText(String.valueOf(FileBox.getInstance().getUsbotherList().size()));
        }
    }

    /*
    *
    * 更新界面
    *
    *
    * */
    public void updateData() {

        new Thread(new Runnable() {
            int waitTime = 0;//等待时间

            @Override
            public void run() {
                while (waitTime < 2) {
                    waitTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }

                }
              handler.sendEmptyMessage(1);
            }
        }).start();


    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 1:
                    updateUsbItemSize();
                    break;
            }


        }
    };

}
