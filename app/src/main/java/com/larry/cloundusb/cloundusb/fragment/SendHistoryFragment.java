package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.SendHistoryAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.database.DB_AceClound;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by LARRYSEA on 2016/6/23.
 */
public class SendHistoryFragment extends BackHandledFragment  implements  SendHistoryAdapter.clickListener {
    RecyclerView recyclerView;
    SendHistoryAdapter sendHistoryAdapter;
    DB_AceClound db_aceClound;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_history_fragment, container, false);
        initView(view);
        return view;
    }


    public void initView(View view) {
        db_aceClound=new DB_AceClound(GetContextUtil.getInstance());
        sendHistoryAdapter=new SendHistoryAdapter(db_aceClound.GetHistoryTFInfo(1));
        recyclerView=(RecyclerView)view.findViewById(R.id.send_history_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        recyclerView.setAdapter(sendHistoryAdapter);
        sendHistoryAdapter.setOnClickListener(this);
    }


    @Override
    public void onClicK(View view, int position) {

        String path=db_aceClound.GetHistoryTFInfo(1).get(position).getTFFilePath();
        File file=new File(path);
        try{
          file.createNewFile();
        }catch (IOException e)
        {

        }
        FileUtil.openFile(file);

    }

    @Override
    public boolean interceptBackPressed() {
        return false;
    }
}
