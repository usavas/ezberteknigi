package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;
import com.example.savas.ezberteknigi.WebContentRetrievable;
import com.example.savas.ezberteknigi.WebContentRetrieverViaHttpRequest;
import com.example.savas.ezberteknigi.WebContentRetrieverViaJsoup;

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

    TextView tvHttpAddress;
    Button btnSaveHttpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

                if (sharedText.startsWith("http")){
                    setContentView(R.layout.activity_http_handler);
                    setTitle("Web İçeriği Ekle");

                    tvHttpAddress = findViewById(R.id.tv_url_address);
                    tvHttpAddress.setText(sharedText);

                    btnSaveHttpContent = findViewById(R.id.button_add_http_content);
                    btnSaveHttpContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebContentRetrievable retriever = new WebContentRetrieverViaJsoup();
                            saveHttpContent(sharedText, retriever.retrieveContent(sharedText));
                        }
                    });
                } else {
                    setContentView(R.layout.activity_add_word);
                    setTitle("Kelime Ekle");

                    editWord = findViewById(R.id.edit_word_word_fragment);
                    editWordTranslation = findViewById(R.id.edit_word_translation_fragment);
                    editExampleSentence = findViewById(R.id.edit_word_example_sentence_fragment);
                    btnAddWord = findViewById(R.id.button_add_word_fragment);
                    btnAddWordMastered = findViewById(R.id.button_add_word_mastered_fragment);

                    editWord.setText(sharedText);

                    btnAddWord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveWord(Word.WORD_LEARNING);
                        }
                    });

                    btnAddWordMastered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveWord(Word.WORD_MASTERED);
                        }
                    });
                }
            }
        }
    }

    private void saveHttpContent(String httpAddress, String httpContent){
        //TODO: save http content
        ReadingTextViewModel vm = new ReadingTextViewModel(getApplication());
        vm.insert(new ReadingText("en", httpAddress, "sample_web_header", ReadingText.DOCUMENT_TYPE_OTHER, 7, httpContent));
    }

    private void saveWord(int wordState){
        String word = editWord.getText().toString();
        String wordTranslation = editWordTranslation.getText().toString();
        String exampleSentence = editExampleSentence.getText().toString();

        if (word.trim().isEmpty() || wordTranslation.trim().isEmpty()) {
            Toast.makeText(this, "Lütfen kelime ve çevirisini giriniz", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Word w = new Word(word, wordTranslation, 0, exampleSentence);
            w.setWordState(wordState);

            WordRepository repo = new WordRepository(getApplication());
            repo.insert(w);
        }

        finish();
    }

}
