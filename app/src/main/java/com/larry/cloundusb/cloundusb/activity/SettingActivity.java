package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.view.SelectFilePathDialog;

/**
 * Created by Larry on 5/25/2016.
 */

public class SettingActivity extends AppCompatActivity {

    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        myToolbar=(Toolbar)findViewById(R.id.setting_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.setting));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);

    }

    public void pathOnClick(View view)
    {
        SelectFilePathDialog selectFilePathDialogDialog =new SelectFilePathDialog();
        selectFilePathDialogDialog.show(getFragmentManager(),"null");

    }


    public void aboutInfor(View view)
    {
       startActivity(new Intent(this,InforActivity.class));
    }

    public void settingHelpDoc(View view){
        Toast.makeText(SettingActivity.this, GetContextUtil.getInstance().getString(R.string.no_doc_tip), Toast.LENGTH_SHORT).show();
    }


    /*
    * 监听toolbar的返回事件
    *
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
