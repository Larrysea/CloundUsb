package com.larry.cloundusb.cloundusb.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CommonUtil;

/**
 * Created by Larry on 6/8/2016.
 */
public class InforActivity extends AppCompatActivity {
    Toolbar myToolbar;

    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        //initComponent();
        myToolbar = (Toolbar) findViewById(R.id.infor_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.about_infor));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);
        context=this;


    }

    public void checkVersionUpdate(View view) {


        int flag = 0;
        progressDialog=ProgressDialog.show(context, null, GetContextUtil.getInstance().getString(R.string.tip_checking_version));
        checkVersion();



    }


    /*
    * 监听toolbar的返回事件
    *
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
      //  progressDialog.dismiss();

    }

    Handler handler =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==2)
            {
                progressDialog.dismiss();
                Toast.makeText(InforActivity.this, GetContextUtil.getInstance().getResources().getString(R.string.tip_is_last_version), Toast.LENGTH_SHORT).show();

            }
        }
    };

    public InforActivity() {
        super();
    }


    /*
    *显示dialog
    *
    * */
    public void checkVersion()
    {
        new Thread(new Runnable() {
            int flag;
            @Override
            public void run() {
                for (flag = 0; flag < 3; flag++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag == 2) {
                        handler.sendEmptyMessage(2);

                    }
                }
            }
        }).start();




    }
}


