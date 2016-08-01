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
import com.larry.cloundusb.cloundusb.adapter.MusicChildAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.MusicInform;
import com.larry.cloundusb.cloundusb.fileutil.OpenFileUtil;
import com.larry.cloundusb.cloundusb.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 4/29/2016.
 * <p/>
 * 展示音乐的子类信息fragment
 */
public class MusicChildFragment extends BackHandledFragment {
    List<MusicInform> musicList = new ArrayList<MusicInform>();

    RecyclerView recyclerView;
    MusicFragment.ItemClickListener listener;
    List<MusicInform> checkedMusicList;//选中发送的音乐文件
    View view;
    MusicChildAdapter.MusicChildClickListener musicChildClickListener;
    MusicChildAdapter musicChildAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_child_music, container, false);
        init();
        return view;
    }


    //初始化组件
    public void init() {
        Bundle bundle = getArguments();
        musicList = (List<MusicInform>) bundle.getSerializable("musicChildList");
        recyclerView = (RecyclerView) view.findViewById(R.id.activity_child_music_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        recyclerView.addItemDecoration(new DividerItemDecoration(GetContextUtil.getInstance(), DividerItemDecoration.VERTICAL_LIST));
        musicChildAdapter = new MusicChildAdapter(GetContextUtil.getInstance(), musicList);
        recyclerView.setAdapter(musicChildAdapter);
        musicChildAdapter.setMusicListener(new MusicChildAdapter.MusicChildClickListener() {
            @Override
            public void onClick(View view, int posoition) {
                OpenFileUtil.openFile(musicList.get(posoition).getAbsPath());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });

    }

    @Override
    public boolean interceptBackPressed() {
        return true;
    }
}
