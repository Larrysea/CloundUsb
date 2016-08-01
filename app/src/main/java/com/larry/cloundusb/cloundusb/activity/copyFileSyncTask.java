package com.larry.cloundusb.cloundusb.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.BoringLayout;
import android.widget.Toast;

import com.larry.cloundusb.cloundusb.baseclass.CopyFile;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.File;
import java.net.URL;

import cn.qqtheme.framework.util.FileUtils;

/**
 * Created by LARRYSEA on 2016/7/26.
 * <p/>
 * 显示复制的对话框的异步任务
 */
public class copyFileSyncTask extends AsyncTask<String, Integer, Boolean> {

    ProgressDialog progressDialog;      //progressdialog
    File oldFile;                       //文件原始地址
    File newFile;                       //新的文件地址
    String oldPath;                     //旧的文件路径
    Context mcontext;                   //设备上下文
    Boolean isFinished;                 //是否完成文件复制的标志
    int percent;                        //进度百分比
    /*
    *
    * 构造函数
    *
    * */
    public copyFileSyncTask(String oldPath, Context context) {
        this.oldPath = oldPath;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle("复制");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("正在复制" + FileUtil.getFileNameFromPath(oldFile.getPath()));
    }

    @Override
    protected Boolean doInBackground(String... filePath) {
        oldFile = new File(filePath[0]);
        newFile = new File(filePath[1]);
        android.os.FileUtils.copyFile(oldFile, newFile);
        while (isFinished) {
            percent=(int) ((100 * newFile.length()) / oldFile.length());
            if(percent==100)
            {
             break;
            }
        }
        return true;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Toast.makeText(mcontext, "文件复制成功", Toast.LENGTH_SHORT).show();
    }
}





