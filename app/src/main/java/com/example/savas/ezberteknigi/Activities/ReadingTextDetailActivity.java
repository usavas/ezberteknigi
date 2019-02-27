package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.ExampleSentenceExtractor;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class ReadingTextDetailActivity extends AppCompatActivity {

    public WordRepository wordRepository;
    public ReadingTextRepository readingTextRepository;

    public static String WORD_TO_PASS_FOR_TRANSLATION = "ReadingTextDetailActivity.WORD_TO_PASS_FOR_TRANSLATION";
    public static String EXAMPLE_SENTENCE_TO_PASS = "ReadingTextDetailActivity.EXAMPLE_SENTENCE_TO_PASS";

    public static int RESULT_CODE_FOR_READING = 4;

    private ReadingText readingText;
    private TextView tvHeader;
    private TextView tvContent;

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
        if (rt.getSource().startsWith("http")){
            setContentView(R.layout.activity_http_handler);
            WebView wv = findViewById(R.id.web_view);
            wv.loadUrl(rt.getSource());
        } else {
            setContentView(R.layout.activity_reading_text_detail);
            tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
            tvContent = findViewById(R.id.text_view_reading_text_detail_content);

            tvHeader.setText(readingText.getHeader());
            tvContent.setText(readingText.getContent());

//        registerForContextMenu(tvContent);

            tvContent.setOnLongClickListener(v -> {
                new Handler().postDelayed(() -> {
                    int wordSelectionStart = tvContent.getSelectionStart();
                    int wordSelectionEnd = tvContent.getSelectionEnd();

                    if (wordSelectionStart > wordSelectionEnd){
                        return;
                    }

                    String selectedText = tvContent.getText().toString().substring(wordSelectionStart, wordSelectionEnd);
                    String text = tvContent.getText().toString();

                    String selectedSentence = ExampleSentenceExtractor
                            .getSelectedSentence(text, wordSelectionStart, wordSelectionEnd);
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
        //TODO: implement word extraction logic
        if (wordString.trim() != ""
                && wordString.trim().split(" ").length == 1) {
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

    private String getTranslation(String word){
        return word + " translation (sample)";
    }

//    private void OpenWordDetailsActivity(Word word) {
//        Intent intent = new Intent(getApplicationContext(), WordDetailActivity.class);
//        intent.putExtra(WordsFragment.EXTRA_WORD_ID, word.getWordId());
//        intent.putExtra(WordsFragment.EXTRA_WORD_WORD, word.getWord());
//        intent.putExtra(WordsFragment.EXTRA_WORD_TRANSLATION, word.getTranslation());
//        intent.putExtra(WordsFragment.EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
//        startActivity(intent);
//    }

//    private void OpenAddWordActivityForResult(String selectedText, String selectedSentence) {
//        Intent intent = new Intent(getApplicationContext(), AddWordActivity.class);
//        intent.putExtra(WORD_TO_PASS_FOR_TRANSLATION, selectedText); //selectedText
//        intent.putExtra(EXAMPLE_SENTENCE_TO_PASS, selectedSentence);
//        startActivityForResult(intent, RESULT_CODE_FOR_READING);
//    }

    private void openAddWordDialog(String word, String translation, String exampleSentence) {
        AddWordFragment wordDialogFragment = AddWordFragment.newInstance(word, translation, exampleSentence, readingText.getReadingTextId());
        wordDialogFragment.show(getSupportFragmentManager(), "add word");
    }

    private void openWordDetailsDialog(int wordId) {
        WordDetailFragment wordDetailFragment= WordDetailFragment.newInstance(wordId, readingText.getReadingTextId());
        wordDetailFragment.show(getSupportFragmentManager(), "see word details");
    }

//    @Override
//    public void insertWord(String word, String translation, String exampleSentence, int learningMastered) {
//        Word w = new Word(word, translation, readingText.getReadingTextId(), exampleSentence);
//        w.setWordState(learningMastered);
//        wordRepository.insert(w);
//
//        Log.d( "READINGTEXTDETAIL", "WORD INSERTED TO DB, masteredLearning: " + learningMastered);
//    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

//         menu.add(0, v.getId(), 0, selectedText);
//
//         ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//         ClipData data = ClipData.newPlainText("copied text", tvContent.getText());
//         clipboard.setPrimaryClip(data);
//    }

    //NOT IMPLEMENTED YET, will be implemented if arises the need
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        if (item.getTitle() == "Save") {
//        } else {
//            return false;
//        }
//        return true;
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_CODE_FOR_READING && resultCode == RESULT_OK) {
//            String wordContent = data.getStringExtra(AddWordActivity.EXTRA_WORD);
//            String wordTranslation = data.getStringExtra(AddWordActivity.EXTRA_TRANSLATION);
//            String exampleSentence = data.getStringExtra(AddWordActivity.EXTRA_EXAMPLE_SENTENCE);
//
//            Word word = new Word(
//                    wordContent,
//                    wordTranslation,
//                    0,
//                    exampleSentence);
//            wordRepository.insert(word);
//            Toast.makeText(this, "Kelime eklendi", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Kelime eklenmedi", Toast.LENGTH_SHORT).show();
//        }
//    }


}
