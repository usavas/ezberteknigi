package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;

public class NavigatorActivity extends AppCompatActivity {

    public static final String IS_WORD_FRAGMENT_START = "IS_WORD_FRAGMENT_START";

    /**
     *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartMainActivity(AppStarter.existsWordsToRevise(getApplication()));
    }

    /**
     *
     */
    private void StartMainActivity(boolean existsWordsToRevise) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(IS_WORD_FRAGMENT_START, existsWordsToRevise);
        startActivity(i);
        finish();
    }
}
