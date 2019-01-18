package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

import static com.example.savas.ezberteknigi.Models.ReadingText.DOCUMENT_TYPE_NEWS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartsReadingTexts;
        btnStartsReadingTexts = findViewById(R.id.button_start_reading_texts);
        btnStartsReadingTexts.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReadingTextsActivity.class);
            startActivity(intent);
        });

        Button btnStartWords;
        btnStartWords = findViewById(R.id.button_start_words);
        btnStartWords.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WordsActivity2.class);
            startActivity(intent);
        });

        Button btnStartWordsLearningMastered;
        btnStartWordsLearningMastered = findViewById(R.id.button_start_words_learning_mastered);
        btnStartWordsLearningMastered.setOnClickListener(v -> {
            Intent intent = new Intent(this.getApplicationContext(), WordsActivity.class);
            startActivity(intent);
        });

        Button btnStartWordsRevision;
        btnStartWordsRevision = findViewById(R.id.button_start_words_revision);
        btnStartWordsRevision.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WordRevisionActivity.class);
            startActivity(intent);
        });

    }



    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("en", "BBC", "sample header", DOCUMENT_TYPE_NEWS, 7, newsContent));

        Log.d("MainAct", "news inserted");
        Toast.makeText(MainActivity.this, "row saved", Toast.LENGTH_SHORT).show();
    }

}
