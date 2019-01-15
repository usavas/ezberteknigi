package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

public class WordsActivity extends AppCompatActivity {
    public static final int ADD_WORD_REQUEST = 1;

    public static final String EXTRA_WORD_ID = "EXTRA_WORD_ID";
    public static final String EXTRA_WORD_WORD = "EXTRA_WORD_WORD";
    public static final String EXTRA_WORD_TRANSLATION = "EXTRA_TRANSLATION";
    public static final String EXTRA_WORD_EXAMPLE_SENTENCE = "EXTRA_EXAMPLE_SENTENCE";

    WordViewModel wordViewModel;
    private TextView tvItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_word);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(WordsActivity.this, AddWordActivity.class);
            startActivityForResult(intent, ADD_WORD_REQUEST);
        });

        tvItemCount = findViewById(R.id.tvItemCount);

        final WordAdapter wordAdapter = new WordAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_word);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        wordViewModel.getAllWords().observe(this, words -> {
            wordAdapter.setWords(words);
            tvItemCount.setText(String.valueOf(words.size() + " words listed"));
        });

        wordAdapter.setOnItemClickListener(word -> {
            Intent intent = new Intent(getApplicationContext(), WordDetailActivity.class);
            intent.putExtra(EXTRA_WORD_ID, word.getWordId());
            intent.putExtra(EXTRA_WORD_WORD, word.getWord());
            intent.putExtra(EXTRA_WORD_TRANSLATION, word.getTranslation());
            intent.putExtra(EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_REQUEST && resultCode == RESULT_OK){
            String wordContent = data.getStringExtra(AddWordActivity.EXTRA_WORD);
            String wordTranslation = data.getStringExtra(AddWordActivity.EXTRA_TRANSLATION);
            String exampleSentence = data.getStringExtra(AddWordActivity.EXTRA_EXAMPLE_SENTENCE);

            Word word = new Word(
                    wordContent,
                    wordTranslation,
                    0,
                    exampleSentence);
            wordViewModel.insert(word);
            Toast.makeText(this, "Kelime eklendi", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Kelime eklenmedi", Toast.LENGTH_SHORT).show();
        }
    }
}
