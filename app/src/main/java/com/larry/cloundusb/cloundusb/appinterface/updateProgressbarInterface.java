package com.larry.cloundusb.cloundusb.appinterface;

import java.net.InetAddress;

/**
 * Created by LARRYSEA on 2016/7/1.
 */
public interface updateProgressbarInterface {



        /*
   *
   *
   * 更新进度条的回调函数
   * parm position 是发送的第几个数据
   *
   * progress 发送的进度
   *
   *
   *
   * 在tcpserver中实现
   *
   * */
        void updateProgressbar(int position, long progress, long max);
        void addFile(int startPosition);        //更新文件接口



}






