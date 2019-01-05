package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class AddWordActivity extends AppCompatActivity {

    public static final String EXTRA_WORD = "com.example.savas.ezberteknigi.EXTRA_WORD";
    public static final String EXTRA_WORD_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_WORD_TRANSLATION";
    public static final String EXTRA_WORD_EXAMPLE_SENTENCE = "com.example.savas.ezberteknigi.EXTRA_WORD_EXAMPLE_SENTENCE";

    EditText editWord;
    EditText editWordTranslation;
    EditText editExampleSentence;
    Button btnAddWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        setTitle("Kelime Ekle");

        editWord = findViewById(R.id.editWord);
        editWordTranslation = findViewById(R.id.editWordTranslation);
        editExampleSentence = findViewById(R.id.editExampleSentence);
        btnAddWord = findViewById(R.id.button_save_word);

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWord();
            }
        });
    }

    private void saveWord(){
        String word = editWord.getText().toString();
        String wordTranslation = editWordTranslation.getText().toString();
        String exampleSentence = editExampleSentence.getText().toString();

        if (word.trim().isEmpty() || wordTranslation.trim().isEmpty()) {
            Toast.makeText(this, "Lütfen kelime ve çevirisini giriniz", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_WORD, word);
        data.putExtra(EXTRA_WORD_TRANSLATION, wordTranslation);
        data.putExtra(EXTRA_WORD_EXAMPLE_SENTENCE, exampleSentence);

        setResult(RESULT_OK, data);
        finish();
    }
}
