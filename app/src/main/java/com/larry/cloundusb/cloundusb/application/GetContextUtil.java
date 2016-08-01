package com.larry.cloundusb.cloundusb.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Larry on 4/4/2016.
 *
 *
 *
 * 获取context的工具类
 */
public class GetContextUtil extends MultiDexApplication {

    static GetContextUtil instance;
    public static Context getInstance()
    {
        if(instance==null)
            return instance=new GetContextUtil();

      /*  MyCrashHandler crashHandler=MyCrashHandler.getInstanceMyCrashHandler();
        crashHandler.init(instance);*/
        return instance;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance=this;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


}
