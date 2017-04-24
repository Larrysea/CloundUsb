package com.larry.cloundusb.cloundusb.view;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.larry.cloundusb.R;

import cn.qqtheme.framework.picker.NumberPicker;

/**
 * 自定义顶部及底部
 *
 *
 *
 * 自定义的时间选择器
 *
 */
public class SyncTimePicker extends NumberPicker {

    public SyncTimePicker(Activity activity) {
        super(activity);
        setTitleText(R.string.choose_sync_time_range);
        setTopLineVisible(false);
        setTextSize(20);

    }

   /* @Override
    public void show() {
        super.show();
        ViewAnimator.animate(getRootView())
                .duration(100)
                .interpolator(new AccelerateInterpolator())
                .slideBottom()
                .start();
    }*/

    @Override
    public void dismiss() {
        ViewAnimator.animate(getRootView())
                .duration(100)
                .rollOut()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        SyncTimePicker.super.dismiss();
                    }
                })
                .start();
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_choose_time_header, null);
        TextView titleView = (TextView) view.findViewById(R.id.bottom_sheet_time_tv);
        titleView.setText(titleText);
        return view;
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_choose_time_footer, null);
        Button submitView = (Button) view.findViewById(R.id.bottom_sheet_choose_time_sure);
        submitView.setText(submitText);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();

            }
        });
        Button cancelView = (Button) view.findViewById(R.id.bottom_sheet_choose_time_cancel);
        cancelView.setText(cancelText);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        return view;
    }




}
