package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.ReadingTextAdapter;
import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.Date;
import java.util.List;

public class WordsActivity extends AppCompatActivity {
    public static final int ADD_WORD_REQUEST = 1;

    WordViewModel wordViewModel;
    private TextView tvItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_word);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordsActivity.this, AddWordActivity.class);
                startActivityForResult(intent, ADD_WORD_REQUEST);
            }
        });

        tvItemCount = findViewById(R.id.tvItemCount);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_word);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final WordAdapter wordAdapter = new WordAdapter();
        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable List<Word> words) {
                wordAdapter.setWords(words);
                tvItemCount.setText(String.valueOf(words.size() + " words listed"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_REQUEST && resultCode == RESULT_OK){
            String wordContent = data.getStringExtra(AddWordActivity.EXTRA_WORD);
            String wordTranslation = data.getStringExtra(AddWordActivity.EXTRA_WORD_TRANSLATION);
            String exampleSentence = data.getStringExtra(AddWordActivity.EXTRA_WORD_EXAMPLE_SENTENCE);

            Word word = new Word(
                    wordContent, wordTranslation, 0, exampleSentence, new Date(),
                    0, 0,0,0,0,0);
            wordViewModel.insert(word);
            Toast.makeText(this, "Kelime eklendi", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Kelime eklenmedi", Toast.LENGTH_SHORT).show();
        }
    }
}
