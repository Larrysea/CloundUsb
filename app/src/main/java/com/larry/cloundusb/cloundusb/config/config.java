package com.larry.cloundusb.cloundusb.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.baseclass.User;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

/**
 * Created by Larry on 6/14/2016.
 * 保存程序相关的配置信息
 * <p>
 * 还有用户信息
 * 还有用户的登录信息
 */
public class config {


    /*
    *
    * 获取头像bitmap
    *
    *
    * */
    static public Bitmap getPortraitBitmap(int type, Context context) {
        Drawable drawable;
        switch (type) {
            case 1:
                drawable = context.getResources().getDrawable(R.drawable.portrait1);

                break;
            case 2:
                drawable = context.getResources().getDrawable(R.drawable.portrait2);

                break;
            case 3:
                drawable = context.getResources().getDrawable(R.drawable.portrait3);

                break;
            case 4:
                drawable = context.getResources().getDrawable(R.drawable.portrait4);

                break;
            case 5:
                drawable = context.getResources().getDrawable(R.drawable.portrait5);

                break;
            case 6:
                drawable = context.getResources().getDrawable(R.drawable.portrait6);
                break;
            case 7:
                drawable = context.getResources().getDrawable(R.drawable.portrait7);

                break;
            case 8:
                drawable = context.getResources().getDrawable(R.drawable.portrait8);
                break;
            default:
                drawable = context.getResources().getDrawable(R.drawable.portrait4);
                break;
        }

        return GraphicsUtil.toRoundBitmap(GraphicsUtil.drawableToBitmap(drawable));


    }




    /*
    *
    *
    * 保存登录信息
    *
    * 写入sharedpreference
    *
    *
    *
    * 保存了login信息
    *
    * 保存用户昵称
    * */
    static public void saveLoginInfor(User user,Context context)
    {
        SharedPreferences mySharedPreferences= context.getSharedPreferences("login_infor",
                Activity.MODE_PRIVATE);
         SharedPreferences.Editor editor = mySharedPreferences.edit();

         editor.putBoolean("islogin",user.isLogin());
         editor.putString("name", user.getPetname());
         editor.putString("userid",user.getUserId());
         editor.putInt("portraitid",user.getPortraitId());
         editor.commit();


    }


    /*
    * 获取保存的登录信息信息
    *
    * */
    static public User getLoginInfor(Context context)
    {
        User user=new User();
        SharedPreferences mySharedPreferences= context.getSharedPreferences("login_infor",
                Activity.MODE_PRIVATE);
        if(mySharedPreferences!=null)
        {
            user.setName(mySharedPreferences.getString("name",""));
            user.setUserId(mySharedPreferences.getString("userid",""));
            user.setLogin(mySharedPreferences.getBoolean("islogin",false));
            String string=mySharedPreferences.getString("userid","");
            String userid= user.getUserId();
            return user;
        }

        return  null;
    }



    /*
     *
     *
     *清除信息
     *
       */
  static   public void clearInfor(Context context)
    {
        SharedPreferences mySharedPreferences= context.getSharedPreferences("login_infor",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putBoolean("islogin",false);
        editor.putString("name", "");
        editor.putString("userid","");
        editor.putInt("portraitid",-1);
        editor.commit();





    }




}
