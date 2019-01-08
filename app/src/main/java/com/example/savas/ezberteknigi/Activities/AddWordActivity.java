package com.example.savas.ezberteknigi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savas.ezberteknigi.R;

public class AddWordActivity extends AppCompatActivity {

    public static final int GET_TRANSLATIONS_CODE = 3;

    public static final String EXTRA_WORD = "com.example.savas.ezberteknigi.EXTRA_WORD";
    public static final String EXTRA_WORD_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_WORD_TRANSLATION";
    public static final String EXTRA_WORD_EXAMPLE_SENTENCE = "com.example.savas.ezberteknigi.EXTRA_WORD_EXAMPLE_SENTENCE";

    public static final String EXTRA_WORD_TO_GET_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_WORD_TO_GET_TRANSLATION";

    EditText editWord;
    EditText editWordTranslation;
    EditText editExampleSentence;
    Button btnAddWord;
    Button btnGetTranslation;
//    ProgressDialog pd;

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

        btnGetTranslation = findViewById(R.id.button_get_translation);
        btnGetTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWordActivity.this, WordAlternativeTranslationsActivity.class);
                intent.putExtra("wordToTranslate", editWord.getText().toString());
                startActivityForResult(intent, GET_TRANSLATIONS_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_TRANSLATIONS_CODE && resultCode == RESULT_OK){
            editWordTranslation.setText(data.getStringExtra(WordAlternativeTranslationsActivity.EXTRA_TRANSLATION_RESULT));
        }
    }
}
