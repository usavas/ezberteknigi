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
import com.example.savas.ezberteknigi.Activities.Services.WordRevisionScheduler;


import static android.support.constraint.Constraints.TAG;

public class NavigatorActivity extends AppCompatActivity {

    public static final String IS_WORD_FRAGMENT_START = "IS_WORD_FRAGMENT_START";

    /**
     *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartMainActivityBasedOnRevision();
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
