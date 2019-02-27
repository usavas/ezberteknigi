package com.example.savas.ezberteknigi.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.concurrent.ExecutionException;


public class WordDetailFragment extends DialogFragment {
    public static int _wordId;
    public static int _readingTextId;

    public static WordDetailFragment newInstance(int wordId, int readingTextId) {
        WordDetailFragment f = new WordDetailFragment();
        _wordId = wordId;
        _readingTextId = readingTextId;
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_word_detail, null);
        builder.setView(view);

        WordRepository repo = new WordRepository(getActivity().getApplication());
        Word word = null;

        try {
            //TODO: may not work right
            word = repo.getWordById(_wordId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView tvWord = view.findViewById(R.id.tv_word_detail_word);
        TextView tvTranslation = view.findViewById(R.id.tv_word_detail_translation);
        TextView tvExampleSentence = view.findViewById(R.id.tv_word_detail_example_sentence);
        Button btnSetWordState = view.findViewById(R.id.button_word_detail_add_mastered_or_learn);

        tvWord.setText(word.getWord());
        tvTranslation.setText(word.getTranslation());
        tvExampleSentence.setText(word.getExampleSentence());

        if (word.getWordState() == Word.WORD_LEARNING){
            btnSetWordState.setText("ÖĞRENDİM");
        } else if (word.getWordState() == Word.WORD_MASTERED){
            btnSetWordState.setText("ÖĞRENECEĞİM");
        }

        Word finalWord = word;
        btnSetWordState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalWord.getWordState() == Word.WORD_LEARNING) {
                    finalWord.setWordState(Word.WORD_MASTERED);
                } else if (finalWord.getWordState() == Word.WORD_MASTERED) {
                    finalWord.setWordState(Word.WORD_LEARNING);
                }
                repo.update(finalWord);
                WordDetailFragment.this.dismiss();
            }
        });

        return builder.create();
    }
}
