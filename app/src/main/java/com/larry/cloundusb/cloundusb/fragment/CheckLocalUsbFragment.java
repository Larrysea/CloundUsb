package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;

/**
 * Created by LARRYSEA on 2016/6/22.
 *
 * 查看本地保存的文件fragment
 *
 *
 *
 */
public class CheckLocalUsbFragment extends BackHandledFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View convertView = inflater.inflate(R.layout.check_local_usb_item_fragment, container, false);
        return convertView;

    }





}
