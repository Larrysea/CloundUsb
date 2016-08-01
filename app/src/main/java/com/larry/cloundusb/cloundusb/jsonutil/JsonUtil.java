package com.larry.cloundusb.cloundusb.jsonutil;

import android.util.Log;

import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Larry on 6/10/2016.
 *
 *
 * json常用工具
 */
public class JsonUtil {




     /*
     * 将json对象转换为sendfileinform对象
     *
     * */
  static   public SendFileInform jsonObjectToSenFileInform(JSONObject jsonObject) throws JSONException
    {
        SendFileInform sendFileInform=new SendFileInform();
        sendFileInform.setPath(jsonObject.getString("file_path"));
        sendFileInform.setName(jsonObject.getString("file_name"));
        sendFileInform.setFileUnit(jsonObject.getString("file_size"));
         Log.e("xianshiwenjian xinxi ",sendFileInform.getName());
        return sendFileInform;
    }



}
