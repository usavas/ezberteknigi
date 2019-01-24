package com.example.savas.ezberteknigi.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordRevisionActivity extends AppCompatActivity
implements WordsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_revision_activity);
        setTitle("Kelime Tekrarları");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WordsFragment.newInstance(Word.WORD_REVISION))
                    .commitNow();
        }
    }

    @Override
    public void onFragmentInteraction(String title) {
        
    }
}
