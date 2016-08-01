package com.larry.cloundusb.cloundusb.Interneutil;

import android.content.Context;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.larry.cloundusb.cloundusb.fileutil.FileSizeUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.function.LongToDoubleFunction;

import okhttp3.Call;


/**  网络常用的工具
 * Created by Larry on 4/4/2016.
 */

public class InternetTool {

    static double speed;//网络速度
    static long currentTime;
    static double remainingTime;//剩余时间
    static String [] NetSpeedResult;
    static String fileSize;//文件大小


  //将获取的整形数转换为ipv4地址
    public static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }


    /*
    * 判断wifi是否打开状态
    *  return 返回结果true wifi打开
    *
    *
    * */
  static   public boolean JudgeWifiState(Context context)
  {
      WifiManager manager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
      return manager.isWifiEnabled();

  }


    /*
    *
    *     wifi状态下获取ip地址
    *
    *     返回inetaddress
    *
    *
    * */

   static public InetAddress getWifiAddress(Context context)
    {


        if(JudgeWifiState(context))
        {

        WifiManager manager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=manager.getConnectionInfo();
        InetAddress inetAddress=null;
        try{

            inetAddress=InetAddress.getByName(intToIp(wifiInfo.getIpAddress()));

        }catch (UnknownHostException e)
        {
            Log.e("internetool error", e.getMessage());
        }
        return inetAddress;

        }
        else
            return null;


    }




    /*
    *     获得本地的ip地址
    *
    *     这个方法有点问题好像
    *
    * */
    static String  getLocalIpaddress(Context context) {

        if (JudgeWifiState(context)) {


            try {
                for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements(); ) {
                    {
                        NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                        for (Enumeration<InetAddress> enuminteAddresses = networkInterface.getInetAddresses();
                             enuminteAddresses.hasMoreElements();)
                        {

                            InetAddress inetAddress = enuminteAddresses.nextElement();
                            if (!inetAddress.isLoopbackAddress()) {

                                return inetAddress.getHostAddress();
                            }

                        }


                    }
                }
            } catch (SocketException e) {
                Log.e("internettool error", e.getMessage());

            }



        }
        return null;
    }


    /*
    * 判断手机的网络状况是否可以使用网络
    * return true 说明网络可用
    *
    * */
    static public boolean JudgeNetWorkAvaliable(Context context)
    {

        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null) {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null &&networkInfo.isConnected())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

    }


    /*
    * 计算文件发送的大概时间
    * parm 文件长度
    *
    * 返回时间   单位s
    * */

    public String getTotalTime(long size)
    {
           long s= size/(1024*1024);
           long m=size/(1024*1024*60)%60;

           return  m+" m"+s;
    }

    /*
    * 判断网络状况的函数
    *
    * 返回网络质量信息
    *
    * 返回null表示当前网络不可用
    *
    * */
   static public ConnectionQuality getNetWorkState(ExecutorService executorService,Context context)
    {

     if(true)
     {
        ConnectionQuality result;
        networkQuality callable=new networkQuality();
       // Future  future=executorService.submit(callable);
       // executorService.shutdown();

       try{

         // return (ConnectionQuality) future.get();
       }
       catch(Exception e)
       {

       }

     }
        else
     {
         return null;
     }

      return null;

    }


    /*
    * 获取网络带宽的大小
    *
    * */
   static  public double getBandWidth()
    {

        return ConnectionClassManager.getInstance().getDownloadKBitsPerSecond();

    }

  static   class networkQuality implements Callable<ConnectionQuality>
    {


        @Override
        public ConnectionQuality call() throws Exception {

            OkHttpUtils.get()
                    .url("https://www.taobao.com/")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DeviceBandwidthSampler.getInstance().stopSampling();
                            ConnectionClassManager.getInstance().getCurrentBandwidthQuality();

                        }
                    });


            return ConnectionClassManager.getInstance().getCurrentBandwidthQuality();
        }
    }


    /*
    *
    * 获取网络下速度
    *
    * @parm 第一个参数是代表已经发送的长度
    * @parm 第二个参数是代表开始发送时间
    * @parm 第三个参数是代表文件总的长度
    *
    * @return 返回结果中第一个是网络速度
    * 第二个结果结果是返回的下载剩余时间
    *
    * */
  static public String[] getDownloadSpeed(long fileSendSize,long startTime,long totalSize)
  {
      if(NetSpeedResult==null){
          NetSpeedResult=new String[2];
      }
      currentTime= System.currentTimeMillis();
      speed=(Double.parseDouble(FileSizeUtil.convertFileSize(fileSendSize).substring(0,FileSizeUtil.convertFileSize(fileSendSize).indexOf(" ")))/(double)((currentTime-startTime)/1000));
      if(speed!=0)
      {
          fileSize= FileSizeUtil.convertFileSize(totalSize-fileSendSize).substring(0,(FileSizeUtil.convertFileSize(totalSize-fileSendSize)).indexOf(" "));
          remainingTime= Double.parseDouble(fileSize)/speed;
          NetSpeedResult[0]=new DecimalFormat("0.0").format(speed);
          NetSpeedResult[1]=String.valueOf((int)remainingTime);

      }
      return NetSpeedResult;
  }








}
