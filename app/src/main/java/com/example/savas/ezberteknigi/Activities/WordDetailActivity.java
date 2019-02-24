package com.example.savas.ezberteknigi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.savas.ezberteknigi.DAO.WordDao;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.concurrent.ExecutionException;

import static com.example.savas.ezberteknigi.Activities.WordsFragment.EXTRA_WORD_ID;

public class WordDetailActivity extends AppCompatActivity {
    public static Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        setTitle("Kelime Detayları");

        populateWordContent();
    }

    private void populateWordContent() {

        Button btnSetWordState = findViewById(R.id.button_set_mastered_or_learning);
        WordRepository repo = new WordRepository(getApplication());
        try {
            word = repo.getWordById(getIntent().getIntExtra((EXTRA_WORD_ID), 0));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (word.getWordState() == Word.WORD_LEARNING){
            btnSetWordState.setText("MARK AS MASTERED");
        } else if (word.getWordState() == Word.WORD_MASTERED){
            btnSetWordState.setText("MARK AS LEARNING");
        }

        btnSetWordState.setOnClickListener(v -> {
            if (word.getWordState() == Word.WORD_LEARNING){
                word.setWordState(Word.WORD_MASTERED);
            }
            else if (word.getWordState() == Word.WORD_MASTERED){
                word.setWordState(Word.WORD_LEARNING);
            }
            repo.update(word);
            finish();
        });

        TextView tvWord = findViewById(R.id.text_view_word_word);
        TextView tvTranslation = findViewById(R.id.text_view_word_translation);
        TextView tvExampleSentences = findViewById(R.id.text_view_word_example_sentence);

        tvWord.setText(word.getWord());
        tvTranslation.setText(word.getTranslation());
        tvExampleSentences.setText(word.getExampleSentence());
    }
}
