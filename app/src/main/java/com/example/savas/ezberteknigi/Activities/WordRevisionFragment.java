package com.example.savas.ezberteknigi.Activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordRevisionFragment extends Fragment
implements WordsFragment.OnFragmentInteractionListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_revision, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Kelime Tekrarları");
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.word_revision_container, WordsFragment.newInstance(Word.WORD_REVISION))
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(String title) {

    }
}
