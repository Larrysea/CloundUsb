package com.larry.cloundusb.cloundusb.view;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;

/**
 * Created by Larry on 6/8/2016.
 * <p>
 * 常用的视图小工具
 */
public class ViewUtil {


    static public BottomSheetBehavior mBehavior;
    static public BottomSheetBehavior mDialogBehavior;
    static public BottomSheetDialog mBottomSheetDialog;



    /*
    *
    * 第一个是布局 查找者
    *
    * 第二个参数是  父类布局的容器
    *
    *
    * */


    @SuppressLint("InflateParams")
    static public void showBottomSheetDialog(LayoutInflater layoutInflater, View parentView) {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        /*View view = layoutInflater.inflate(R.layout.sheet, null);*/

        mDialogBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        mBottomSheetDialog = new BottomSheetDialog(GetContextUtil.getInstance());
        mBottomSheetDialog.setContentView(parentView);
       /* mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());*/
        mDialogBehavior = BottomSheetBehavior.from(parentView);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }


}
