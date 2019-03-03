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

    private ReadingText readingText;

    private TextView tvHeader;
    private TextView tvContent;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReadingTextRepository readingTextRepository;
        readingTextRepository = new ReadingTextRepository(getApplication());
        readingText = readingTextRepository.getReadingTextById(getIntent().getIntExtra(ReadingTextsFragment.EXTRA_READING_TEXT_DETAIL_ID, 0));

        if (WebContentRetrievable.isValidUrl(readingText.getSource())) {
            prepareLayoutForWebView();
//            prepareLayoutForReadingTextView();
        } else {
            prepareLayoutForReadingTextView();
        }
    }

    private void prepareLayoutForWebView() {
        setContentView(R.layout.activity_http_viewer);
        tvHeader = findViewById(R.id.tv_header_url_address);
        tvHeader.setText(readingText.getHeader());

        WebView wv = findViewById(R.id.web_view_reading_text);
        wv.loadUrl(readingText.getSource());
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

                                    if (selectedText.trim() != "") {
//
//                                        List<String> selectedSentences = ExampleSentenceExtractor
//                                                .getSentences(readingText.getContent(), selectedText);
//
//                                        String singleSentence = "";
//                                        if (selectedSentences.size() > 0) {
//                                            singleSentence = selectedSentences.get(0);
//                                        }
//
//                                        showWordDialog(selectedText, singleSentence);

                                        wv.evaluateJavascript("(function(){return document.body.textContent})()",
                                                new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String text) {
                                                        if (text.trim() != "") {
                                                            List<String> selectedSentences = ExampleSentenceExtractor
                                                                    .getSentences(text, selectedText);

                                                            String singleSentence = "";
                                                            if (selectedSentences.size() > 0){
                                                                singleSentence = selectedSentences.get(0);
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
    }

    private void prepareLayoutForReadingTextView() {
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
            WordRepository wordRepository = new WordRepository(getApplication());
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
