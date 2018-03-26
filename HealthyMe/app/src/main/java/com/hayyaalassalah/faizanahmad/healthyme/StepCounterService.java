package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Faizan Ahmad on 11/26/2016.
 */
public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    Sensor countSensor;
    boolean isRunning = false;
    int offset = 0;
    public static Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        context = this;
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase sqlRead = dbhelper.getReadableDatabase();
        String date = getTodayDate();
        String query = "select * from steps where date='" + date + "'";
        System.out.println(query);
        Cursor c = sqlRead.rawQuery(query,null);
        int mysteps = 0;
        if(c.moveToNext())
        {
            System.out.println("Came into movetonext");
            try {
                if (intent.hasExtra("broadcast")) {
                    offset = c.getInt(c.getColumnIndex("count"));
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }




        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor!=null)
        {
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
            isRunning = true;
        }
        else
        {
            Toast.makeText(this,"Count sensor not available!",Toast.LENGTH_LONG).show();
        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        isRunning = false;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if(isRunning) {
            //Toast.makeText(this, "Steps from service" + String.valueOf(event.values[0]), Toast.LENGTH_SHORT).show();
            System.out.println("Steps from service" + String.valueOf(event.values[0]));

            String count = String.valueOf(event.values[0]);
            int integerCount = (int) Float.parseFloat(count) + offset;
            if(integerCount % 50 == 0)
            {
                String c = Integer.toString(integerCount);
                sendMessageToActivity(c);
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static void sendMessageToActivity(String steps) {
        Intent intent = new Intent("MySteps");
        intent.putExtra("steps", steps);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public String getTodayDate()
    {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(ca.getTime()).toString();
        return date;
    }
}
