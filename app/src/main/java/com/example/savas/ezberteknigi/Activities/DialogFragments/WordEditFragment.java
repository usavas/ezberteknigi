package com.example.savas.ezberteknigi.Activities.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordsFragment;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.Objects;


public class WordEditFragment extends DialogFragment {

    private static Word _word = null;

    public static WordEditFragment newInstance(Word word) {

        WordEditFragment wordEditDialog = new WordEditFragment();

        _word = word;

        return wordEditDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_word_edit, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText editWord = view.findViewById(R.id.edit_word_word_fragment);
        EditText editTranslation = view.findViewById(R.id.edit_word_translation_fragment);
        EditText editExampleSentence = view.findViewById(R.id.edit_word_example_sentence_fragment);

        editWord.setText(_word.getWord());
        editTranslation.setText(_word.getTranslation());
        editExampleSentence.setText(_word.getExampleSentence());

        editWord.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view.findViewById(R.id.button_save_changes).setOnClickListener(v -> {

            /*
            * These vars are for rolling back to the previous version of the word*/
            final String wordInitialState = _word.getWord();
            final String translationInitialState = _word.getTranslation();
            final String exampleSentenceInitialState = _word.getExampleSentence();

            if (!editWord.getText().toString().equals("")
                    && !editTranslation.getText().toString().equals("")) {
                _word.setWord(editWord.getText().toString());
                _word.setTranslation(editTranslation.getText().toString());
                _word.setExampleSentence(editExampleSentence.getText().toString());

                WordRepository _repo = new WordRepository(getActivity().getApplication());
                _repo.update(_word);

                getDialog().dismiss();

            } else {
                Toast.makeText(getContext(),
                        "Kelime ve çevirisi boş olamaz",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        Fragment f = getParentFragment();
//        WordsFragment parentFrag = ((WordsFragment)WordEditFragment.this.getParentFragment());


//        if (parentFrag instanceof OnWordEditDialogDismissListener) {
//            mCallback = (OnWordEditDialogDismissListener) getParentFragment();
//        } else {
//            throw new RuntimeException("The parent fragment must implement OnDismissListener");
//        }

//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            mCallback = (OnWordEditDialogDismissListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement OnDialogDismissListener");
//        }
    }

}
