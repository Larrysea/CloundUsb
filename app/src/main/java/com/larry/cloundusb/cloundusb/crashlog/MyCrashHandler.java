package com.larry.cloundusb.cloundusb.crashlog;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Larry on 4/25/2016.
 *
 * 记录  crash崩溃日志文件并将之存储子sd卡
 *
 */
public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    @SuppressWarnings("unused")
    private Context context;
    private static MyCrashHandler crashHandler = new MyCrashHandler();
    Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private MyCrashHandler() {
    }
    public static MyCrashHandler getInstanceMyCrashHandler() {
        return crashHandler;
    }
    /**
     * 初始化方法
     * @param context 上下文对象
     */
    public void init(Context context) {
        this.context = context;
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    /**
     * 异常处理方法
     * @Params Thread对象
     * @param Throwable对象
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(thread, ex) && defaultExceptionHandler != null) {
            defaultExceptionHandler.uncaughtException(thread, ex);
        }
    }
    // 程序异常处理方法
    private boolean handleException(Thread thread, Throwable ex) {
        StringBuilder sb = new StringBuilder();
        long startTimer = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss ");
        Date firstDate = new Date(System.currentTimeMillis()); // 第一次创建文件，也就是开始日期
        String str = formatter.format(firstDate);
        sb.append(startTimer);
        //sb.append("\n");
        sb.append(str); // 把当前的日期写入到字符串中
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        String errorresult = writer.toString();
        sb.append(errorresult);
        sb.append("\n");
        try {
          //  File fileDir = new File("/data/data/com.larry.cloundusb/Ebank/");
         File fileDir = new File("/sdcard/com.ebank/EBank/");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File files = new File(fileDir, "ebank.log");
            if (!files.exists()) {
                files.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(files,
                    true);
            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();
            // 文件大小限制在1M,超过1M自动删除
            FileInputStream fileInputStream = new FileInputStream(files);
            int sizeK = fileInputStream.available() / 1024; // 单位是KB
            int totalSize = 1 * 1024;
            if (sizeK > totalSize) {
                boolean b = files.delete();
                if (b) { // 删除成功,重新创建一个文件
                    @SuppressWarnings("unused")
                    File filesTwo = new File(fileDir, "ebank.log");
                    if (!files.exists()) {
                        files.createNewFile();
                    }
                } else {
                  // 删除失败
                    FileOutputStream fileOutputStream2 = new FileOutputStream(
                            files);
                    fileOutputStream2.write(" ".getBytes()); // 写入一个空格进去
                }
            }
// 文件保存7天，过了7天自动删除
            FileReader fileReader = new FileReader(files);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String firstLine = bufferedReader.readLine();
            long startTimerFile = Long.valueOf(firstLine.trim()); // 类型转换
            long endTimer = System.currentTimeMillis();
            long totalDay = 24 * 60 * 60 * 1000 * 7;
            final File f = files;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        boolean n = f.delete();
                        if(n){
                            File fileDirs = new File("/sdcard/com.ebank/EBank/");
                            if (!fileDirs.exists()) {
                                fileDirs.mkdirs();
                            }
                            File filess = new File(fileDirs, "ebank.log");
                            if (!filess.exists()) {
                                filess.createNewFile();
                            }
                        }else{
// 删除失败
                            FileOutputStream fileOutputStream2 = new FileOutputStream(f);
                            fileOutputStream2.write(" ".getBytes()); // 写入一个空格进去
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
//定时器类的对象
            Timer timer = new Timer();
            if ((endTimer - startTimerFile) >= totalDay) {
                timer.schedule(timerTask, 1); // 7天后执行
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        defaultExceptionHandler.uncaughtException(thread, ex);
        return true;
    }
}