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
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


public class ReadingTextDetailActivity extends AppCompatActivity {

    public WordRepository wordRepository;

    public static String WORD_TO_PASS_FOR_TRANSLATION = "ReadingTextDetailActivity.WORD_TO_PASS_FOR_TRANSLATION";
    public static String EXAMPLE_SENTENCE_TO_PASS = "ReadingTextDetailActivity.EXAMPLE_SENTENCE_TO_PASS";

    public static int RESULT_CODE_FOR_READING = 4;

    private ReadingText readingText;

    private TextView tvHeader;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_text_detail);

        readingText = new ReadingText();
        Intent sender = getIntent();
        readingText.setHeader(sender.getStringExtra(ReadingTextsActivity.EXTRA_READING_TEXT_DETAIL_HEADER));
        readingText.setContent(sender.getStringExtra(ReadingTextsActivity.EXTRA_READING_TEXT_DETAIL_CONTENT));

        tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
        tvContent = findViewById(R.id.text_view_reading_text_detail_content);

        tvHeader.setText(readingText.getHeader());
        tvContent.setText(readingText.getContent());

        wordRepository = new WordRepository(getApplication());
//        registerForContextMenu(tvContent);

        tvContent.setOnLongClickListener(v -> {
            new Handler().postDelayed(() -> {
                int selectionStart = tvContent.getSelectionStart();
                int selectionEnd = tvContent.getSelectionEnd();

                if ((selectionEnd - selectionStart) <= 0) {
                    return;
                }

                String selectedText = tvContent.getText().toString().substring(selectionStart, selectionEnd);
                String text = tvContent.getText().toString();

                String selectedSentence = getSelectedSentence(text, selectionStart, selectionEnd);
                Log.wtf("SELECTED SENTENCE: ", selectedSentence);

                //there should be many more conditions and/or regex processes on the string got from edittext
                if (selectedText.trim() != ""
                        && selectedText.trim().split(" ").length == 1){
                    try {
                        //TODO: instead of sending two queries, just use the second one

                        Word word = wordRepository.getWordByWord(selectedText.trim());
                        if (word != null) {
                            Intent intent = new Intent(getApplicationContext(), WordDetailActivity.class);
                            intent.putExtra(WordsFragment.EXTRA_WORD_ID, word.getWordId());
                            intent.putExtra(WordsFragment.EXTRA_WORD_WORD, word.getWord());
                            intent.putExtra(WordsFragment.EXTRA_WORD_TRANSLATION, word.getTranslation());
                            intent.putExtra(WordsFragment.EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddWordActivity.class);
                            intent.putExtra(WORD_TO_PASS_FOR_TRANSLATION, selectedText); //selectedText
                            intent.putExtra(EXAMPLE_SENTENCE_TO_PASS, selectedSentence);
                            startActivityForResult(intent, RESULT_CODE_FOR_READING);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 10);
            return false;
        });
    }

    public static String getSelectedSentence(String text, int selectionIndex, int finisherIndex){
        Log.wtf("selection index", String.valueOf(selectionIndex));
        Log.wtf("selection index", String.valueOf(finisherIndex));

        return text.substring(getNearestStartingIndex(text, selectionIndex), getNearestFinisherIndex(text, finisherIndex));
    }

    public static List<String> getSentences(String text, String word) {
        final Pattern END_OF_SENTENCE = Pattern.compile("(?<=[.?!(...)])[\\s\\n\\t+]");
        String[] sentences = END_OF_SENTENCE.split(text);
        List<String> sentencesContaining = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.contains(word.toLowerCase())){
                sentencesContaining.add(sentence);
            }
        }
        return sentencesContaining;
    }

    private static int getNearestStartingIndex(String text, int index){
        String[] sentenceSeparators = new String[]{
                "? ", ". ", "! ", "... ", ".. ", ".\n", ".\t"
        };
        int nearestStarterIndex = 0;
        for (String separator: sentenceSeparators) {
            int startingCharIndex = text.lastIndexOf(separator, index) + 1;
            if (startingCharIndex > nearestStarterIndex){
                nearestStarterIndex = startingCharIndex;
            }
        }
        Log.wtf("nearest starter", String.valueOf(nearestStarterIndex));
        return nearestStarterIndex;
    }

    private static int getNearestFinisherIndex(String text, int index){
        String[] sentenceSeparators = new String[]{
                "? ", ". ", "! ", "... ", ".. ", ".\n", ".\t"
        };
        int nearestFinisherIndex = text.length() - 1;
        for (String separator: sentenceSeparators) {
            int finishingCharIndex = text.indexOf(separator, index) + 1;
            if (finishingCharIndex < nearestFinisherIndex && finishingCharIndex > 0){
                nearestFinisherIndex = finishingCharIndex;
            }
        }
        Log.wtf("nearest finisher", String.valueOf(nearestFinisherIndex));
        return nearestFinisherIndex;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

//         menu.add(0, v.getId(), 0, selectedText);
//
//         ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//         ClipData data = ClipData.newPlainText("copied text", tvContent.getText());
//         clipboard.setPrimaryClip(data);
    }

    //NOT IMPLEMENTED YET, will be implemented if arises the need
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Save") {
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE_FOR_READING && resultCode == RESULT_OK) {
            String wordContent = data.getStringExtra(AddWordActivity.EXTRA_WORD);
            String wordTranslation = data.getStringExtra(AddWordActivity.EXTRA_TRANSLATION);
            String exampleSentence = data.getStringExtra(AddWordActivity.EXTRA_EXAMPLE_SENTENCE);

            Word word = new Word(
                    wordContent,
                    wordTranslation,
                    0,
                    exampleSentence);
            wordRepository.insert(word);
            Toast.makeText(this, "Kelime eklendi", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Kelime eklenmedi", Toast.LENGTH_SHORT).show();
        }
    }


}
