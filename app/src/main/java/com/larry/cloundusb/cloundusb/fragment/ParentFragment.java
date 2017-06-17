package com.larry.cloundusb.cloundusb.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.InternetTool;
import com.larry.cloundusb.cloundusb.Interneutil.UdpReceive;
import com.larry.cloundusb.cloundusb.Interneutil.WifiUtil;
import com.larry.cloundusb.cloundusb.activity.SelectContactActivity;
import com.larry.cloundusb.cloundusb.activity.SendProgressActivity;
import com.larry.cloundusb.cloundusb.activity.SyncPictureActivity;
import com.larry.cloundusb.cloundusb.adapter.FragmentPagerAdapter;
import com.larry.cloundusb.cloundusb.adapter.PictureGroupAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.ImageBean;
import com.larry.cloundusb.cloundusb.baseclass.SendContactInfo;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.util.SlidingTabLayout;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action0;
import rx.functions.Action1;


/**
 * Created by Larry on 3/15/2016.
 * <p/>
 * 外层框架
 */
public class ParentFragment extends Fragment {
    static final int REPLACEFLAG = 2;//替换图片的fragment  变成子的fragment
    static final int PORT = 8779;
    static ViewPager mViewpager;
    static TextView countTextView;   //用于计数的textview
    static ButtonFloat buttonFloat;
    static List<ImageBean> mcheckImageBean;  //被选中的imagebean
    static PictureGroupAdapter mpictureAdapter;
    String wifiName; //wifi热点名称
    String wifiPassword;//wifi热点密码
    Intent sendProGressIntent;  //发送文件进度的intent
    static addFileInterFace maddFileInterface;
    int oldFileLength;       //旧的文件长度
    final static String TAG = ParentFragment.class.toString();

    public static Handler handler = new Handler(GetContextUtil.getInstance().getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PictureFragment.SCAN_oK:
                    Bundle bundle = msg.getData();
                    mpictureAdapter = (PictureGroupAdapter) bundle.getSerializable("adapter");
                    mpictureAdapter.setUpdateView(new PictureGroupAdapter.updateView() {
                        @Override
                        public void updateView(ImageBean imageBean, int size) {
                            mcheckImageBean.add(imageBean);
                            updateCountTextView(size);
                        }
                    });
                    break;
                case REPLACEFLAG:
                    updateCountTextView(0);
                    break;


            }
        }
    };
    static PictureFragment pictureFragment;
    static ParentFragment instance;//返回唯一的实例
    static FragmentManager fragmentManager;
    SlidingTabLayout mTabLayout;
    List<Fragment> list = new ArrayList<Fragment>();
    FragmentPagerAdapter fragmentadapter;//用于fragment页面的适配器
    RelativeLayout parent_relativelayout;  //父类布局

    static public ParentFragment getInstance() {
        return instance = new ParentFragment();

    }

    /*
    * 更新count textview的函数
    *
    * */
    static public void updateCountTextView(int size) {

        int count = FileBox.getInstance().getSendListSize();

        if (count == 0) {
            countTextView.setVisibility(View.GONE);
        } else {
            if (count > 10) {

                countTextView.setTextSize(8);
            } else if (count >= 100) {
                countTextView.setTextSize(4);
            }
            countTextView.setVisibility(View.VISIBLE);
            countTextView.setText(String.valueOf(count));
            countTextView.bringToFront();
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity != null) {
                //  pictureFragment=getActivity().getFragmentManager().findFragmentById(R.id.recyclerviewPicture);
            }
        } catch (ClassCastException e) {

            e.printStackTrace();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        return inflater.inflate(R.layout.file_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        init(view);

    }

    public void init(View view)//初始化各种组件适配器
    {

        mViewpager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        buttonFloat = (ButtonFloat) view.findViewById(R.id.button_float);
        countTextView = (TextView) view.findViewById(R.id.count_textView);
        parent_relativelayout = (RelativeLayout) view.findViewById(R.id.parent_relativeLayout);
        countTextView.bringToFront();
        buttonFloat.bringToFront();
        parent_relativelayout.bringToFront();
        mcheckImageBean = new ArrayList<ImageBean>();
        list.add(new ApkFragment());
        list.add(new PictureFragment());
        list.add(new VideoFragment());
        list.add(new MusicFragment());
        list.add(new ChooseFileFragment());
        fragmentadapter = new FragmentPagerAdapter(getFragmentManager(), list);
        mViewpager.setAdapter(fragmentadapter);
        mTabLayout.setViewPager(mViewpager);
        final int[] location = new int[2];
        buttonFloat.post(new Runnable() {
            @Override
            public void run() {
                buttonFloat.getLocationOnScreen(location);

            }
        });
        buttonFloat.setOnClickListener(new sendClickListener());
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.picture_group_item, null, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.picture_fragment_imageview);
        imageView.measure(w, h);


    }

    class sendClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (InternetTool.JudgeWifiState(GetContextUtil.getInstance()) || WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())) {
                if (FileBox.getInstance().getSendListSize() == 0) {
                    Toast.makeText(GetContextUtil.getInstance(), "请选择文件发送！", Toast.LENGTH_SHORT).show();

                }
                //已经选择发送文件的情况
                else {

                    //   判断有几个接受者，如果是多和接受者的话，则进行选择
                    if (UdpReceive.getSendContactInforList() != null) {

                        if (UdpReceive.getSendContactInforList().size() == 1) {
                            startSendProgressActivity(1);

                        } else if (UdpReceive.getSendContactInforList().size() > 1) {

                            Intent intent = new Intent();
                            intent.putParcelableArrayListExtra("contactInfor", (ArrayList<SendContactInfo>)
                                    UdpReceive.getSendContactInforList());
                            intent.setClass(getActivity(), SelectContactActivity.class);
                            startActivity(intent);

                        } else if (WifiUtil.readClientList() != null) {
                            if (WifiUtil.readClientList().size() == 1) {
                                //代表是自己创建wifi热点的情况
                                startSendProgressActivity(4);
                            }
                        } else {
                            Toast.makeText(GetContextUtil.getInstance(), "稍等一会等待好友连接", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            } else   //没有打开wifi的情况
            {
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.setLogging(true);
                rxPermissions.request(Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.WRITE_SETTINGS).
                        subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    if (!WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())) {
                                        Intent intent = new Intent(getActivity(), SyncPictureActivity.class);
                                        intent.putExtra("recent_hours", 0);
                                        intent.putExtra("call_type", 2);  //代表是没有wifi调用
                                        getActivity().startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "拒绝将无法传输文件", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, throwable.getMessage());
                                Toast.makeText(getActivity(), "异常了", Toast.LENGTH_SHORT).show();
                            }
                        }, new Action0() {
                            @Override
                            public void call() {

                                Toast.makeText(getActivity(), "打开热点成功准备文件传输", Toast.LENGTH_SHORT).show();
                            }
                        });



               /* if (!WifiUtil.isWifiApEnabled(GetContextUtil.getInstance())) {
                    Intent intent = new Intent(getActivity(), SyncPictureActivity.class);
                    intent.putExtra("recent_hours", 0);
                    intent.putExtra("call_type", 2);  //代表是没有wifi调用
                    getActivity().startActivity(intent);

                }*/


            }
        }


        /*
        * 启动sendprogressActivity
        *
        *
        * parm 启动源标记  来自于客户端则为1
        *
        *来自于服务端则为   2
        *
        *
        * 来自于wifi  util 则为3
        *
        * */
        public void startSendProgressActivity(final int startType) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendProGressIntent = new Intent(GetContextUtil.getInstance(), SendProgressActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", startType);
                        sendProGressIntent.putExtras(bundle);
                        startActivity(sendProGressIntent);
                        oldFileLength = FileBox.getInstance().getSendListSize();
                        if (maddFileInterface != null) {
                            maddFileInterface.addFile(oldFileLength);
                        }

                    } catch (Exception e) {

                        Log.e("抓到错误", e.getCause() + " " + e.getMessage());
                    }

                }
            }).start();

        }
    }

    /*
    * 通知sendprogressactivity 的通信接口
    *
    *
    * */
    public interface addFileInterFace {
        void addFile(int startPosition);
    }


    /*
    *该接口在sendprogressactivity中实现
    *
    *
    * */
    static public void setAddFileInterFace(addFileInterFace add) {
        maddFileInterface = add;
    }


}
