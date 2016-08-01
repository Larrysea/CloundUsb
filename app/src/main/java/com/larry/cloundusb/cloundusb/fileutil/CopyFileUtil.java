package com.larry.cloundusb.cloundusb.fileutil;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.baseclass.SendFileInform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Larry on 6/17/2016.
 * 文件复制粘贴的工具类，支持多文件粘贴复制
 */
public class CopyFileUtil {
    static final String TAG = "copyfileUtil";
    static List<SendFileInform> copyFileList;//保存备份文件信息
    static String mcopyFile = null;

    /*
    * 存储备份文件信息
    * */
    static public void storageCopyFile(SendFileInform copyFile) {
        if (copyFile != null) {
            copyFileList.add(copyFile);
        }


    }


    public static String getMcopyFile() {
        return mcopyFile;
    }

    public static void setMcopyFile(String mcopyFile) {
        CopyFileUtil.mcopyFile = mcopyFile;
    }

    /*
        *
        * 获取文件备份列表
        * */
    static public List<SendFileInform> getCopyFileList() {
        return copyFileList;
    }

    /*
    *
    * 文件路径粘贴复制
    *
    *
    *
    * 返回0表示文件复制正常
    *
    */
    static public int copy(String fromFile, String toFile) {


        File[] currentFiles;
        File root = new File(fromFile);
        File dest = new File(toFile);
        try {
            if (!root.exists()) {
                root.createNewFile();
            }
            if (!dest.exists()) {
                dest.createNewFile();
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (!root.exists()) {
            return -1;
        }
        if (!root.isDirectory()) {
            FileUtil.copyFile(root, dest);
        } else {
            currentFiles = root.listFiles();
            File targetDir = new File(toFile);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
                {
                    copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

                } else//如果当前项为文件则进行文件拷贝
                {
                    CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
                }
            }
        }

        return 0;
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    static public int CopySdcardFile(String fromFile, String toFile) {

        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;

        } catch (Exception ex) {
            return -1;
        }
    }

    /*
    * 初始化文件备份列表
    *
    *
    * */
    static public void initList() {
        if (copyFileList == null) {
            copyFileList = new ArrayList<SendFileInform>();
        }

    }

    static public int copyFile(SendFileInform sendFileInform, String toFile) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fromFile = sendFileInform.getPath();
        String fileName = sendFileInform.getName();
        File[] currentFiles;
        File root = null;
        File dest = null;
        try {
            root = new File(fromFile);
            dest = new File(toFile, fileName);
            root.mkdir();
            root.mkdir();
            root.createNewFile();
            dest.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!root.exists()) {
            return -1;
        }
        if (!root.isDirectory()) {
            FileUtil.copyFile(root, dest);


        } else {
            currentFiles = root.listFiles();
            File targetDir = new File(toFile);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            //遍历要复制该目录下的全部文件
            for (int i = 0; i < currentFiles.length; i++) {
                if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
                {
                    copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

                } else//如果当前项为文件则进行文件拷贝
                {
                    CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
                }
            }
        }

        return 0;

    }

   static public void storageCopyFile(String path) {
        if (path != null) {
            mcopyFile = path;
        }

    }

    /*
    * 获取文件链表长度
    * */
    public int getCopyFileSize() {
        if (copyFileList != null)
            return copyFileList.size();
        else
            return 0;
    }

    /*
    *
    * 删除备份文件
    * */
    public void deleteFile(String path) {
        if (copyFileList != null)
            for (int position = 0; position < copyFileList.size(); position++) {
                SendFileInform copyFile = copyFileList.get(position);
                if (copyFile.getPath().equals(path)) {
                    copyFileList.remove(position);
                }
            }

    }

    public void clearCopyFileList() {
        if (copyFileList != null) {
            copyFileList.clear();
        }


    }



      /*
      * copyFileUtil特殊的工具类
      *
      * 目的参数是sendFileInform类型
      *
      * */

    public void copyData() {
        ClipboardManager copy = (ClipboardManager) GetContextUtil.getInstance()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        // copy.se

    }


}
