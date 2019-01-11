package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import static android.app.Activity.RESULT_OK;

public class WordLearningFragment extends Fragment {

    public static final int ADD_WORD_REQUEST = 1;
    private static final String WORD_TYPE_PARAM = "param1";

    TextView tvItemCount;
    WordViewModel wordViewModel;

    private int mParam1;

    private OnFragmentInteractionListener mListener;

    public WordLearningFragment() {
        // Required empty public constructor
    }

    public static WordLearningFragment newInstance(int param1) {
        WordLearningFragment fragment = new WordLearningFragment();
        Bundle args = new Bundle();
        args.putInt(WORD_TYPE_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(WORD_TYPE_PARAM);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_word_learning, container, false);

        FloatingActionButton buttonAddNote = view.findViewById(R.id.fab_add_word_learning);;
        if (mParam1 == Word.WORD_LEARNING){
            buttonAddNote.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddWordActivity.class);
                startActivityForResult(intent, ADD_WORD_REQUEST);
            });
        } else {

        }

        tvItemCount = view.findViewById(R.id.text_view_item_count_learning);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_word_learning);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final WordAdapter wordAdapter = new WordAdapter();
        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        if (mParam1 == Word.WORD_LEARNING) {
            wordViewModel.getAllWordsBasedOnState(Word.WORD_LEARNING).observe(this, words -> {
                wordAdapter.setWords(words);
                tvItemCount.setText(String.valueOf(words.size() + " words listed"));
            });
        } else if (mParam1 == Word.WORD_MASTERED) {
            wordViewModel.getAllWordsBasedOnState(Word.WORD_MASTERED).observe(this, words -> {
                wordAdapter.setWords(words);
                tvItemCount.setText(String.valueOf(words.size() + " words listed"));
            });
        } else if(mParam1 == Word.WORD_REVISION){
            wordViewModel.getAllWords().observe(this, words -> {
                wordAdapter.setWordsRevision(words);
                tvItemCount.setText(String.valueOf(words.size() + " words listed"));
            });
        }

        wordAdapter.setOnItemClickListener(word -> {
            Intent intent = new Intent(getContext(), WordDetailActivity.class);
            intent.putExtra(WordsActivity.EXTRA_WORD_ID, word.getWordId());
            intent.putExtra(WordsActivity.EXTRA_WORD_WORD, word.getWord());
            intent.putExtra(WordsActivity.EXTRA_WORD_TRANSLATION, word.getTranslation());
            intent.putExtra(WordsActivity.EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            Toast.makeText(getActivity(), "Kelime eklendi", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Kelime eklenmedi", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
