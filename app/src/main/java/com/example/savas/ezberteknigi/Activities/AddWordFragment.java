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

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class AddWordFragment extends AppCompatDialogFragment {
    private DialogListener listener;
    private EditText editWord;
    private EditText editTranslation;
    private EditText editExampleSentence;


    public static AddWordFragment newInstance(String word, String translation, String exampleSentence) {
        AddWordFragment f = new AddWordFragment();

        Bundle args = new Bundle();
        args.putString("WORD", word);
        args.putString("TRANSLATION", translation);
        args.putString("EXAMPLE_SENTENCE", exampleSentence);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_word, null);

        builder.setView(view);

        editWord = view.findViewById(R.id.edit_word_word_fragment);
        editTranslation = view.findViewById(R.id.edit_word_translation_fragment);
        editExampleSentence = view.findViewById(R.id.edit_word_example_sentence_fragment);

        editWord.setText(getArguments().getString("WORD"));
        editTranslation.setText(getArguments().getString("TRANSLATION"));
        editExampleSentence.setText(getArguments().getString("EXAMPLE_SENTENCE"));

        Button btnAddLearning = view.findViewById(R.id.button_add_word_fragment);
        btnAddLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                listener.insertWord(word, translation, exampleSentence, Word.WORD_LEARNING);
                getDialog().dismiss();
            }
        });

        Button btnAddMastered = view.findViewById(R.id.button_add_word_mastered_fragment);
        btnAddMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editWord.getText().toString();
                String translation = editTranslation.getText().toString();
                String exampleSentence = editExampleSentence.getText().toString();

                listener.insertWord(word, translation, exampleSentence, Word.WORD_MASTERED);
                getDialog().dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface DialogListener {
        void insertWord(String word, String translation, String exampleSentence, int learningMastered);
    }
}
