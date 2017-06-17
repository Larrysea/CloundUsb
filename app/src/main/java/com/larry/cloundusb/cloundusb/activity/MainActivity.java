package com.larry.cloundusb.cloundusb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.TcpClient;
import com.larry.cloundusb.cloundusb.Interneutil.loginUtil;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.User;
import com.larry.cloundusb.cloundusb.config.config;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.fragment.ParentFragment;
import com.larry.cloundusb.cloundusb.qractivity.CaptureActivity;
import com.larry.cloundusb.cloundusb.service.CheckNetWorkStateServices;
import com.larry.cloundusb.cloundusb.util.CommonUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;
import com.larry.cloundusb.cloundusb.view.SyncTimePicker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.qqtheme.framework.picker.OptionPicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static User user = null;
    public static ExecutorService sexecutorService;
    public static TcpClient tcpClient;
    public static MainActivity mainactivityInstance;
    static public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    Intent intent = new Intent(GetContextUtil.getInstance(), SendProgressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    intent.putExtras(bundle);
                    mainactivityInstance.startActivity(intent);
                    break;

                case 3:
                    Toast.makeText(mainactivityInstance, "3333", Toast.LENGTH_SHORT).show();
                    break;

                case 6:
                    Toast.makeText(mainactivityInstance, "6666", Toast.LENGTH_SHORT).show();

                    break;
                case 7:
                    Toast.makeText(mainactivityInstance, "7777", Toast.LENGTH_SHORT).show();

                    break;

                case 4:
                    Toast.makeText(mainactivityInstance, "4444", Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    //  Toast.makeText(mainactivityInstance, "5555", Toast.LENGTH_SHORT).show();

                    break;
                case 9:
                    Toast.makeText(mainactivityInstance, "9999", Toast.LENGTH_SHORT).show();

                    break;


            }
        }
    };
    static TextView nameTv;
    static ImageView portraitIv;
    static TextView tipLoginInforTv;//登录提示
    static Button loginBtn;  //登录按钮
    static closeSendProgressActivity mcloseIn;// 关闭接口
    final String TAG = "MAINACTIVITY";
    public long exitTime;
    ParentFragment filefragment;
    int checkedSize;
    Intent checkNetWorkServices;  //检查网络状况的服务
    // 再按一次退出
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), GetContextUtil.getInstance().getString(R.string.exit_infor),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/
    NavigationView navigationView;

    /*
    * 显示更新登录侧边栏的信息
    *
    * */
    static public void updateLoginInfor(User user) {
        config.saveLoginInfor(user, GetContextUtil.getInstance());
        if (MainActivity.user.isLogin()) {
            loginBtn.setVisibility(View.GONE);
            tipLoginInforTv.setVisibility(View.GONE);
            nameTv.setVisibility(View.VISIBLE);
            portraitIv.setVisibility(View.VISIBLE);
            nameTv.setText(MainActivity.user.getPetname());
            portraitIv.setImageBitmap(GraphicsUtil.toRoundBitmap(config.getPortraitBitmap(MainActivity.user.getPortraitId(), GetContextUtil.getInstance())));
        }


    }

    /*
    * 初始化相关组件的信息
    *
    *
    * */

    /*
    *
    * 获取handler
    * */
    static public Handler getHandler() {
        return handler;
    }

    static public void setCloseInterface(closeSendProgressActivity closeInterface) {
        mcloseIn = closeInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConfig(savedInstanceState);
        initComponent();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
            if (mcloseIn != null) {
                mcloseIn.close();
            }
            finish();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, CheckHistoryActivity.class));
            return true;
        }
        if (id == R.id.action_scanqr) {
            startActivity(new Intent(this, CaptureActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_share) {
            startActivity(Intent.createChooser(CommonUtil.shareInfor(R.string.shareReason),
                    GetContextUtil.getInstance().getString(R.string.shareTomyFrinend)));
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(GetContextUtil.getInstance(), FeedBackActivitiy.class));

        } else if (id == R.id.nav_check_usb) {
            if (MainActivity.user.isLogin()) {
                FileBox.getInstance().cleanAllUsbItem();
                loginUtil.getusbFile();
                startActivity(new Intent(GetContextUtil.getInstance(), CheckUsbActivity.class));

            } else {
                Toast.makeText(MainActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, RegisterActivity.class));
            }


        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(GetContextUtil.getInstance(), SettingActivity.class));

        } else if (id == R.id.nav_exit) {
            if (MainActivity.user.isLogin()) {
                loginUtil.exiteAccount();
            } else {
                this.finish();
            }


        } else if (id == R.id.nav_picture_sync) {
            //  ViewUtil.showBottomSheetDialog(getLayoutInflater(),getWindow().getDecorView());
            SyncTimePicker customHeaderAndFooterPicker = new SyncTimePicker(this);
            customHeaderAndFooterPicker.setOffset(2);
            customHeaderAndFooterPicker.setRange(0, 24);
            customHeaderAndFooterPicker.setSelectedIndex(3);
            customHeaderAndFooterPicker.setLabel("   小时");
            customHeaderAndFooterPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    Intent intent = new Intent(mainactivityInstance, SyncPictureActivity.class);
                    intent.putExtra("recent_hours", Integer.valueOf(option));

                    mainactivityInstance.startActivity(intent);
                    Toast.makeText(MainActivity.this, "选择了" + option, Toast.LENGTH_SHORT).show();
                }
            });
            customHeaderAndFooterPicker.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void initConfig(Bundle savedInstanceState) {
        int count = Runtime.getRuntime().availableProcessors() * 2 + 1;
        sexecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
        checkNetWorkServices = new Intent();
        checkNetWorkServices.setClass(GetContextUtil.getInstance(), CheckNetWorkStateServices.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            filefragment = ParentFragment.getInstance();
            // PictureFragment fragment=new PictureFragment();
            transaction.replace(R.id.com_larry_cloundusb_mainactivity_textview, filefragment);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FileBox.cleanInsatance();
        user = new User();
        FileBox.getInstance().initUsbFileList();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getBaseContext().startService(checkNetWorkServices);
        if (MainActivity.user.isLogin()) {
            updateLoginInfor(MainActivity.user);

        } else if (config.getLoginInfor(GetContextUtil.getInstance()) != null) {
            MainActivity.user = config.getLoginInfor(GetContextUtil.getInstance());
            if (!MainActivity.user.getName().trim().equals("")) {
                updateLoginInfor(MainActivity.user);
                Log.e(TAG, "数据为空");
            }


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(checkNetWorkServices);
        sexecutorService.shutdown();


    }

    public void loginClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /*
    * 初始化组件
    *
    * */
    public void initComponent() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        tipLoginInforTv = (TextView) view.findViewById(R.id.mainactivity_infortip_tv);
        loginBtn = (Button) view.findViewById(R.id.mainactivity_login_btn);
        nameTv = (TextView) view.findViewById(R.id.com_larry_cloundusb_mainactivity_drawer_nametv);
        portraitIv = (ImageView) view.findViewById(R.id.com_larry_cloundusb_mainactivity_drawer_portrait);
        portraitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetContextUtil.getInstance(), PersonalSettingActivity.class));
            }
        });
        mainactivityInstance = this;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == event.ACTION_DOWN) {
            onBackPressed();
        }
        return false;
    }

    /*
    * 关闭发送进度progressactivity
    *
    * */
    public interface closeSendProgressActivity {
        void close();
    }


}
