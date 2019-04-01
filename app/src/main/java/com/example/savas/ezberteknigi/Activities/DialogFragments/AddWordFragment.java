package com.example.savas.ezberteknigi.Activities.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

public class AddWordFragment extends AppCompatDialogFragment {
    private static String STARTING_ACTIVITY_TO_ADD_WORD =  "STARTING_ACTIVITY_TO_ADD_WORD";
    private static String STARTING_ACTIVITY_FROM_READING_TEXTS =  "STARTING_ACTIVITY_FROM_READING_TEXTS";
    private static String STARTING_ACTIVITY_FROM_WORDS =  "STARTING_ACTIVITY_FROM_WORDS";

    private static String STARTING_WORD_LEARNING_MASTERED = "STARTING_WORD_LEARNING_MASTERED";

    private EditText editWord;
    private EditText editTranslation;
    private EditText editExampleSentence;

    private static int _readingTextId;

    public static AddWordFragment newInstance(String word, String translation, String exampleSentence, int readingTextId) {
        AddWordFragment f = new AddWordFragment();
        _readingTextId = readingTextId;

        Bundle args = new Bundle();
        args.putString(STARTING_ACTIVITY_TO_ADD_WORD, STARTING_ACTIVITY_FROM_READING_TEXTS);
        args.putString("WORD", word);
        args.putString("TRANSLATION", translation);
        args.putString("EXAMPLE_SENTENCE", exampleSentence);
        f.setArguments(args);
        return f;
    }

    public static AddWordFragment newInstance(int learningOrMastered) {
        AddWordFragment f = new AddWordFragment();
        Bundle args = new Bundle();
        args.putString(STARTING_ACTIVITY_TO_ADD_WORD, STARTING_ACTIVITY_FROM_WORDS);
        args.putInt(STARTING_WORD_LEARNING_MASTERED, learningOrMastered);
        f.setArguments(args);
        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_add_word, null);

        builder.setView(view);

        editWord = view.findViewById(R.id.edit_word_word_fragment);
        editTranslation = view.findViewById(R.id.edit_word_translation_fragment);
        editExampleSentence = view.findViewById(R.id.edit_word_example_sentence_fragment);

        Button btnAddLearning = view.findViewById(R.id.button_add_word_fragment);
        Button btnAddMastered = view.findViewById(R.id.button_add_word_mastered_fragment);

        if (getArguments().getString(STARTING_ACTIVITY_TO_ADD_WORD) == STARTING_ACTIVITY_FROM_READING_TEXTS){
            editWord.setText(getArguments().getString("WORD"));
            editTranslation.setText(getArguments().getString("TRANSLATION"));
            editExampleSentence.setText(getArguments().getString("EXAMPLE_SENTENCE"));
        } else if (getArguments().getString(STARTING_ACTIVITY_TO_ADD_WORD) == STARTING_ACTIVITY_FROM_WORDS) {
            if (getArguments().getInt(STARTING_WORD_LEARNING_MASTERED) == Word.WORD_LEARNING){
                btnAddMastered.setVisibility(View.GONE);
            }
            else if (getArguments().getInt(STARTING_WORD_LEARNING_MASTERED) == Word.WORD_MASTERED){
                btnAddLearning.setVisibility(View.GONE);
            }
        }

        btnAddLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                if (checkIfFilledFields(word, translation)){
                    saveWord(word, translation, exampleSentence, Word.WORD_LEARNING);
                } else {
                    showWordTranslationEmptyError();
                }
            }
        });

        btnAddMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                if (checkIfFilledFields(word, translation)){
                    saveWord(word, translation, exampleSentence, Word.WORD_MASTERED);
                } else {
                    showWordTranslationEmptyError();
                }
            }
        });

        return builder.create();
    }

    private boolean checkIfFilledFields(String word, String translation) {
        return !word.trim().equals("") && !translation.trim().equals("");
    }

    private void showWordTranslationEmptyError() {
        Toast.makeText(getContext(), "Kelime ve çevirisi boş bırakılamaz", Toast.LENGTH_SHORT).show();
//        Snackbar.make(getActivity().findViewById(android.R.id.content), "Kelime ve çevirisi boş bırakılamaz", BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private void saveWord(String word, String translation, String exampleSentence, int learningMastered){
        WordRepository repo = new WordRepository(getActivity().getApplication());
        Word w = new Word(word, translation, _readingTextId, exampleSentence);
        w.setWordState(learningMastered);
        repo.insert(w);
        Snackbar.make(getActivity().findViewById(android.R.id.content), "Kelime eklendi", BaseTransientBottomBar.LENGTH_LONG).show();
        getDialog().dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
