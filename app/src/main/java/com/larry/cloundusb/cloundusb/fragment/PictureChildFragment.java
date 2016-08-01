package com.larry.cloundusb.cloundusb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.adapter.PictureChildAdapter;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 4/21/2016.
 *
 * 显示图片的子内容
 *
 *
 */
public class PictureChildFragment extends BackHandledFragment{

    List<String> imageList = new ArrayList<String>();
    PictureChildAdapter adapter;
    RecyclerView recyclerView;
    PictureFragment.ItemClickListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.activity_child_picture, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.activity_child_picture_recyclerview);
        if(getArguments()!=null)
        {
            if(getArguments().get("childList")!=null)
            {
                imageList=(List<String>)getArguments().get("childList");
            }
        }
        recyclerView.setLayoutManager(new GridLayoutManager(GetContextUtil.getInstance(), 3));
      //  recyclerView.setLayoutManager(new LinearLayoutManager(GetContextUtil.getInstance()));
        recyclerView.setAdapter(new PictureChildAdapter(GetContextUtil.getInstance(), imageList));
        return view;
    }


    /*
    *
    * 刷新子界面
    *
    * */

    public void updateView()
    {

    }




    @Override
    public boolean interceptBackPressed() {
        return true;
    }
}

