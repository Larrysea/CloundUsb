package com.larry.cloundusb.cloundusb.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Larry on 3/21/2016.
 */
public class CacheUtil {
    LruCache<String, Bitmap> videoThumb;
    final int MAXSIZE = (int) (Runtime.getRuntime().totalMemory()/8);
    static CacheUtil tool;
    /**
     *
     * @param key  键值
     * @return   获得缓存
     */
    public Bitmap getBitmap(String key)
    {

     return videoThumb.get(key);


    }

   /**
    * 返回bitmap
    *
    *
    */
    public  void  putBitmap(String key,Bitmap bitmap)
    {

        videoThumb.put(key,bitmap);

    }

    public static CacheUtil getInsatance()
    {
        if(tool==null)
        tool=new CacheUtil();
        return tool;
    }


    private CacheUtil()   //防止公共调用
    {

    }









}