package com.example.savas.ezberteknigi.Activities.DialogFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;

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

    public static WordDetailFragment newInstance(int wordId) {
        WordDetailFragment f = new WordDetailFragment();
        _wordId = wordId;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_word_detail, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WordRepository repo = new WordRepository(getActivity().getApplication());
        Word word = null;

        try {
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

    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_dialog_word_detail, null);
//        builder.setView(view);
//
//        return builder.create();
//
//    }
}
