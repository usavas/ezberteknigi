package com.example.savas.ezberteknigi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class AppStarter extends Application {

    public static final String CHANNEL_WORD_REVISION = "CHANNEL_WORD_REVISION";
    public static final String CHANNEL_WORD_OF_THE_DAY = "CHANNEL_WORD_OF_THE_DAY";

//    public static InputStream dictionaryLemmatizer = null;
//    public static InputStream posTaggerMe = null;

    public static POSTaggerME posTaggerMe = null;
    public static DictionaryLemmatizer dictionaryLemmatizer = null;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();

        //TODO: re-activate these lines on production //move them to the opening of the reading text detail activity
//        dictionaryLemmatizer = inflateLemmatizerModel();
//        posTaggerMe = inflatePosModel();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public Context getContext() {
        return getBaseContext();
    }

    public static POSTaggerME getPosTaggerMe() {
        return posTaggerMe;
    }

    public static DictionaryLemmatizer getDictionaryLemmatizer() {
        return dictionaryLemmatizer;
    }

    private DictionaryLemmatizer inflateLemmatizerModel() {
        try {
            return new DictionaryLemmatizer(getBaseContext().getResources().openRawResource(R.raw.en_lemmatizer));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private POSTaggerME inflatePosModel() {
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
        AssetFileDescriptor fd = getBaseContext().getResources().openRawResourceFd(R.raw.en_pos_maxent);
        try {
            return new POSTaggerME(new POSModel(fd.createInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
