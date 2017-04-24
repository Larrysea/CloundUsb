package com.larry.cloundusb.cloundusb.testactivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.larry.cloundusb.R;

/**
 * Created by Larry-sea on 2016/8/11.
 */
public class testMagnetic  extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mmagetic;
    TextView tv;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magneatic);
        tv=(TextView)findViewById(R.id.magaetic_tv);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mmagetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        tv.setText(" x  "+event.values[0]+"  Y   "+event.values[1]+"  z  "+event.values[2]);
        // Do something with this sensor value.
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mmagetic, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }




}
