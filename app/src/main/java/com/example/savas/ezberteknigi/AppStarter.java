package com.example.savas.ezberteknigi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.savas.ezberteknigi.Helpers.BufferedReaderHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class AppStarter extends Application {

    public static final String CHANNEL_WORD_REVISION = "CHANNEL_WORD_REVISION";
    public static final String CHANNEL_WORD_OF_THE_DAY = "CHANNEL_WORD_OF_THE_DAY";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        createNotificationChannels();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Log.d("XXXXXXXXXXXXXXX", "source dir: " + AppStarter.getContext().getApplicationInfo().sourceDir);
//        Log.d("XXXXXXXXXXXXXXX", "filesDir: " + AppStarter.getContext().getFilesDir());
//        Log.d("XXXXXXXXXXXXXXX", "assets: " + AppStarter.getContext().getAssets());
        try {
            Log.d("XXXXXXXXXXXXXXX", "pos content: "
                    + BufferedReaderHelper.readFromInputStream(AppStarter.getContext().getAssets()
                    .open("en_pos_maxent.bin.dict")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static Context getContext(){
        return mContext;
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


}
