package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordDetailActivity extends AppCompatActivity {
    public static Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);

        populateWordContent();
    }

    private void populateWordContent() {
        word = new Word();
        Intent intent = getIntent();
        word.setWord(intent.getStringExtra(WordsActivity.EXTRA_WORD_WORD));
        word.setTranslation(intent.getStringExtra(WordsActivity.EXTRA_WORD_TRANSLATION));
        word.setExampleSentence(intent.getStringExtra(WordsActivity.EXTRA_WORD_EXAMPLE_SENTENCE));

        TextView tvWord = findViewById(R.id.text_view_word_word);
        TextView tvTranslation = findViewById(R.id.text_view_word_translation);
        TextView tvExampleSentences = findViewById(R.id.text_view_word_example_sentence);

        tvWord.setText(word.getWord());
        tvTranslation.setText(word.getTranslation());
        tvExampleSentences.setText(word.getExampleSentence());
    }
}
