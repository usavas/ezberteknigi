package com.example.savas.ezberteknigi;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class WordRevisionScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: WordRevisionJob started");
        doBakgroundWork(params);
        return true;
    }

    private void doBakgroundWork(JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO: check whether there are words to revise, if yes display notification

            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return true;
    }
}
