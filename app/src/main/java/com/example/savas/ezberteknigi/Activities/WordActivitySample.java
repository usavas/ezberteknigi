package com.example.savas.ezberteknigi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.WordMinimal;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class WordActivitySample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_sample);

        List<WordMinimal> wordMinimals = new ArrayList<>();
        WordRepository repo = new WordRepository(getApplication());
        try {
            wordMinimals = repo.getWordsMinimal();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LinearLayout ll = findViewById(R.id.linearLayout);
        for (WordMinimal wm : wordMinimals) {
            TextView tv = new TextView(this);
            tv.setText(wm.getWord());
            tv.setTextSize(20);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT));
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            LinearLayout innerL = new LinearLayout(this);
            innerL.setLayoutParams(new ViewGroup.LayoutParams(
                    MATCH_PARENT,
                    WRAP_CONTENT));
            innerL.addView(tv);
            ll.addView(innerL);
        }

    }
}
