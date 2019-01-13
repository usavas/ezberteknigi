package com.example.savas.ezberteknigi.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.concurrent.ExecutionException;

import javax.security.auth.callback.Callback;

public class ReadingTextDetailActivity extends AppCompatActivity {

    public WordRepository wordRepository;

    public static String WORD_TO_PASS_FOR_TRANSLATION = "WORD_TO_PASS_FOR_TRANSLATION";
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
                int startIndex = tvContent.getSelectionStart();
                int endIndex = tvContent.getSelectionEnd();

                if ((endIndex - startIndex) <= 0) {
                    return;
                }

                //TODO: if selectedText exists in the words the user saved, return WordDetailActivity with parameters else start the AddWordActivity
                String selectedText = tvContent.getText().toString().substring(startIndex, endIndex);

                //there should be many more conditions and/or regex processes on the string got from edittext
                if (selectedText.trim() != ""
                        && selectedText.trim().split(" ").length == 1){
                    try {
                        //TODO: instead of sending to query, just use the second one
                        if (wordRepository.existsWord(selectedText.trim())){
                            Word word = wordRepository.getWordByWord(selectedText);
                            Intent intent = new Intent(getApplicationContext(), WordDetailActivity.class);
                            intent.putExtra(WordsActivity.EXTRA_WORD_ID, word.getWordId());
                            intent.putExtra(WordsActivity.EXTRA_WORD_WORD, word.getWord());
                            intent.putExtra(WordsActivity.EXTRA_WORD_TRANSLATION, word.getTranslation());
                            intent.putExtra(WordsActivity.EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddWordActivity.class);
                            intent.putExtra(WORD_TO_PASS_FOR_TRANSLATION, selectedText); //selectedText
                            startActivityForResult(intent, RESULT_CODE_FOR_READING);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //TODO: test for developer branch test



            }, 10);
            return false;
        });
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
