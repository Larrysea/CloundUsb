package com.larry.cloundusb.cloundusb.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


/**     基于  tcp协议的client
 * Created by Larry on 3/28/2016.
 */
public class FileClient extends Service {

    Context mcontext;
    int port;      //端口
    int length;
    byte [] buffer;
    String host;   //主机地址
    Socket socket;
    File mFile;
    public FileClient()
    {

    }


    public  FileClient(File file)
    {

        super();
        mFile=file;

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public void onCreate() {


        buffer=new byte[1024];

        try{


            super.onCreate();
            mcontext=getApplicationContext();
            socket=new Socket();
            socket.bind(null);
            socket.connect((new InetSocketAddress(host, port)), 500);
            InputStream inputStream=null;
            inputStream=new FileInputStream(mFile);
            OutputStream outputStream=socket.getOutputStream();
            while((length =inputStream.read(buffer))!=-1)
            {
                outputStream.write(buffer,0, length);

            }

            outputStream.close();
            inputStream.close();



        }
        catch (FileNotFoundException e)
        {
            Log.e("file not found"," in the fileClient java");
        }
        catch(IOException e)
        {
            Log.e(" born in fileClient","  io EXCEPTION");
        }



        finally {

            if(socket!=null)
            {
                if(socket.isConnected())
                {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


    }




}
