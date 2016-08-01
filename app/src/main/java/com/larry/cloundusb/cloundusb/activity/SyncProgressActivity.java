package com.larry.cloundusb.cloundusb.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.widgets.ProgressDialog;


import com.gc.materialdesign.widgets.SnackBar;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.SyncPitctureUtil;
import com.larry.cloundusb.cloundusb.adapter.SyncProgressAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LARRYSEA on 2016/7/19.
 * <p/>
 * 同步照片activity
 */
public class SyncProgressActivity extends AppCompatActivity implements SyncPitctureUtil.updateProgressInterface {

    RecyclerView recyclerview;                              //同步的recyclerview
    SyncProgressAdapter msyncProgrssAdapter;                //图片视频适配器
    ProgressDialog progressDialog;
    int recentHours;                                        //最近几个小时以内的图片
    Snackbar snackBar;
    final static String TAG = "syncprogressactivity";       //开始同步
    SyncProgressActivity syncProgressActivity;
    Toolbar myToolbar;
    SyncPitctureUtil syncPitctureUtil;                      //更新图片的线程工具
    SyncProgressAdapter.viewHolder     mviewHolder;         //子view视图的holder
    View childView;                                         //子视图
    boolean   updateFlag=true;                              //更新progressbar的子线程是否存活
    int type;                                               //调用类型  1代表客户端调用  2 代表服务端调用
    Bitmap contentBitmap;                                   //内容bitmap
    List<Bitmap> tempbitmapList;                            //暂时性的bitmaplist
    Boolean isRunnable=false;                               //更新 progressabar线程是否在运行
    double percent;                                         //精度百分比


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_picture_progress);
        type=this.getIntent().getIntExtra("call_type",1);
        initComponent();

    }

    public void initComponent() {
        tempbitmapList=new ArrayList<Bitmap>();
        myToolbar = (Toolbar) findViewById(R.id.sync_picture_progress_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.sync_picture));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);
        syncProgressActivity = this;
        progressDialog = new ProgressDialog(this, "正在查找图片");
        progressDialog.show();
        recentHours=getIntent().getIntExtra("recent_hours",0);
        MultiMediaUtil.scanImage(recentHours, handler);
        recyclerview = (RecyclerView) findViewById(R.id.sync_picture_recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));



    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    msyncProgrssAdapter = new SyncProgressAdapter(MultiMediaUtil.recentPictureList,tempbitmapList,GetContextUtil.getInstance());
                    recyclerview.setAdapter(msyncProgrssAdapter);
                    syncPitctureUtil=new SyncPitctureUtil("测试地点",1,handler);
                    syncPitctureUtil.setUpdateInterface(SyncProgressActivity.this);
                    progressDialog.dismiss();
                    snackBar=Snackbar.make(getWindow().getDecorView(),"开始同步",Snackbar.LENGTH_INDEFINITE);
                    snackBar.setAction("确认",new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            snackBar.dismiss();
                            MainActivity.sexecutorService.submit(syncPitctureUtil);

                        }
                    });
                    snackBar.show();
                    break;
                case 2:
                    getBitmap(handler);
                    break;
                case 3:
                    mviewHolder.squareProgressBar.setProgress(percent);
                    break;
            }
        }
    };


    /*
   * 监听toolbar的返回事件
   *
   * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /*
    *
    * 回调更新progressbar接口
    *
    *
    * 接口调用在syncpictureutile中
    *
    *
    * */
    @Override
    public void updateSqurebar(int position) {

        childView=recyclerview.getChildAt(position);
        if(childView!=null)
        {
            recyclerview.getChildViewHolder(childView);
            if(recyclerview.getChildViewHolder(childView)!=null)
            {
                mviewHolder=(SyncProgressAdapter.viewHolder)recyclerview.getChildViewHolder(childView);
            }
        }
        if(isRunnable==false)
        {
            mrunnable.run();
        }


    }


    public void getBitmap(final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bmp = Bitmap.createBitmap(250, 250, Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bmp);
                Paint paint = new Paint();
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.parseColor("#D11111"));
                paint.setTextSize(100);
                for (int position = 0; position < MultiMediaUtil.recentPictureList.size(); position++) {
                    contentBitmap = ImageLoader.getInstance().loadImageSync("file://" + MultiMediaUtil.recentPictureList.get(position),new ImageSize(125,125));
                 if(contentBitmap!=null)
                 {
                     canvas.drawBitmap(contentBitmap, 0, 0, paint);
                     contentBitmap=GraphicsUtil.zoomImage(contentBitmap,250,250);
                     tempbitmapList.add(contentBitmap);
                 }
                }
                handler.sendEmptyMessage(1);  //发送消息通知已经处理完毕
            }
        }).start();


    }

    Runnable mrunnable=new Runnable() {
        @Override
        public void run() {

            isRunnable=true;
            while(updateFlag)
            {
               if(mviewHolder!=null)
               {
                   if(mviewHolder.squareProgressBar!=null)
                   {
                       percent=100*(SyncPitctureUtil.pictureSendLength/(double)SyncPitctureUtil.pictureTotalLength);
                       Looper.prepare();
                       handler.sendEmptyMessage(3);
                       Looper.loop();
                       if(percent==100)
                       {
                           try{
                             Thread.sleep(4000);
                           }catch(InterruptedException e)
                           {
                               Log.e(TAG,e.getMessage());

                           }
                       }
                   }
               }
                try{
                    Thread.sleep(200);
                }catch(InterruptedException e)
                {
                    Log.e(TAG,e.getMessage());

                }

            }


        }
    };


}
