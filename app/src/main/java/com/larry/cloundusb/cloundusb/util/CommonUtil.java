package com.larry.cloundusb.cloundusb.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Larry on 3/24/2016.
 * 基础工具类
 */

public class CommonUtil extends Application {


    final static String TAG = CommonUtil.class.toString();

    /**
     * 播放time 单位为ms
     * 返回时间格式 13:24
     */

    public static String getTotalTime(String time) {
        long longTime = 0;
        try {
            longTime = (long) Integer.parseInt(time);
        } catch (NumberFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        Date date = new Date(longTime);
        return dateFormat.format(date);
    }


    /*
    *
    * 获取当日时间  结果例如2016 -7-23
    *
    *
    *
    * */
    static public String getTimeToDay() {
        return FormatTimeToday(GetTime());
    }


    /*
    *    获取时间间隔
    *
    *
    * */

    static public String getTimeGap(int startTime, int endTime) {
        String result;
        result = String.valueOf(endTime - startTime);
        Log.e("显示时间是多少i", result);
        return String.valueOf(endTime - startTime);

    }

    /*
    * 获得系统当前的
    *
    *
    * */
    static public int getSecond() {

        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);


    }

    /*
    获取当前的时间戳
     */
    public static long GetTime() {
        long time = new Date().getTime();
        return time;
    }

    /*
    把时间戳转为时间格式转换结果2016-9-12 5:22:33
     */
    public static String FormatTimeToSeconds(long time) {
        Date d = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");   //注意两边的空格

        return format.format(d);
    }


    /*
   把时间戳转为时间格式转换结果2016-9-1
    */
    public static String FormatTimeToday(long time) {
        Date d = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");   //注意两边的空格

        return format.format(d);
    }


    /*
    * 分享应用功能
    * parm 需要分享的内容
    *
    *
    *  return 返回已经配置好的intent
    *
    *
    * */
    static public Intent shareInfor(int shareInfor) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, GetContextUtil.getInstance().getResources().getString(shareInfor));


        intent.setType("text/plain");
        return intent;


    }

    static public String getPhoneName() {
        return new Build().MODEL;
    }


    /*
    * 获取手机的相关联账户
    *
    * */
    static public String getAccountName() {

        AccountManager accountManager = AccountManager.get(GetContextUtil.getInstance());
        Account[] accounts = accountManager.getAccounts();
        for (Account parm : accounts) {
            System.out.print("显示账户数据" + parm.name);

        }
        return accounts[0].name;

    }

    /*
    *
    * 将hashmap转换为list
    *
    * */

    static public <T> List<T> subHashmap(HashMap<String, T> hashMap) {
        List<T> list = new ArrayList<T>();

        Iterator<Map.Entry<String, T>> iterator = hashMap.entrySet().iterator();
        if (hashMap.size() != 0)
            while (iterator.hasNext()) {
                Map.Entry<String, T> entry = iterator.next();
                T t = entry.getValue();
                list.add(t);
            }

        return list;

    }


    /*
    *
    * 在非主线程中显示toast
    *
    * */
    static public void showToast(String msg) {
        Looper.prepare();
        Toast.makeText(GetContextUtil.getInstance(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }




    /*
    *
    * 获得屏幕尺寸  例如手机尺寸为4 英寸  获取的数据也为4左右
    *
    * */

    static public int getScreenSizeOfDevice2(Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return (int) screenInches;
    }


}
