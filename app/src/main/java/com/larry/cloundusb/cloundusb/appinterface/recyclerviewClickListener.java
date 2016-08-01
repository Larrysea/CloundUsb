package com.larry.cloundusb.cloundusb.appinterface;

import android.view.View;

/**
 * Created by LARRYSEA on 2016/7/14.
 *
 * recyclerview 的监听回调接口
 *
 */
public interface recyclerviewClickListener {

    void onItemClick(View view, int position);
    void onLongItemClick(View view,int position);
}
