package com.larry.cloundusb.cloundusb.util;

import android.widget.EditText;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Larry on 6/7/2016.
 *
 *
 * 检查之类的工具类
 */
public class CheckUtil {

    /*
    *
    * 检查文本内容是否为空
    * */
    final static String infor="请填写你的";

      static  public String checkEditext(HashMap<String,EditText> editTextHashMap) {
        Iterator<Map.Entry<String,EditText>> iterator=editTextHashMap.entrySet().iterator();
        List<String> resultString;
        while (iterator.hasNext())
        {
         Map.Entry<String,EditText> entry=iterator.next();
           String content= entry.getValue().getText().toString();

            if(entry.getValue().getText().toString().trim().equals(""))
                 {
                   return infor+entry.getKey();
                 }

        }
        return  null;
    }

}
