package com.larry.cloundusb.cloundusb.fileutil;

/**
 * Created by Larry on 5/24/2016.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;


/*
* 获取文件大小相关信息的工具类
*
*
* */
public class FileSizeUtil {

    private static final int ERROR = -1;

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static double getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {

        long blockSize = 0;
        long totalBlocks = 0;
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (Build.VERSION.SDK_INT > 18) {
                blockSize = stat.getBlockSizeLong();
                totalBlocks = stat.getBlockCountLong();
            } else {
                blockSize = stat.getBlockSize();
                totalBlocks = stat.getBlockCount();
            }

            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取系统总内存
     *
     * @param context 可传入应用程序上下文。
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024l;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存单位为B。
     */
    public static long getAvailableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");




    /*
    * 文件根据大小转换为相应的单位
    *
    *
    *
    * */

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    /*
    * 获取外部空间内存大小
    *
    * 返回结果 获取手机外部内存卡内存空间大小
    * 第一个返回结果总的内存大小
    *
    * 第二个结果是总的剩余空间
    *
    *
    * */
    static public double[] getExternalSDCardStafs(Context context, boolean isremoveable) {
        double blockSize = 0;
        double blockCount = 0;
        double[] result = new double[2];
        double blockFreeCount = 0;
        String path = FileUtil.getStoragePath(context, isremoveable);
        if (path != null) {
            StatFs statfs = new StatFs(path);
            if (Build.VERSION.SDK_INT > 18) {
                blockCount = statfs.getBlockCountLong();
                blockSize = statfs.getBlockSizeLong();
                blockFreeCount = statfs.getAvailableBlocksLong();

            } else {
                blockCount = statfs.getBlockCount();
                blockSize = statfs.getBlockSize();
                blockFreeCount = statfs.getAvailableBlocks();
            }

            result[0] = (blockCount * blockSize);
            result[1] = (blockFreeCount * blockSize);
            return result;
        } else {
            return null;
        }

    }


    /*
    *
    * 获取手机内部存储卡信息
    *
    * 第一个结果返回手内不存储卡的全部大小
    *
    * 第二个结果返回手机内部剩余存储大小
    *
    * */
    static public double[] getPhoneSDaCardStafs() {
        double result[] = new double[2];
        result[0] = getTotalExternalMemorySize();
        result[1] = getAvailableExternalMemorySize();
        return result;
    }

    static public String sdCardstorageInfor() {

        double sdcardResult[] = FileSizeUtil.getExternalSDCardStafs(GetContextUtil.getInstance(), true);

        return FileSizeUtil.convertFileSize((long) sdcardResult[1]) + "/" + FileSizeUtil.convertFileSize((long) sdcardResult[0]);

    }

    static public String phoneStorageInfor() {
        double phoneResult[] = FileSizeUtil.getPhoneSDaCardStafs();
        return FileSizeUtil.convertFileSize((long) phoneResult[1]) + "/" + FileSizeUtil.convertFileSize((long) phoneResult[0]);

    }


}
