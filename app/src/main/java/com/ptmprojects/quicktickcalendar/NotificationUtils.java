package com.ptmprojects.quicktickcalendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {
    public static final String CHANNEL_ID = "<< Channel ID >>";
    public static final String CHANNEL_NAME = "< Channel name >";
    private NotificationManager mManager;

    public NotificationUtils(Context base) {
        super(base);
        createChannel();
    }

    public void createChannel() {
        // create android channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            notificationChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            notificationChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            notificationChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);
        }

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getAndroidChannelNotification(String title, String body, PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

    }

}
