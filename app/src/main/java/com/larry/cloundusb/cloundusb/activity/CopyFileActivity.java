package com.larry.cloundusb.cloundusb.activity;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.CopyFile;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;
import com.larry.cloundusb.cloundusb.fileutil.CopyFileUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;
import com.larry.cloundusb.cloundusb.fragment.CopyFileFragment;
import com.larry.cloundusb.cloundusb.fragment.FileChooserPathFragment;

import java.io.File;
import java.util.List;

/**
 * Created by Larry on 6/17/2016.
 * <p/>
 * 复制文件activity
 */
public class CopyFileActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    FileChooserPathFragment FileChoosePathFragment;
    CopyFileFragment copyFileFragment;
    Toolbar toolbar;
    updateFragment mupdateFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_file);
        initComponent();


    }


    /*
    *
    *初始化组件
    *
    * */
    public void initComponent() {

        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.activity_copy_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(GetContextUtil.getInstance().getString(R.string.pastetopath));
        CopyFileFragment copyFileFragment = new CopyFileFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.choose_file_fragment_container, copyFileFragment);
        fragmentTransaction.commit();
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void setUpdateFragment(updateFragment updateFragment) {
        mupdateFragment = updateFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_paste) {
        if( FileUtils.copyFile(new File(CopyFileUtil.getMcopyFile()),new File(FileChoosePathFragment.nowPath)))
        {
            Toast.makeText(this,"复制文件成功",Toast.LENGTH_SHORT).show();
        }
        }

        return true;
    }


    /*
    * 更新文件之后刷新fragment的接口
    *
    * */
    public interface updateFragment {
        void copyupdateFragment(int position, SendFileInform sendFileInform);

    }
}



