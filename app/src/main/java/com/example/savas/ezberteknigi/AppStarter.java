package com.example.savas.ezberteknigi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class AppStarter extends Application {

    public static final String CHANNEL_WORD_REVISION = "CHANNEL_WORD_REVISION";
    public static final String CHANNEL_WORD_OF_THE_DAY = "CHANNEL_WORD_OF_THE_DAY";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
        scheduleJob();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelWordRevision = new NotificationChannel(
                    CHANNEL_WORD_REVISION,
                    "Kelime Tekrarı",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelWordRevision.setDescription("Periyodik olarak tekrar edilmesi öngörülen kelimeler için bildirimde bulunur.");

            NotificationChannel channelWordOftheDay = new NotificationChannel(
                    CHANNEL_WORD_OF_THE_DAY,
                    "Günün Kelimesi",
                    NotificationManager.IMPORTANCE_LOW
            );
            channelWordOftheDay.setDescription("Her gün sistem tarafından gönderilecek bir İngilizce kelimeyi bildirim olarak görüntüler.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelWordRevision);
            manager.createNotificationChannel(channelWordOftheDay);
        }
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, WordRevisionScheduler.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }
}
