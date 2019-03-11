package com.example.savas.ezberteknigi;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.List;

public class WordRevisionScheduler extends JobService {

    private static final String TAG = "XXXXXXXXXXXXXXXX";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: WordRevisionJob started");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Word> wordsToRevise = getWordsToRevise();
                if (wordsToRevise.size() > 0) {
                    String wordsToDisplayInNotif = getWordListInString(wordsToRevise);
                    showNotification(wordsToDisplayInNotif);
                }
            }
        }).start();
    }

    private List<Word> getWordsToRevise() {
        WordRepository r = new WordRepository(getApplication());
        List<Word> words = r.getAllWordsAsList();
        return Word.getWordsToRevise(words);
    }

    @NonNull
    private String getWordListInString(List<Word> wordsToRevise) {
        StringBuilder wordsToDisplay = new StringBuilder();
        for (Word w : wordsToRevise) {
            wordsToDisplay.append(w.getWord()).append(", ");
        }
        return wordsToDisplay.toString().substring(0, wordsToDisplay.length() - 2);
    }

    private void showNotification(String wordsToDisplayInNotif) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), AppStarter.CHANNEL_WORD_REVISION)
                .setSmallIcon(R.drawable.button_revision)
                .setContentTitle("Tekrar Edilecek Kelimeler")
                .setContentText(wordsToDisplayInNotif)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    private void testIfThreadRuns() {
        for (int i = 0; i < 10; i++) {
            Log.d(TAG, "run: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "run: job finished");
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
