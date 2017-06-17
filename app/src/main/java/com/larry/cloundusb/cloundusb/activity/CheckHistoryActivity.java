package com.larry.cloundusb.cloundusb.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.database.DB_AceClound;
import com.larry.cloundusb.cloundusb.fragment.ReceiveHistoryFragment;
import com.larry.cloundusb.cloundusb.fragment.SendHistoryFragment;

/**
 * Created by LARRYSEA on 2016/6/21.
 */
public class CheckHistoryActivity extends AppCompatActivity {


    SendHistoryFragment sendHistoryFragment;          //发送的fragment
    Toolbar myToolbar;
    FragmentTransaction fragmentTransaction;
    ReceiveHistoryFragment receiveHistoryFragment;    //显示接收的fragment
    Button sendButton;                                 //发送按钮
    Button receiveButton;                              //接收按钮
    clickListener mclickListener;                      //监听器

    @Override
    public void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_check_history);
        initComponent();

    }

    /*
    *
    *初始化组件
    *
    * */
    public void initComponent() {
        myToolbar = (Toolbar) findViewById(R.id.check_history_toolbar);
        receiveButton = (Button) findViewById(R.id.activity_check_receive_button);
        sendButton = (Button) findViewById(R.id.activity_check_send_button);
        receiveButton.setBackgroundResource(R.drawable.check_history_left_circle_button);
        receiveButton.setTextColor(getResources().getColor(R.color.white));
        mclickListener = new clickListener();
        receiveButton.setOnClickListener(mclickListener);
        sendButton.setOnClickListener(mclickListener);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.chech_history));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);
        receiveButton.setSelected(true);
        receiveHistoryFragment = new ReceiveHistoryFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_check_history_container, receiveHistoryFragment);
        fragmentTransaction.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.delete_history_item) {
            DB_AceClound db_aceClound = new DB_AceClound(GetContextUtil.getInstance());
            db_aceClound.ClearAllInfo();

        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.activity_check_send_button:
                    sendButton.setBackgroundResource(R.drawable.check_history_right_blue_button);
                    receiveButton.setBackgroundResource(R.drawable.check_history_left_white_button);
                    receiveButton.setTextColor(getResources().getColor(R.color.blue));
                    sendButton.setTextColor(getResources().getColor(R.color.white));
                    if (sendHistoryFragment == null) {
                        sendHistoryFragment = new SendHistoryFragment();
                    }
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.activity_check_history_container, sendHistoryFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.activity_check_receive_button:
                    sendButton.setBackgroundResource(R.drawable.check_history_right_white_button);
                    receiveButton.setBackgroundResource(R.drawable.check_history_left_circle_button);
                    receiveButton.setTextColor(getResources().getColor(R.color.white));
                    sendButton.setTextColor(getResources().getColor(R.color.blue));
                    if (receiveHistoryFragment == null) {
                        receiveHistoryFragment = new ReceiveHistoryFragment();
                    }
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.activity_check_history_container, receiveHistoryFragment);
                    fragmentTransaction.commit();
                    break;
            }


        }
    }


}
