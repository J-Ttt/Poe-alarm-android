package com.feilz.poe_alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class BackgroundService extends Service {

    private final IBinder myBinder = new MyLocalBinder();
    public BackgroundService() {
    }
    //RETURN NEW RANDOM DATA BASED ON Y TIMESTAMPS AND X VALUES
    //TRYING TO SIMULATE THE DATA I'M EXPECTING FROM THE DATABASE
    public Map<Date,Double> newRandomData(){
        double[] values = new double[10];
        Timestamp[] dates = new Timestamp[10];
        Map<Date,Double> returnval = new HashMap<Date, Double>();
        Log.i("service","started");
        long offset = Timestamp.valueOf("2017-11-09 00:00:00").getTime();
        long end = Timestamp.valueOf("2017-11-10 00:00:00").getTime();
        long diff = end-offset+1;
        for (int i=0;i<23;i++){
            Timestamp rand = new Timestamp(offset);
            Date d = new Date();
            d.setTime(rand.getTime());
            //dates[i]=rand;
            double val = (double)(Math.random() * 5 + 8);
            //values[i] = val;
            returnval.put(rand,val);
            offset += 60*60*1000;
        }
        Log.i("service","done");
        return returnval;
    }
/*
    public getNewData(){

    }
*/
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyLocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }
}
