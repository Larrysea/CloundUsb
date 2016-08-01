package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.CheckUtil;

import java.util.HashMap;


/**
 * Created by Larry on 6/6/2016.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * 注册activity
 */
public class RegisterActivity extends AppCompatActivity {


    static final String TAG = "registeractivity";
    static Button loginButton;
    static TextView inforTV;//提示信息
    static updateface mupdateface;//更新界面接口
    static RegisterActivity mregisterAcitvity;
    Toolbar myToolbar;
    static public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "收到消息刷新");
            switch (msg.what) {
                case -2:
                    inforTV.setVisibility(View.VISIBLE);
                    loginButton.setText(R.string.register);
                    break;
                case 1://登录成功，关闭当前activity
                    mregisterAcitvity.finish();
                    break;
                case -1:
                    Toast.makeText(GetContextUtil.getInstance(), "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    mupdateface.update();//激活 personalsettingactivity


            }
        }


    };
    EditText editTextName;
    EditText editTextPassword;
    ImageView deleteImageView;
    RegisterActivity mregisteractivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mregisterAcitvity=this;
        initComponent();
    }

    public void initComponent() {
        myToolbar=(Toolbar)findViewById(R.id.register_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.login_tip));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);
        mregisteractivity = this;
        editTextName = (EditText) findViewById(R.id.editetextName);
        editTextPassword = (EditText) findViewById(R.id.editetextPassword);
        deleteImageView = (ImageView) findViewById(R.id.deleteNumber);
        loginButton = (Button) findViewById(R.id.login_button);
        inforTV = (TextView) findViewById(R.id.register_infor_tv);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.showSoftInputFromInputMethod(editTextName.getWindowToken(), 0);
        editTextName.addTextChangedListener(new MuitlListener());
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    loginButton.setTextColor(GetContextUtil.getInstance().getResources().getColor(R.color.white));
                }
                else if(motionEvent.getAction()==motionEvent.ACTION_UP)
                {
                    loginButton.setTextColor(GetContextUtil.getInstance().getResources().getColor(android.R.color.holo_red_light));
                }
                return false;

            }
        });
        loginButton.setOnClickListener(new MuitlListener());
        mupdateface = new updateface() {
            @Override
            public void update() {

                startActivity(new Intent(GetContextUtil.getInstance(), PersonalSettingActivity.class));
                mregisteractivity.finish();

            }
        };
    }

    public void setPersonalSetting() {
        startActivity(new Intent(this, PersonalSettingActivity.class));


    }


    interface updateface {
        void update();//更新界面函数
    }

    class MuitlListener implements View.OnClickListener, TextWatcher {


        @Override
        public void onClick(View v) {
            HashMap<String, EditText> hashMap = new HashMap<String, EditText>();
            hashMap.put("姓名", editTextName);
            hashMap.put("密码", editTextPassword);
            if (CheckUtil.checkEditext(hashMap) != null) {
                Toast.makeText(GetContextUtil.getInstance(), CheckUtil.checkEditext(hashMap), Toast.LENGTH_SHORT).show();
            } else {
                if (!loginButton.getText().equals("注册")) {
                    loginUtil.loginRequest(editTextName.getText().toString(), editTextPassword.getText().toString());
                } else {
                    loginUtil.register(editTextName.getText().toString(), editTextPassword.getText().toString());

                }
            }


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (editTextName.getText() != null) {
                deleteImageView.setVisibility(View.INVISIBLE);

            }
        }
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
