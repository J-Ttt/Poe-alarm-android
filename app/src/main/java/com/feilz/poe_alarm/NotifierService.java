package com.feilz.poe_alarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Joonas on 16.12.2017.
 */

public class NotifierService extends Service{

    NotificationCompat.Builder notif;
    NotificationManager notificationManager;
    private static final int notfID = 12131;
    public void initChannels(Context context) {
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies

        // Here you can return one of some different constants.
        // This one in particular means that if for some reason
        // this service is killed, we don't want to start it
        // again automatically

        //Test Notification
        Log.d("Notifier", "It's on");
        initChannels(this);
        notif = new NotificationCompat.Builder(this, "default");
        notif.setAutoCancel(true);

        sendNotification(this);
        stopSelf();

        return START_NOT_STICKY;
    }
    /*@Override
    public void onDestroy() {
        // service will restart at some point
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 60),
                PendingIntent.getService(this, 0, new Intent(this, NotifierService.class), 0)
        );
    }*/

    private void sendNotification(Context context){

        //Default texts used in testing
        String ticker = "THIS IS TICKER";
        String title = "TITLE";
        String body = "THIS IS A BODY";

        //tells if notification is sent
        boolean shouldSend = true;

        //READ DATABASE HERE AND MODIFY ABOVE VARIABLES ACCORDINGLY

        if (shouldSend) {
            notif.setSmallIcon(R.drawable.main_icon);
            notif.setTicker(ticker);
            notif.setWhen(System.currentTimeMillis());
            notif.setContentTitle(title);
            notif.setContentText(body);

            //sends the user to main page

            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pend = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notif.setContentIntent(pend);


            //build notif
            notificationManager.notify(notfID, notif.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
