package com.larry.cloundusb.cloundusb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CheckUtil;

import java.util.HashMap;

/**
 * Created by Larry on 5/25/2016.
 * 反馈activity
 */

public class FeedBackActivitiy extends AppCompatActivity {
    Button sendButton;
    EditText feedbackET;//反馈信息
    EditText emailAddressET;//联系方式

    FeedBackActivitiy feedBackActivity;
    final static String TAG="feedback activity";
    Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initComponent();

    }

    /*
    * 初始化组件
    *
    * */
    public void initComponent() {
        feedBackActivity=this;
        myToolbar=(Toolbar)findViewById(R.id.feedback_toolbar);
        sendButton = (Button) findViewById(R.id.feedback_send_button);
        feedbackET = (EditText) findViewById(R.id.feedback_content);
        emailAddressET = (EditText) findViewById(R.id.feedback_email_edittext);
        final HashMap<String, EditText> checkHashmap = new HashMap<String, EditText>();
        checkHashmap.put("反馈内容", feedbackET);
        checkHashmap.put("邮箱", emailAddressET);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.feedback));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;
                if (null==(result= CheckUtil.checkEditext(checkHashmap))) {
                    if(MainActivity.user!=null&&MainActivity.user.isLogin())
                    {
                        loginUtil.feedbackRequest(MainActivity.user.getUserId(), feedbackET.getText().toString(), emailAddressET.getText().toString());
                    }else{
                        loginUtil.feedbackRequest("notregister user", feedbackET.getText().toString(), emailAddressET.getText().toString());
                        Toast.makeText(FeedBackActivitiy.this, GetContextUtil.getInstance().getString(R.string.thanks_you_feedback), Toast.LENGTH_SHORT).show();
                        feedBackActivity.finish();
                    }

                } else {
                    Toast.makeText(GetContextUtil.getInstance(),result,Toast.LENGTH_SHORT).show();
                }
            }
        });

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
