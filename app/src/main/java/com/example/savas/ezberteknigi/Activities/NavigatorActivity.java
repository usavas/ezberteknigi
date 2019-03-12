package com.example.savas.ezberteknigi.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.List;

public class NavigatorActivity extends AppCompatActivity {

    public static final String IS_WORD_FRAGMENT_START = "IS_WORD_FRAGMENT_START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(this, MainActivity.class);

        if (existsWordsToRevise()) {
            i.putExtra(IS_WORD_FRAGMENT_START, true);
        } else {
            i.putExtra(IS_WORD_FRAGMENT_START, false);
        }

        startActivity(i);
    }

    private boolean existsWordsToRevise(){
        return new WordRepository(getApplication()).getAllWordsAsList().size() > 0;
    }
}
