package com.example.savas.ezberteknigi;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Build;
import android.util.Log;

import com.example.savas.ezberteknigi.Activities.Services.WordRevisionScheduler;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import static android.support.constraint.Constraints.TAG;


public class AppStarter extends Application {

    public static final String CHANNEL_WORD_REVISION = "CHANNEL_WORD_REVISION";
    public static final String CHANNEL_WORD_OF_THE_DAY = "CHANNEL_WORD_OF_THE_DAY";

    private static POSTaggerME mPosTaggerMe = null;
    private static DictionaryLemmatizer mDictionaryLemmatizer = null;
    private static SentenceDetectorME mSentenceDetectorME = null;

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                createNotificationChannels();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mSentenceDetectorME = inflateSentenceDetectorME();
                mDictionaryLemmatizer = inflateLemmatizerModel();
                mPosTaggerMe = inflatePosModel();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                scheduleJob();
            }
        }).start();

    }

    public Context getContext() {
        return getBaseContext();
    }

    public static SentenceDetectorME getSentenceDetectorME(){
        return mSentenceDetectorME;
    }

    public static POSTaggerME getPosTaggerMe() {
        return mPosTaggerMe;
    }

    public static DictionaryLemmatizer getDictionaryLemmatizer() {
        return mDictionaryLemmatizer;
    }


    private SentenceDetectorME inflateSentenceDetectorME() {
        InputStream modelIn = getBaseContext().getResources().openRawResource(R.raw.en_sent);
        try {
            SentenceModel model = new SentenceModel(modelIn);
            return new SentenceDetectorME(model);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    private void scheduleJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (checkIfJobRunning(scheduler)) return;

        ComponentName componentName = new ComponentName(getApplicationContext(), WordRevisionScheduler.class);
        JobInfo info = new JobInfo.Builder(12345, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        int resultCode = scheduler.schedule(info);
        printResultIfJobStartedOrNot(resultCode);
    }

    private boolean checkIfJobRunning(JobScheduler scheduler) {
        if (Build.VERSION.SDK_INT >= 24){
            JobInfo job = scheduler.getPendingJob(12345);
            if (job != null){
                return true;
            }
        } else {
            for (JobInfo job : scheduler.getAllPendingJobs()) {
                if (job.getId() == 12345){
                    return true;
                }
            }
        }
        return false;
    }

    private void printResultIfJobStartedOrNot(int resultCode) {
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    /**
     * even if there is a single item to revise return true
     * @return
     */
    public static boolean existsWordsToRevise(Application application){
        for (Word w : new WordRepository(application).getAllWordsAsList()) {
            if (w.isWordToRevise()){
                return true;
            }
        }

        return false;
    }

}
