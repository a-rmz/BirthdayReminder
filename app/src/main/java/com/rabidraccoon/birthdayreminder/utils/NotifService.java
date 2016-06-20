package com.rabidraccoon.birthdayreminder.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rabidraccoon.birthdayreminder.ActivityMain;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by alex on 6/5/16.
 */
public class NotifService extends Service {

    ArrayList<Contact> bd;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bd = (intent != null) ? (ArrayList<Contact>) intent.getExtras().get("contacts") : null;
        if(bd != null) validateBirthdays(bd);
        return START_STICKY;
    }

    private void validateBirthdays(ArrayList<Contact> birthdays) {
        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);

        
        for(Contact contact : birthdays) {

            if(DateUtils.isItsBirthday(contact.getDay(), day, contact.getMonth(), month)) {
                Intent startIntent = new Intent(this, ActivityMain.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, contact.getID(), startIntent, 0);

                // Build the notification
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("It's " + contact.getName() + "'s birhtday!")
                        .setContentText("Wish him a happy bd")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(android.R.drawable.btn_radio)
                        // { delay, vibrate, sleep, vibrate, sleep }
                        .setVibrate(new long[] {1000, 300, 300, 300, 300, 300, 0})
                        .setLights(Color.WHITE, 3000, 1000)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Send the notification
                notificationManager.notify(contact.getID(), notification);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
