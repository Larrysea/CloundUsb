package com.larry.cloundusb.cloundusb.baseclass;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.util.GraphicsUtil;

/**
 * Created by Larry on 6/5/2016.
 * 用户类记录用户基本数据
 *
 *
 *
 *
 */
public class User {
    String name;//用户名    其实默认为用户的手机账户信息
    String password;//密码    //此密码是经过加密的md5密码
    boolean isLogin;//是否登录
    String userId;//用户id
    int device;//设备平台标注
    String petname;
    int portraitId;//现在使用本地的头像资源  范围为1-8

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    /*
    * 获取头像的drawable
    *
    * */
   static  public Bitmap getPortraitBitmap(int type)
    {

        Bitmap bitmap=null;
      switch (type)
      {
          case 0:
              bitmap=GraphicsUtil.drawableToBitmap( GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait1));
              break;
          case 1:
              bitmap= GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait2));
              break;
          case 2:
              bitmap= GraphicsUtil.drawableToBitmap( GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait3));
              break;

          case 3:
              bitmap=GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait4));
              break;
          case 4:
              bitmap=GraphicsUtil.drawableToBitmap( GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait5));
              break;

          case 5:
              bitmap=GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait6));
              break;
          case 6:
              bitmap=GraphicsUtil.drawableToBitmap( GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait7));
              break;

          case 7:
              bitmap= GraphicsUtil.drawableToBitmap(GetContextUtil.getInstance().getResources().getDrawable(R.drawable.portrait8));
              break;


      }
        return bitmap;

    }


}
