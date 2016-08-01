package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.ReceiveHistoryAdapter;
import com.larry.cloundusb.cloundusb.adapter.SendHistoryAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.database.DB_AceClound;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by LARRYSEA on 2016/6/22.
 * <p/>
 * 显示接收记录的fragment
 */
public class ReceiveHistoryFragment extends BackHandledFragment implements ReceiveHistoryAdapter.clickListener {

    RecyclerView recyclerView;                            //显示的recyclerview
    ReceiveHistoryAdapter receiveHistoryAdapter;          //适配器
    DB_AceClound db_aceClound;
    final static String TAG="Receivehistoryfragment";     // log tag


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receive_history_fragment, container, false);
        db_aceClound = new DB_AceClound(GetContextUtil.getInstance());
        initView(view);
        return view;
    }


    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.receive_history_recyclerview);
        receiveHistoryAdapter=new ReceiveHistoryAdapter(db_aceClound.GetHistoryTFInfo(2));
        recyclerView.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        recyclerView.setAdapter(receiveHistoryAdapter);
        receiveHistoryAdapter.setClickListener(this);


    }


    @Override
    public void onClick(View view, int position) {
        String path = db_aceClound.GetHistoryTFInfo(2).get(position).getTFFilePath();
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {

        }
        FileUtil.openFile(file);
    }


    @Override
    public boolean interceptBackPressed() {
    return false;
    }
}
