package com.larry.cloundusb.cloundusb.util;

import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * 字符串常用工具类
 * Created by Larry on 4/20/2016.
 */
public class StringUtil {

    final static String TAG="String util";

    /*
    *
    * 获得ip地址的最后一部分  例如 192.168.1.43
    * 最后返回的是43
    *
    * */

    static public String getLastpartIpAddress(String ads)
    {
         if(ads.split("\\.").length>3)
         {
             return (ads.split("\\."))[3];
         }else
         {
             return null;
         }


    }


    /*
    *
    *
    * 检查是否符合规范的wifi名称
    * 标砖wifi名称
    * ”携传A4A7Larry“
    *
    *  前两位是携传  后一个是设备类型代号   然后三位是设备的mac  地址后三位 然后是设备名称  *
    * */

    static 	public  boolean checkWifiName(String name)
    {

        if(name.length()!=0)
        {
            if(String.valueOf(name.charAt(0)).concat(String.valueOf(name.charAt(1))).equals("携传"));
            {
                if(Integer.valueOf(name.charAt(2))==65||Integer.valueOf(name.charAt(2))==87||Integer.valueOf(name.charAt(2))==73)
                {
                    for(int i=3;i<7;i++)
                    {
                        boolean judge= checkNumberOrChar(name.charAt(i));
                        if(judge==false)
                            return false;

                    }
                    return true;
                }

            }
        }

        return false;


    }


    /*
    *
    * 检查是否是数字还是字母
    * 如果是返回结果为true
    *
    * */

    static public boolean checkNumberOrChar(char parm)
    {

        if((Integer.valueOf(parm)>=48&&Integer.valueOf(parm)<=59)||(Integer.valueOf(parm)>=65&&Integer.valueOf(parm)<=90))
        {
            return true;
        }

        return false;
    }


    /*
    *
    * 将输入流转化为byte array[]
    *
    * */

    public   static   /*byte[]*/  Bundle inputStreamToByteArray(InputStream is)    {
        ByteArrayOutputStream   baos   =   new ByteArrayOutputStream();
        int length;
        Bundle infobund=new Bundle();
        byte [] content=new byte[1460*8];
       try {
           while(true)
           {
               if(is.available()>0)         //数据可用
               {
                   length = is.read(content);
                   byte[] realcontent = null;
                   if (length > 0) {
                       if (length < (700))               //协议或者文件末尾数据
                       {
                           realcontent = new byte[length];
                           for (int i = 0; i < length; i++) {
                               realcontent[i] = content[i];
                           }
                           baos.close();
                           infobund.putInt("streamlength", length);
                           infobund.putByteArray("streamvalue", realcontent);
                           return infobund;
                       }
                       else
                       {
                           infobund.putInt("streamlength", length);
                           infobund.putByteArray("streamvalue",content);
                           return infobund;
                       }
                   }
               }
           }
       }catch (IOException e)
       {
         Log.e ("String util", e.getMessage());
       }

        return  null;

    }



    /*
    * 将任意的字符串协议转换为list<String>
    *
    *
    *例如协议内容是open_handle11|a.txt|1024|1024
    *
    *因此返回的结果是open_handle11  a.txt  1024 1024 链表
    * */
   static public List<String> Muitlsplit(String source,String regex)
    {
        List<String> result=new ArrayList<String>();

        for(String parm: source.split("\\"+regex)){
            result.add(parm);
        }

       return result;
    }



    /*
    *获取文件的位置
    *  例如  协议内容为 open_handle112|a.txt|1024|1024
    *
    *
    * 返回值则是12
    * */
   static  public int getFilePosition(String protocol)
    {
            List<String> result;
            result=Muitlsplit(protocol,"|");
            return Integer.valueOf(String.valueOf(result.get(0)).substring(12));

    }

    /*
    * 输入流转换为字符串
    *
    * */
    static public  String inputStreamToString(InputStream inputStream)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        int length;
        String result=null;
        try {
            while ((length = inputStream.read())!= -1) {
                byteArrayOutputStream.write(length);
                length+=length;
                if(length>700)
                {
                 return null;
                }
            }
            byteArrayOutputStream.close();
            result= new String(byteArrayOutputStream.toByteArray(),"UTF-8");
        }catch (IOException e)
        {

        }
        return  result;

    }







}
