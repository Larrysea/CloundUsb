package com.larry.cloundusb.cloundusb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Larry on 6/6/2016.
 *
 *
 *
 *
 * 加密工具类
 *
 */
public class EncipherUtil {


/*
*
* md5加密方式
*
* */

    public static String getMD5(String val) throws NoSuchAlgorithmException {

       /* MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();*/
        val+="PHP888";
        char hexDigits[] = { '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        byte[] btInput = val.getBytes();
        //获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        //使用指定的字节更新摘要
        mdInst.update(btInput);
        //获得密文
        byte[] md = mdInst.digest();
        //把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];

        }
        return  new String(str);
    }



    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }




}
