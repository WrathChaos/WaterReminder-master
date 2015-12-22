package com.water.waterreminder.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.water.waterreminder.MainActivity;
import com.water.waterreminder.R;

public class NotifyService extends Service {

    String[] array = {"water1","water2","water3","water4"};
    int i=0;

    @Override
    public void onStart(Intent intent, int startid) {
        if (i >= 0) {
            NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);


            Notification mNotify = new Notification.Builder(this)
                    .setContentTitle("Water Reminder")
                    .setContentText(array[i])
                    .setSmallIcon(R.drawable.water_reminder_logo)
                    .setContentIntent(pIntent)
                    .setVibrate(new long[]{1000, 1000, 1000})
                    .setTicker("Water Reminder")
                    .build();


            i++;
            if (i >= 4) {
                i = 0;

            }
            mNM.notify(1, mNotify);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
