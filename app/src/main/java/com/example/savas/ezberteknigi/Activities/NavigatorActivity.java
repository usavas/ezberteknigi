package com.example.savas.ezberteknigi.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.WordRevisionScheduler;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class NavigatorActivity extends AppCompatActivity {

    public static final String IS_WORD_FRAGMENT_START = "IS_WORD_FRAGMENT_START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scheduleJob();
        StartMainActivityBasedOnRevision();
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(getBaseContext(), WordRevisionScheduler.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (checkIfJobRunning(scheduler)) return;
        int resultCode = scheduler.schedule(info);
        printResultIfJobStartedOrNot(resultCode);
    }

    private boolean checkIfJobRunning(JobScheduler scheduler) {
        if (Build.VERSION.SDK_INT >= 24){
            JobInfo job = scheduler.getPendingJob(123);
            if (job != null){
                return true;
            }
        } else {
            for (JobInfo job : scheduler.getAllPendingJobs()) {
                if (job.getId() == 123){
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

    private void StartMainActivityBasedOnRevision() {
        Intent i = new Intent(this, MainActivity.class);
        boolean existsRevision = existsWordsToRevise();
        if (existsRevision) i.putExtra(IS_WORD_FRAGMENT_START, true);
        else i.putExtra(IS_WORD_FRAGMENT_START, false);
        startActivity(i);
        finish();
    }

    private boolean existsWordsToRevise(){
        return Word.getWordsToRevise(new WordRepository(getApplication()).getAllWordsAsList()).size() > 0;
    }
}
