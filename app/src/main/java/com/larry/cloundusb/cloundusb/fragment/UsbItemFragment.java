package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.adapter.UsbItemAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.constant.InternetConfig;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.io.File;
import java.util.List;

/**
 * Created by Larry on 6/10/2016.
 */
public class UsbItemFragment extends BackHandledFragment {

    RecyclerView mrecyclerview;
    UsbItemAdapter usbItemAdapter;
    List<SendFileInform> sendFileInforms;//保存文件list
    UsbItemAdapter.viewHolder viewHolder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.check_usb_fragment, container, false);
        mrecyclerview = (RecyclerView) view.findViewById(R.id.check_usb_recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        mrecyclerview.addItemDecoration(new DividerItemDecoration(GetContextUtil.getInstance(), DividerItemDecoration.VERTICAL_LIST));

        usbItemAdapter = new UsbItemAdapter(getArguments().getInt("type"));
        sendFileInforms = usbItemAdapter.getMsendFileInfromList();
        usbItemAdapter.setRecyclerListener(new UsbItemAdapter.recyclerListener() {
            @Override
            public void onItemClick(View view, int position, int type) {

                viewHolder = (UsbItemAdapter.viewHolder) mrecyclerview.getChildViewHolder(view);
                if (viewHolder != null) {
                    if (type == 1) {
                        loginUtil.downLoadUsbFile(InternetConfig.download_interface + sendFileInforms.get(position).getPath(), sendFileInforms.get(position).getName(),
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiechuan/本地文件/",
                                viewHolder.progressbar,
                                handler);

                    } else if (type == 2) {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiechuan/本地文件/" + sendFileInforms.get(position).getName());
                        if (file.exists()) {
                            FileUtil.openFile(file);
                        }


                    }

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mrecyclerview.setAdapter(usbItemAdapter);


        return view;
    }

    @Override
    public boolean interceptBackPressed() {
        Log.e("内部消耗", "消耗了");
        return true;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    viewHolder.imagview.setText(GetContextUtil.getInstance().getString(R.string.open));
                    viewHolder.imagview.setBackground(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.strokebg));
                    viewHolder.imagview.setPadding(10,10,10,10);
                    viewHolder.imagview.setTextColor(GetContextUtil.getInstance().getResources().getColor(R.color.blue));
                    break;
            }
        }
    };


}
