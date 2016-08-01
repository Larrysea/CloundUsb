package com.larry.cloundusb.cloundusb.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.ikidou.fragmentBackHandler.BackHandledFragment;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.File;

/**
 * Created by Larry on 6/17/2016.
 * 复制文件fragment 选择黏贴的位置
 */
public class CopyFileFragment extends BackHandledFragment {

    View view;
    Bundle mbundle;
    FileChooserPathFragment mfragment;
    FragmentTransaction mfragmentTransaction;
    LinearLayout phoneLinearLayout;//手机布局
    LinearLayout sdLinearLayout;//sd卡布局
    ProgressBar phoneProgressbar;//手机文件大小进度
    ProgressBar sdcardProgressbar;//存储卡文件大小进度

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.from(getContext()).inflate(R.layout.copy_fragment, null, false);
        initComponent(view);
        return view;
    }


    public void initComponent(View view) {
        phoneLinearLayout=(LinearLayout)view.findViewById(R.id.choose_file_fragment_phone_linearlayout);
        sdLinearLayout=(LinearLayout)view.findViewById(R.id.choose_file_fragment_card_linearlayout);
        mbundle=new Bundle();
        mfragment=new FileChooserPathFragment();
        mfragmentTransaction=getFragmentManager().beginTransaction();
        phoneLinearLayout.setOnClickListener(new itemClickListener());
        sdLinearLayout.setOnClickListener(new itemClickListener());
        phoneProgressbar=(ProgressBar)view.findViewById(R.id.choose_file_fragment_phone_progressbar);
        sdcardProgressbar=(ProgressBar)view.findViewById(R.id.choose_file_fragment_card_progressbar);
        phoneProgressbar.setVisibility(View.GONE);
        sdcardProgressbar.setVisibility(View.GONE);
    }


    class itemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.choose_file_fragment_phone_linearlayout:
                    mbundle.putString("initPath", Environment.getExternalStorageDirectory().getAbsolutePath());
                    mbundle.putInt("type", 0);
                    break;

                case R.id.choose_file_fragment_card_linearlayout:
                    mbundle.putInt("type",1);
                    String path;
                    String sdpath= FileUtil.getStoragePath(GetContextUtil.getInstance(),true);
                    File file=new File(sdpath);
                    Log.e(sdpath+"    ","显示外置sd卡文件路径"+file.isDirectory());
                    mbundle.putString("initPath",sdpath);
                    break;


            }

            mfragment.setArguments(mbundle);
            mfragmentTransaction = getFragmentManager().beginTransaction();
            mfragmentTransaction.replace(R.id.copy_fragment_container,mfragment);
            mfragmentTransaction.addToBackStack(null);
            mfragmentTransaction.commit();

        }
    }


}
