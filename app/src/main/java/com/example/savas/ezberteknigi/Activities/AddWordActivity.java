package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

public class AddWordActivity extends AppCompatActivity {

    public static final int GET_TRANSLATIONS_CODE = 3;

    public static final String EXTRA_WORD = "com.example.savas.ezberteknigi.EXTRA_WORD";
    public static final String EXTRA_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_TRANSLATION";
    public static final String EXTRA_EXAMPLE_SENTENCE = "com.example.savas.ezberteknigi.EXTRA_EXAMPLE_SENTENCE";

    public static final String EXTRA_WORD_TO_GET_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_WORD_TO_GET_TRANSLATION";

    EditText editWord;
    EditText editWordTranslation;
    EditText editExampleSentence;
    Button btnAddWord;
    Button btnAddWordMastered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        setTitle("Kelime Ekle");

        editWord = findViewById(R.id.edit_word_word_fragment);
        editWordTranslation = findViewById(R.id.edit_word_translation_fragment);
        editExampleSentence = findViewById(R.id.edit_word_example_sentence_fragment);
        btnAddWord = findViewById(R.id.button_add_word_fragment);
        btnAddWordMastered = findViewById(R.id.button_add_word_mastered_fragment);

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();
            }
        });

        btnAddWordMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();
            }
        });

        editExampleSentence.setText(getIntent().getStringExtra(ReadingTextDetailActivity.EXAMPLE_SENTENCE_TO_PASS));
        String wordForTranslation = getIntent().getStringExtra(ReadingTextDetailActivity.WORD_TO_PASS_FOR_TRANSLATION);
        if (wordForTranslation != null && wordForTranslation != ""){
            editWord.setText(wordForTranslation);
        }


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText.startsWith("http")){
            //TODO: add content of this to reading text
        } else if (sharedText != null) {
            editWord.setText(sharedText);

        }
    }

    private void saveWord(){
        String word = editWord.getText().toString();
        String wordTranslation = editWordTranslation.getText().toString();
        String exampleSentence = editExampleSentence.getText().toString();

        if (word.trim().isEmpty() || wordTranslation.trim().isEmpty()) {
            Toast.makeText(this, "Lütfen kelime ve çevirisini giriniz", Toast.LENGTH_SHORT).show();
            return;
        } else {
            WordRepository repo = new WordRepository(getApplication());
            repo.insert(new Word(word, wordTranslation, 0, exampleSentence));
        }

        finish();
    }

}
