package com.larry.cloundusb.cloundusb.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Larry on 3/28/2016.
 * 服务器接受文件类   基于tcp协议的服务器端
 *
 *
 */
 public   class FileserverAsyncTask extends AsyncTask  {

    Context mContext;


    public FileserverAsyncTask(Context context)
    {
        mContext=context;

    }




    @Override
    protected Object doInBackground(Object[] params) {

      try{
          ServerSocket server=new ServerSocket(8888);
          Socket client=server.accept();

          final    File imagePath=new File(Environment.getExternalStorageDirectory()+"/"+
                  mContext.getPackageName()+"/"+"Image"+System.currentTimeMillis()+"jpg");
          File parentPath=new File(imagePath.getParent());
          if(!parentPath.exists())
          parentPath.mkdirs();
          imagePath.createNewFile();
          InputStream inputStream=client.getInputStream();
          //FileUtil.copyFile(inputStream, new FileOutputStream(imagePath));
          server.close();
          return imagePath.getAbsolutePath();


      }
      catch (FileNotFoundException e)
      {
          Log.e("产生了exception","Fill server ");
          return null;
      }
      catch ( IOException e)
      {
          return null;
      }




    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(o!=null)
        {
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("File://"+o.toString()),"image/*");
            mContext.startActivity(intent);

        }


    }



}



