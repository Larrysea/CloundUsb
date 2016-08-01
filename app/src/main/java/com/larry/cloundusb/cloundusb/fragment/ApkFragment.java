package com.larry.cloundusb.cloundusb.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.ApkAdapter;
import com.larry.cloundusb.cloundusb.baseclass.ApkInform;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.MultiMediaUtil;
import com.larry.cloundusb.cloundusb.fileutil.ApkSearchUtils;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.larry.cloundusb.cloundusb.view.ContextMenuDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 3/22/2016.
 */
public class ApkFragment extends BackHandledFragment{


    static ProgressDialog mprogressDialog;
    static RecyclerView apkRecyclerView;   //展示应用的recycerView
    final static int SCAN_OK = 1;        //扫描结束的判定符号
    final static int SETDEFAULT=2;

    static Context mcontext;
    static List<ApkInform> mapkList;   //保存某个子类的文件
    static ApkAdapter madapeter;
    static FragmentManager fragmentManager;
    static Bundle mbundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {

        View convertView = inflater.inflate(R.layout.apk_fragment, container, false);
        mcontext = getContext();

        apkRecyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerviewApk);
        apkRecyclerView.setLayoutManager(new GridLayoutManager(mcontext, 4));
        //  apkRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        init();
        fragmentManager=getFragmentManager();
        return convertView;

    }


    public interface ItemClickListener {
        void ItemClick(List<String> list);
    }

   static  public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                 //   mprogressDialog.dismiss();
                    if (mapkList != null) {
                         madapeter = new ApkAdapter(mcontext, mapkList);

                        madapeter.setOnItemClickListener(new ApkAdapter.clickListener() {
                            @Override
                            public void onItemClick(View v, int position) {

                                Bundle bundle = new Bundle();
                                ApkInform inform = mapkList.get(position);
                                bundle.putSerializable("childList", inform);

                            }

                            @Override
                            public void onItemLongClick(View v, int position) {
                                ContextMenuDialog contextMenuDialog= new ContextMenuDialog();
                                SendFileInform sendFileInform=new SendFileInform();
                                sendFileInform.setPath(mapkList.get(position).getAbsPath());
                                sendFileInform.setName(mapkList.get(position).getFileName());
                                sendFileInform.setFilesize(Long.parseLong(mapkList.get(position).getFileSize()));
                                sendFileInform.setType(mapkList.get(position).getType());
                                sendFileInform.setSpecialInfo(mapkList.get(position).getSpecialInfor());
                                sendFileInform.setPortrait(GraphicsUtil.drawableToBitmap(mapkList.get(position).getThumbPath()));

                                mbundle.putParcelable("copyfileinform",sendFileInform);
                                contextMenuDialog.setArguments(mbundle);
                                contextMenuDialog.show(fragmentManager,"tag");

                            }
                        });
                        apkRecyclerView.setAdapter(madapeter);
                    }
                    break;
                case SETDEFAULT:
                    madapeter .notifyDataSetChanged();
                    break;
            }

        }


    };

    /*
    * 对apk信息进行初始化
    *
    *
    * */
    public void init() {

        new ArrayList<ApkInform>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mcontext, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

       // mprogressDialog = ProgressDialog.show(getContext(), null, "稍等");
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApkSearchUtils tool = new ApkSearchUtils(mcontext);
                mapkList = tool.getApkInfor(mcontext);
                long startTime=System.currentTimeMillis();//扫描开始时间

                while(true)
                {
                    if (mapkList!= null)
                    {
                        handler.sendEmptyMessage(SCAN_OK);
                        break;
                    }
                    else
                    {
                        if(((System.currentTimeMillis()-startTime)/1000)>5)
                        {
                            handler.sendEmptyMessage(SCAN_OK);
                            break;
                        }

                    }
                }




            }

        }).start();
        //MultiMediaUtil.scanVideo(null);
        mbundle=new Bundle();

    }




}
