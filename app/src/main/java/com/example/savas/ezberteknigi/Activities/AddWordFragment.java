package com.example.savas.ezberteknigi.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

    public static AddWordFragment newInstance() {
        AddWordFragment f = new AddWordFragment();
        Bundle args = new Bundle();
        args.putString(STARTING_ACTIVITY_TO_ADD_WORD, STARTING_ACTIVITY_FROM_WORDS);
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

        if (getArguments().getString(STARTING_ACTIVITY_TO_ADD_WORD) == STARTING_ACTIVITY_FROM_READING_TEXTS){
            editWord.setText(getArguments().getString("WORD"));
            editTranslation.setText(getArguments().getString("TRANSLATION"));
            editExampleSentence.setText(getArguments().getString("EXAMPLE_SENTENCE"));
        } else if (getArguments().getString(STARTING_ACTIVITY_TO_ADD_WORD) == STARTING_ACTIVITY_FROM_WORDS) {
            //DO NOTHING
        }

        Button btnAddLearning = view.findViewById(R.id.button_add_word_fragment);
        btnAddLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                if (word.trim() != "" && translation.trim() != ""){
                    saveWord(word, translation, exampleSentence, Word.WORD_LEARNING);
                } else {
                    showWordTranslationEmptyError();
                }
            }
        });

        Button btnAddMastered = view.findViewById(R.id.button_add_word_mastered_fragment);
        btnAddMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                if (word.trim() != "" && translation.trim() != ""){
                    saveWord(word, translation, exampleSentence, Word.WORD_MASTERED);
                } else {
                    showWordTranslationEmptyError();
                }
            }
        });

        return builder.create();
    }

    private void showWordTranslationEmptyError() {
        Toast.makeText(getContext(), "Kelime ve çevirisini giriniz!", Toast.LENGTH_LONG).show();
    }

    private void saveWord(String word, String translation, String exampleSentence, int learningMastered){
        WordRepository repo = new WordRepository(getActivity().getApplication());
        Word w = new Word(word, translation, _readingTextId, exampleSentence);
        w.setWordState(learningMastered);
        repo.insert(w);
        getDialog().dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
