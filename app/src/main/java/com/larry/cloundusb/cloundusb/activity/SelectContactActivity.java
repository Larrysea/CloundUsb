package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.TcpClient;
import com.larry.cloundusb.cloundusb.adapter.SelectContactItemAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Larry on 5/14/2016.
 * <p>
 * 选择发送人的activity
 */

public class SelectContactActivity extends AppCompatActivity {


    RecyclerView mrecyclerView;//展示的选择联系人recyclerview
    SelectContactItemAdapter adapter;
    final static int PORT = 4079;
    final static String TAG = SelectContactActivity.class.toString();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact);
        init();

    }


    /*
    *
    * 初始化的信息
    * */

    public void init() {
        mrecyclerView = (RecyclerView) findViewById(R.id.select_contact_recyclerview);

        final ArrayList<SendContactInfo> contactInforList = getIntent().getParcelableArrayListExtra("contactInfor");
        adapter = new SelectContactItemAdapter(contactInforList);

        mrecyclerView.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        mrecyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL_LIST));
        mrecyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new SelectContactItemAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainActivity.tcpClient=new TcpClient();
                try {
                    MainActivity.tcpClient.initInetAddress(InetAddress.getByName(contactInforList.get(position).getIpAddress()));

                } catch (UnknownHostException e) {
                    Log.e(TAG, e.getMessage());
                }
                //  MainActivity.sexecutorService.submit(tcpClient);
                Intent intent = new Intent(SelectContactActivity.this, SendProgressActivity.class);
                Bundle bundle = new Bundle();
                //发送方调用1
                bundle.putInt("type", 1);
                intent.putExtras(bundle);
                SelectContactActivity.this.startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
    }


}
