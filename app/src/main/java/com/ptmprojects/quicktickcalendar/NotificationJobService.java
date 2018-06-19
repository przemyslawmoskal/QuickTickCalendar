package com.ptmprojects.quicktickcalendar;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;

public class NotificationJobService extends JobService {
    private NotificationUtils mNotificationUtils;

    @Override
    public boolean onStartJob(JobParameters params) {
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle("<<< Title of task >>>")
//                .setContentText("< Content Text >")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        PersistableBundle bundleFromParams = params.getExtras();
        String titleFromBundle = bundleFromParams.getString(SingleDayFragment.KEY_TITLE);
        String descriptionFromBundle = bundleFromParams.getString(SingleDayFragment.KEY_DESCRIPTION);
        String dateFromBundle = bundleFromParams.getString(SingleDayFragment.KEY_DATE_FOR_NOTIFICATION);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.DATE_AT_SHOW_UP, dateFromBundle);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        mNotificationUtils = new NotificationUtils(this);
        NotificationCompat.Builder nb = mNotificationUtils.
                getAndroidChannelNotification(titleFromBundle, descriptionFromBundle, pi);

        mNotificationUtils.getManager().notify(101, nb.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
