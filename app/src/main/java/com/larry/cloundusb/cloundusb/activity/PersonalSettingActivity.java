package com.larry.cloundusb.cloundusb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.config.config;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

/**
 * Created by Larry on 6/9/2016.
 */
public class PersonalSettingActivity extends AppCompatActivity {

    ImageView portrait1;
    ImageView portrait2;
    ImageView portrait3;
    ImageView portrait4;
    ImageView portrait5;
    ImageView portrait6;
    ImageView portrait7;
    ImageView portrait8;
    ImageView personal_portrait;//头像imageview
    EditText nameEditext;//名字editetext
    int portraitPosition = 0;//那个头像
    Toolbar myToolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        initComponent();


    }


    /*初始化组件信息*/
    public void initComponent() {
        myToolbar = (Toolbar) findViewById(R.id.personal_setting_toolbar);
        myToolbar.setTitle(GetContextUtil.getInstance().getString(R.string.feedback));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        myToolbar.setNavigationIcon(R.mipmap.arrow_back_white_small);
        setSupportActionBar(myToolbar);
        portrait1 = (ImageView) findViewById(R.id.activity_personal_image1);
        portrait2 = (ImageView) findViewById(R.id.activity_personal_image2);
        portrait3 = (ImageView) findViewById(R.id.activity_personal_image3);
        portrait4 = (ImageView) findViewById(R.id.activity_personal_image4);
        portrait5 = (ImageView) findViewById(R.id.activity_personal_image5);
        portrait6 = (ImageView) findViewById(R.id.activity_personal_image6);
        portrait7 = (ImageView) findViewById(R.id.activity_personal_image7);
        portrait8 = (ImageView) findViewById(R.id.activity_personal_image8);
        personal_portrait = (ImageView) findViewById(R.id.personal_setting_portrait);
        config config = new config();
        personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(config.getPortraitBitmap(MainActivity.user.getPortraitId(), GetContextUtil.getInstance())));
        nameEditext = (EditText) findViewById(R.id.personal_setting_name);
        nameEditext.setText(MainActivity.user.getName());
        portrait1.setOnClickListener(new clickListener());
        portrait2.setOnClickListener(new clickListener());
        portrait3.setOnClickListener(new clickListener());
        portrait4.setOnClickListener(new clickListener());
        portrait5.setOnClickListener(new clickListener());
        portrait6.setOnClickListener(new clickListener());
        portrait7.setOnClickListener(new clickListener());
        portrait8.setOnClickListener(new clickListener());


    }

    class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_personal_image1:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait1))));
                    portraitPosition = 1;
                    break;
                case R.id.activity_personal_image2:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait2))));
                    portraitPosition = 2;
                    break;

                case R.id.activity_personal_image3:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait3))));
                    portraitPosition = 3;
                    break;
                case R.id.activity_personal_image4:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait4))));
                    portraitPosition = 4;
                    break;


                case R.id.activity_personal_image5:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait5))));
                    portraitPosition = 5;
                    break;
                case R.id.activity_personal_image6:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait6))));
                    portraitPosition = 6;
                    break;

                case R.id.activity_personal_image7:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait7))));
                    portraitPosition = 7;
                    break;
                case R.id.activity_personal_image8:
                    personal_portrait.setImageBitmap(GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(getResources().getDrawable(R.drawable.portrait8))));
                    portraitPosition = 8;
                    break;


            }

        }
    }

    /*
    * 取消方法
    *
    * */
    public void personal_cancel(View view) {
        if ((nameEditext.getText().toString().trim()).equals("")) {
            Toast.makeText(this, R.string.tip_input_name, Toast.LENGTH_SHORT).show();
        }
        portraitPosition = 0;

    }


    /*
    *
    *人物信息保存
    *
    *  //先调用上传头像，然后销毁这个activity
    *
    * */
    public void personal_save(View view) {

        loginUtil.setPersonInfor(nameEditext.getText().toString(), portraitPosition);
        this.finish();
    }

    static public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    Toast.makeText(GetContextUtil.getInstance(), GetContextUtil.getInstance().getString(R.string.tip_person_infor), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    /*
  * 监听toolbar的返回事件
  *
  * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
