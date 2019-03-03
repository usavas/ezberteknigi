package com.example.savas.ezberteknigi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.BLL.DummyTranslateProvider;
import com.example.savas.ezberteknigi.BLL.ExampleSentenceExtractor;
import com.example.savas.ezberteknigi.BLL.TranslationProvidable;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.BLL.WebContentRetrievable;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class ReadingTextDetailActivity extends AppCompatActivity {

    public WordRepository wordRepository;
    public ReadingTextRepository readingTextRepository;

    public static String WORD_TO_PASS_FOR_TRANSLATION = "ReadingTextDetailActivity.WORD_TO_PASS_FOR_TRANSLATION";
    public static String EXAMPLE_SENTENCE_TO_PASS = "ReadingTextDetailActivity.EXAMPLE_SENTENCE_TO_PASS";

    public static int RESULT_CODE_FOR_READING = 4;

    private ReadingText readingText;
    private TextView tvHeader;
    private TextView tvContent;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readingText = new ReadingText();
        Intent sender = getIntent();
        readingText.setReadingTextId(sender.getIntExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_ID, 0));
        readingText.setHeader(sender.getStringExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_HEADER));
        readingText.setContent(sender.getStringExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_CONTENT));

        //TODO: get single reading text by readingTextId, setView to HTTP web viewer container layout
        readingTextRepository = new ReadingTextRepository(getApplication());
        ReadingText rt = readingTextRepository.getReadingTextById(readingText.getReadingTextId());

        if (!WebContentRetrievable.isValidUrl(rt.getSource())) {
            setContentView(R.layout.activity_http_viewer);
            WebView wv = findViewById(R.id.web_view_reading_text);
            wv.loadUrl(rt.getSource());
            wv.getSettings().setJavaScriptEnabled(true);

            wv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new Handler().postDelayed(() -> {
                        wv.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String selected) {
                                        String selectedText = selected.substring(1, selected.length() - 1).trim();
                                        Toast.makeText(ReadingTextDetailActivity.this, selectedText, Toast.LENGTH_LONG).show();
                                        Log.d("XXXXXXXXXXXXXXXXXXXXXXX", "selected text: " + selectedText + "\tselected: " + selected);

                                        if (selectedText.trim() != "") {
                                            wv.evaluateJavascript("(function(){return document.body.innerText})()",
                                                    new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String text) {
                                                            if (text.trim() != "") {
                                                                Log.d("XXXXXXXXXXXXXXXXXXXXXXX", "sentences: " + text);
                                                                List<String> selectedSentences = ExampleSentenceExtractor
                                                                        .getSentences(text, selectedText);

                                                                String singleSentence = "";
                                                                if (selectedSentences.size() > 0){
                                                                    singleSentence = selectedSentences.get(0);
                                                                    Log.d("XXXXXXXXXXXXXXXXXXXXXXX", "sentence: " + singleSentence);

                                                                }
                                                                showWordDialog(selectedText, singleSentence);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }, 800);

                    return false;
                }
            });

        } else {
            setContentView(R.layout.activity_reading_text_detail);
            tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
            tvContent = findViewById(R.id.text_view_reading_text_detail_content);

            tvHeader.setText(readingText.getHeader());
            tvContent.setText(readingText.getContent());

            tvContent.setOnLongClickListener(v -> {
                new Handler().postDelayed(() -> {
                    int wordSelectionStart = tvContent.getSelectionStart();
                    int wordSelectionEnd = tvContent.getSelectionEnd();

                    if (!verifySelection(wordSelectionStart, wordSelectionEnd)) {
                        return;
                    }

                    String selectedText = tvContent.getText().toString().substring(wordSelectionStart, wordSelectionEnd);
                    String text = tvContent.getText().toString();

                    String selectedSentence = ExampleSentenceExtractor
                            .getSelectedSentence(text, wordSelectionStart, wordSelectionEnd);
                    Log.d("XXXXXXXXXXXXXXXX", "not webview text: " + text);
                    Log.d("XXXXXXXXXXXXXXXX", "not webview sentence: " + selectedSentence);
                    showWordDialog(selectedText, selectedSentence);
                }, 800);
                return false;
            });
        }

    }

    private boolean verifySelection(int wordSelectionStart, int wordSelectionEnd) {
        if ((wordSelectionEnd - wordSelectionStart) > 0) {
            return true;
        }
        return false;
    }

    private void showWordDialog(String wordString, String exampleSentence) {
        if (wordString.trim() != "" && wordString.trim().split(" ").length == 1) {
            Word word = returnWordIfExists(wordString);

            if (word == null) {
                openAddWordDialog(wordString, getTranslation(wordString), exampleSentence);
            } else {
                openWordDetailsDialog(word.getWordId());
            }
        }
    }

    private Word returnWordIfExists(String wordString) {
        try {
            wordRepository = new WordRepository(getApplication());
            Word word = wordRepository.getWordByWord(wordString.trim());
            if (word == null) {
                return null;
            } else {
                return word;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTranslation(String word) {
        TranslationProvidable translatable = new DummyTranslateProvider();
        return translatable.getMeaningOf(word)[0];
    }

    private void openAddWordDialog(String word, String translation, String exampleSentence) {
        AddWordFragment wordDialogFragment = AddWordFragment.newInstance(word, translation, exampleSentence, readingText.getReadingTextId());
        wordDialogFragment.show(getSupportFragmentManager(), "add word");
    }

    private void openWordDetailsDialog(int wordId) {
        WordDetailFragment wordDetailFragment = WordDetailFragment.newInstance(wordId, readingText.getReadingTextId());
        wordDetailFragment.show(getSupportFragmentManager(), "see word details");
    }

}
