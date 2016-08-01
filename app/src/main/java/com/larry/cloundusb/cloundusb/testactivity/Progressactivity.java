package com.larry.cloundusb.cloundusb.testactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.larry.cloundusb.R;

/**
 * Created by LARRYSEA on 2016/6/19.
 */
public class Progressactivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
       final  int i=0;
        setContentView(R.layout.testprogressbar);
        final  ProgressBar progressBar=(ProgressBar)findViewById(R.id.test_progressbar);
       // progressBar.setMax(10);
        new Thread(new Runnable() {
            @Override
            public void run() {

                    for(int i=0;i<1000000000;i++)
                    {
                        Math.sqrt(i);
                    }

            }
        }).start();




        new Thread(new Runnable() {
            @Override
            public void run() {
                int position=0;
                try{
                    while(true)
                    {

                        position++;
                        progressBar.setProgress(position);
                        Thread.sleep(1000);
                    }
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();





    }


}
