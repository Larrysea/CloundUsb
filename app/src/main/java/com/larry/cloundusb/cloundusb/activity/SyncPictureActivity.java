package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.SyncPitctureUtil;
import com.larry.cloundusb.cloundusb.Interneutil.WifiAdmin;
import com.larry.cloundusb.cloundusb.Interneutil.WifiUtil;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.view.RoundPB;

import java.util.Hashtable;


/**
 * Created by LARRYSEA on 2016/7/18.
 * 同步照片的activity  展示一个二维码  二维码中的信息是wifi信息
 * <p/>
 * calltype  如果为一则代表是照片调用
 * <p/>
 * 如果为二则代表是创建wifi调用
 */
public class SyncPictureActivity extends AppCompatActivity {

    final static String wifiPassword = "123456789";         //创建的wifi密码
    static String wifiName;                                 //创建的热点名称
    ImageView imageView;                                   //显示二维码的iv
    Toolbar toolbar;
    boolean wifiIsOpen = false;                            //wifi是否已经打开
    RoundPB roundPB;                                      //圆形进度条
    int progress;                                         //进度
    LinearLayout qrLinearLayout;                           //  展示二维码的布局
    SyncPictureActivity msyncPictureActivity;
    int recentHours;                                       // 同步照片范围
    Button startButton;
    TextView activity_sync_picture_tv;
    Intent mintent;                                         //使用syncprogressactivity
    SyncPitctureUtil msyncUtil;                            //更新照片工具类
    WifiAdmin wifiAdmin;                                   //wifiadmin工具
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    roundPB.setVisibility(View.GONE);
                    qrLinearLayout.setVisibility(View.VISIBLE);
                    if (getIntent().getIntExtra("call_type", 2) == 2) {
                        startButton.setText("开始发送");
                        startButton.setVisibility(View.VISIBLE);
                    }
                    activity_sync_picture_tv.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //扫码发送文件的activity
                            if (startButton.getText().equals("开始发送")) {
                                Intent sendProGressIntent = new Intent(GetContextUtil.getInstance(), SendProgressActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("type", 1);
                                sendProGressIntent.putExtra("type", 1);
                                sendProGressIntent.putExtras(bundle);
                                startActivity(sendProGressIntent);
                            }
                            //同步照片的activity
                            else {
                                msyncPictureActivity.finish();
                                mintent = new Intent(new Intent(SyncPictureActivity.this, SyncProgressActivity.class));
                                mintent.putExtra("call_type", 2);
                                mintent.putExtra("recent_hours", recentHours);
                                startActivity(mintent);
                            }

                        }
                    });
                    try {
                         imageView.setImageBitmap(create2DCode(wifiName + "]http://www.aceclound.cn/aceclound/apk/android.apkhttp://www.aceclound.cn/aceclound/apk/android.apk", CommonUtil.getScreenSizeOfDevice2(msyncPictureActivity)));  //设置二维码

                    } catch (com.google.zxing.WriterException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    /*
    *
    * parm 1 屏幕手机大小
    *parm 2 手机屏幕尺寸
    *
    *
    * */

    public static Bitmap create2DCode(String text, int phoneSize) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix matrix;
        if (phoneSize < 5) {
            matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 250, 250, hints);
        } else {
            matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 400, 400, hints);

        }

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_picture);
        recentHours = getIntent().getIntExtra("recent_hours", 0);
        wifiAdmin = new WifiAdmin(this);
        wifiName = "TaA" + wifiAdmin.getLastThreMac() + CommonUtil.getPhoneName() + "]" + recentHours;
        initComponent();
        msyncPictureActivity = this;


    }

    /*
    * 监听toolbar的返回事件
    *
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 初始化组件
    *
    *
    * */
    public void initComponent() {

        msyncUtil = new SyncPitctureUtil(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiechuan/测试/", 2, handler);//手机内置存储卡的位置);
        MainActivity.sexecutorService.submit(msyncUtil);
        activity_sync_picture_tv = (TextView) findViewById(R.id.activity_sync_picture_tv);
        startButton = (Button) findViewById(R.id.activity_sync_start_btn);

        imageView = (ImageView) findViewById(R.id.activity_sync_imageview);
        toolbar = (Toolbar) findViewById(R.id.sync_picture_activity_toolbar);
        toolbar.setTitle(GetContextUtil.getInstance().getString(R.string.scan_qr_sync));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        roundPB = (RoundPB) findViewById(R.id.activity_sync_picture_round_pb);
        qrLinearLayout = (LinearLayout) findViewById(R.id.qr_activity_linearlayout);
        setSupportActionBar(toolbar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                WifiUtil.openWifi(SyncPictureActivity.this, wifiName, wifiPassword);
                while (!wifiIsOpen) {
                    progress += 5;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    roundPB.setProgress(progress);
                    if (WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())) {
                        roundPB.setProgress(100);
                        handler.sendEmptyMessage(1);
                        break;
                    }
                }
            }
        }).start();


      /*  UdpReceive receive = new UdpReceive();
        MainActivity.sexecutorService.execute(receive);*/

    }


}
