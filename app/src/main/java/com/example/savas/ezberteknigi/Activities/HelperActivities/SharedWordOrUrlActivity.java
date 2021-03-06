package com.example.savas.ezberteknigi.Activities.HelperActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.BLL.Translation.DummyTranslateProvider;
import com.example.savas.ezberteknigi.BLL.Interfaces.TranslationProvidable;
import com.example.savas.ezberteknigi.Data.Models.SimpleArticle;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Data.Repositories.ReadingRepository;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;
import com.example.savas.ezberteknigi.Activities.Services.SaveWebpageIntentService;

public class SharedWordOrUrlActivity extends AppCompatActivity {

    public static final String EXTRA_WORD_TO_GET_TRANSLATION = "com.example.savas.ezberteknigi.EXTRA_WORD_TO_GET_TRANSLATION";

    EditText editWord;
    EditText editWordTranslation;
    EditText editExampleSentence;
    Button btnAddWord;
    Button btnAddWordMastered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVisible(false);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                int selectedTextWordCount = sharedText.split(" ").length;

                if (URLUtil.isValidUrl(sharedText)){

                    Intent intentService = new Intent();
                    intentService.setClass(this, SaveWebpageIntentService.class);
                    intentService.putExtra(SaveWebpageIntentService.WEB_ADDRESS_TO_SAVE, sharedText);
                    startService(intentService);

                    Toast.makeText(this, "Listeye eklendi!", Toast.LENGTH_LONG).show();
                    
                    finish();

                } else if( selectedTextWordCount == 1 || selectedTextWordCount == 2){
                    prepareLayoutForAddWord(sharedText);
                } else if(selectedTextWordCount >= 40){
                    prepareLayoutForAddPlainText(sharedText);
                } else {
                    Toast.makeText(
                            this,
                            "Metinsel değeri eklemek için 40 veya daha fazla kelimeyi içeren bir metin seçin",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void prepareLayoutForAddPlainText(String sharedText){
        setContentView(R.layout.activity_add_word_or_url_plaintext);
        setTitle("Metin ekle");

        TextView tvContent = findViewById(R.id.tv_plaintext_content);
        tvContent.setText("İçerik:\n\n" + sharedText);

        Button btnAddPlainText = findViewById(R.id.button_add_plaintext);
        btnAddPlainText.setOnClickListener(v-> {

            EditText editTitle =  findViewById(R.id.edit_title_plaintext);

            String title = (!editTitle.getText().toString().equals(""))
                    ? editTitle.getText().toString()
                    : "Başlıksız";

            addAsPlainReadingText(sharedText, title);
        });
    }
    private void addAsPlainReadingText(String sharedText, String title) {
        ReadingRepository repo = new ReadingRepository(getApplication(), Reading.DOCUMENT_TYPE_PLAIN);

        Reading r = new Reading();
        r.setDocumentType(Reading.DOCUMENT_TYPE_PLAIN);
        r.setSimpleArticle(new SimpleArticle(sharedText, title));

        repo.insert(r);
        finish();
    }

    private void prepareLayoutForAddWord(String sharedText) {
        setContentView(R.layout.activity_add_shared_word_or_webpage);
        setTitle("Kelime Ekle");

        editWord = findViewById(R.id.edit_word_word_fragment);
        editWordTranslation = findViewById(R.id.edit_word_translation_fragment);
        editExampleSentence = findViewById(R.id.edit_word_example_sentence_fragment);
        btnAddWord = findViewById(R.id.button_add_word_fragment);
        btnAddWordMastered = findViewById(R.id.button_add_word_mastered_fragment);

        editWord.setText(sharedText);
        TranslationProvidable translatable = new DummyTranslateProvider();
        editWordTranslation.setText(translatable.getMeaningOf(sharedText)[0]);

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
