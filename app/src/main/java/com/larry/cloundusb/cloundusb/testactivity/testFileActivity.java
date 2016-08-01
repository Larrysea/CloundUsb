package com.larry.cloundusb.cloundusb.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.larry.cloundusb.R;
import com.larry.cloundusb.cloundusb.Interneutil.WifiUtil;
import com.larry.cloundusb.cloundusb.fileutil.FileUtil;

import java.io.IOException;

/**
 * Created by Larry on 6/12/2016.
 */
public class testFileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    /*    try{
            FileUtil.createFile("qq.apk");
        }catch (IOException e)
        {
          e.printStackTrace();
            Log.e("文件创建错误",e.getMessage());
        }
*/

    }

}
