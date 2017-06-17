package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.Interneutil.TcpClient;
import com.larry.cloundusb.cloundusb.Interneutil.TcpServer;
import com.larry.cloundusb.cloundusb.Interneutil.UdpReceive;
import com.larry.cloundusb.cloundusb.Interneutil.WifiUtil;
import com.larry.cloundusb.cloundusb.adapter.SendProgressAdapter;
import com.larry.cloundusb.cloundusb.appinterface.recyclerviewClickListener;
import com.larry.cloundusb.cloundusb.appinterface.updateProgressbarInterface;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fragment.ApkFragment;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.service.CheckNetWorkStateServices;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Larry on 5/1/2016.
 * 显示发送的进度activity
 */
public class SendProgressActivity extends AppCompatActivity implements ParentFragment.addFileInterFace, MainActivity.closeSendProgressActivity, TcpClient.updateProgressbar {
    final static String TAG = "send progress activity";
    public static boolean IsAlreadyExist = false;                    //activity已经存在
    static RecyclerView recyclerView;
    static LinkedList<SendFileInform> linkedList;                   //需要发送文件的链表
    static SendProgressAdapter adapter;                             //适配器
    static SendProgressAdapter.viewHolder viewHolder;
    static boolean isFirstUpdate = false;
    static int percent = 0;
    static int finalposition = -1;
    static long progressMax;
    static View view;
    static startReceiveIn mstartReceive;                           //开始接受文件的回调接口
    static int oldFileLength;                                      //在未二次添加文件时的文件长度
    updateProgressbarInterface updateface;
    boolean isLife = true;                                         //是否在发送
    int sourcceType;                                               //标志位到底是发送方调用activity还是接受方调用activity  发送方调用为1接收方调用为2
    Toolbar toolbar;
    TextView speedTv;
    String[] NetSpeedResult;                                        //网络速度result
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 2:
                    speedTv.setText(NetSpeedResult[0] + " MB/s 剩余 " + NetSpeedResult[1] + " s");
                    break;
                case 3:
                    speedTv.setText("发送成功");

                    break;
                case 4:
                    viewHolder.openTv.setVisibility(View.VISIBLE);
                    speedTv.setText("接收成功");
                    break;

            }
        }
    };
    List<SendFileInform> adapterList;//保存适配器的数据集
    TextView withdrawtv;//    撤回显示的textview

    static public void setStartReceive(startReceiveIn startReceive) {
        mstartReceive = startReceive;
    }




    /*
    * 更新progressbar
    *
    * */

    @Override
    public void onCreate(Bundle savedinstance) {
        super.onCreate(savedinstance);
        // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send);
        initCompanet();
        Log.e(TAG, "显示静茹了send pro oncereate");

    }

    /*
    * 初始化组件
    *
    * */
    public void initCompanet() {
        ParentFragment.setAddFileInterFace(this);
        MainActivity.setCloseInterface(this);
        TcpClient.setUpdateProgressbar(this);
        IsAlreadyExist = true;
        recyclerView = (RecyclerView) findViewById(R.id.send_recyclerview);
        withdrawtv = (TextView) findViewById(R.id.with_draw_tv);
        toolbar = (Toolbar) findViewById(R.id.send_progressbr_toolbar);
        toolbar.setTitle(GetContextUtil.getInstance().getString(R.string.send_progress));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(toolbar);
        speedTv = (TextView) findViewById(R.id.send_activity_toolbar_tv);
        judgeFrom(getIntent().getExtras().getInt("type"));
        if (sourcceType == 1 || sourcceType == 3) {
            withdrawtv.setVisibility(View.VISIBLE);
            if (sourcceType == 1 && UdpReceive.getSendContactInforList().size() > 1) {
                withdrawtv.setText(GetContextUtil.getInstance().getString(R.string.send_to) + UdpReceive.getSendContactInforList().get(0).getName() + " " +
                        GetContextUtil.getInstance().getString(R.string.click_cancel));
            }
        }

        withdrawtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.tcpClient != null) {
                    MainActivity.tcpClient.getHandler().sendEmptyMessage(4);
                    withdrawtv.setVisibility(View.GONE);
                    isLife = false;

                }

            }
        });

        NetSpeedResult = new String[2];
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL_LIST));
        adapter.setRecyclerListener(new recyclerviewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FileUtil.openFile(new File(adapterList.get(position).getPath()));

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        updatePorgressBar();
        updateface = new updateProgressbarInterface() {
            @Override
            public void updateProgressbar(int position, long progress, long max) {
                view = recyclerView.getChildAt(position);
                if (view != null) {
                    if (recyclerView.getChildViewHolder(view) != null) {
                        {
                            viewHolder = (SendProgressAdapter.viewHolder) recyclerView.getChildViewHolder(view);
                        }
                    }

                }


            }

            @Override
            public void addFile(int startPosition) {
                adapter.notifyDataSetChanged();
            }
        };
        TcpServer.setUpdateInterface(updateface);   //设置server进度条的更新接口
        //tcpServer是否连接
        if (!TcpServer.CONNECTED) {
            //tcp 客户端还不存在
            if (!MainActivity.tcpClient.IS_ALREADYEXIST) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "运行tcp client");
                        oldFileLength = FileBox.getInstance().getSendListSize();
                        MainActivity.tcpClient = new TcpClient();
                        //代表自己创建热点的情况
                        if (WifiUtil.readClientList().size() > 0) {
                            MainActivity.tcpClient.setConnecteIpFrom(4);
                        }
                        //代表来自局域网的情况
                        else {
                            MainActivity.tcpClient.setConnecteIpFrom(2);
                        }
                        MainActivity.sexecutorService.submit(MainActivity.tcpClient);
                    }
                }).start();
                TcpClient.setUpdateInterFace(updateface);

            }
            //tcpClient已经存在
            else {
                if (MainActivity.tcpClient != null && MainActivity.tcpClient.IS_ALREADYEXIST) {
                    if (mstartReceive != null) {
                        mstartReceive.addFile(oldFileLength - 1);
                    }
                }
               /* Message msg = new Message();
                msg.what = 2;
                MainActivity.tcpClient.getHandler().sendMessage(msg);*/
            }

        } else {
            Message msg = new Message();
            msg.what = 3;
            CheckNetWorkStateServices.tcpServer.gethandler().sendMessage(msg);
        }


        TcpClient.setUpdateInterFace(updateface);//设置client端的进度条更新接口
        if (sourcceType == 2) {
            if (mstartReceive != null)
                mstartReceive.receive(finalposition);
        }


    }

    /*
    * 判断来自于客户端还是服务端
    *
    * 客户端  标志位2
    *
    * 服务端  标志位1
    * */
    public void judgeFrom(int type) {
        adapterList = new ArrayList<SendFileInform>();

        if (type == 1) {
            adapterList = FileBox.getSendList();
            sourcceType = 1;
        } else if (type == 2) {
            adapterList = FileBox.getReceiveList();
            sourcceType = 2;
        } else if (type == 3) {
            adapterList = FileBox.getSendList();
            sourcceType = 3;
        }
        //创建热点并且是发送方
        else if (type == 4) {
            adapterList = FileBox.getSendList();
            sourcceType = 1;
        }
        adapter = new SendProgressAdapter(GetContextUtil.getInstance(), adapterList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ApkFragment.handler.sendEmptyMessage(2);


    }


    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // updatePorgressBar();


    }

    @Override
    public void addFile(int startPosition) {

        adapter.notifyDataSetChanged();
        mstartReceive.addFile(startPosition);
        oldFileLength = startPosition;

    }

    @Override
    public void close() {


        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_progress_menu, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        IsAlreadyExist = false;

    }

    /*
    *
    * 更新精度条的函数
    *
    *
    * */
    public void updatePorgressBar() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isLife) {
                    if (sourcceType == 1) {
                        percent = (int) (100 * (TcpClient.fileSendLength / (double) (TcpClient.fileLength)));
                        if (viewHolder != null) {
                            viewHolder.progressBar.setProgress(percent);
                            NetSpeedResult = InternetTool.getDownloadSpeed(TcpClient.fileSendLength, TcpClient.FileSendTime, TcpClient.fileLength);
                            if (speedTv != null) {
                                if (percent < 100) {
                                    handler.sendEmptyMessage(2);
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (percent == 100) {
                                handler.sendEmptyMessage(3);
                            }

                        }

                    } else if (sourcceType == 2) {
                        percent = (int) (100 * (TcpServer.fileReceiveLength / (double) (TcpServer.fileLength)));
                        if (viewHolder != null) {
                            viewHolder.progressBar.setProgress(percent);
                            NetSpeedResult = InternetTool.getDownloadSpeed(TcpServer.fileReceiveLength, FileUtil.FileReceiveTime, TcpServer.fileLength);
                            if (speedTv != null) {
                                if (percent < 100) {
                                    handler.sendEmptyMessage(2);
                                }
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {

                                }
                            }
                            if (percent == 100) {
                                handler.sendEmptyMessage(4);
                            }
                        }


                    }

                }


            }
        }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(this, MainActivity.class));
            this.overridePendingTransition(0, R.anim.activity_close_anim);
            return false;
        }
        return false;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateNextProgressbar() {
        updatePorgressBar();

    }


    public interface startReceiveIn   //开始接受文件的回调接口   添加文件的的回调接口
    {
        void receive(int filePosition);

        void addFile(int startPosition);    //添加文件,startposition 文件开始位置，  filecounts 有多少个新添加文件    在tcpclient中实现
    }


}
