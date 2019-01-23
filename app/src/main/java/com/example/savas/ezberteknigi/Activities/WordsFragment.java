package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class WordsFragment extends Fragment {

    public static final int ADD_WORD_REQUEST = 1;
    private static final String WORD_TYPE_PARAM = "param1";

    public static final String EXTRA_WORD_ID = "EXTRA_WORD_ID";
    public static final String EXTRA_WORD_WORD = "EXTRA_WORD_WORD";
    public static final String EXTRA_WORD_TRANSLATION = "EXTRA_TRANSLATION";
    public static final String EXTRA_WORD_EXAMPLE_SENTENCE = "EXTRA_EXAMPLE_SENTENCE";

    TextView tvItemCount;
    WordViewModel wordViewModel;

    private int mParam1;

    private OnFragmentInteractionListener mListener;

    public WordsFragment() {
        // Required empty public constructor
    }

    public static WordsFragment newInstance(int param1) {
        WordsFragment fragment = new WordsFragment();
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
        final View view = inflater.inflate(R.layout.fragment_words, container, false);
        tvItemCount = view.findViewById(R.id.text_view_item_count_learning);
        FloatingActionButton buttonAddNote = view.findViewById(R.id.fab_add_word_learning);

        final WordAdapter wordAdapter = new WordAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_word);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        if (mParam1 == Word.WORD_ALL) {
            buttonAddNote.setVisibility(View.VISIBLE);
            wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                    tvItemCount.setText(String.valueOf(words.size() + " words listed"));
                }
            });
        } else if (mParam1 == Word.WORD_LEARNING) {
            buttonAddNote.setVisibility(View.VISIBLE);
            wordViewModel.getAllWordsBasedOnState(Word.WORD_LEARNING).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                    tvItemCount.setText(String.valueOf(words.size() + " words listed"));
                }
            });
        } else if (mParam1 == Word.WORD_MASTERED) {
            buttonAddNote.setVisibility(View.GONE);
            wordViewModel.getAllWordsBasedOnState(Word.WORD_MASTERED).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                    tvItemCount.setText(String.valueOf(words.size() + " words listed"));
                }
            });
        } else if (mParam1 == Word.WORD_REVISION) {
            buttonAddNote.setVisibility(View.GONE);
            wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWordsRevision(words);
                    tvItemCount.setText(String.valueOf(words.size() + " words listed"));
                }
            });
        }

        if (mParam1 == Word.WORD_ALL || mParam1 == Word.WORD_LEARNING) {
            buttonAddNote.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AddWordActivity.class);
                startActivityForResult(intent, ADD_WORD_REQUEST);
            });

//            wordAdapter.setOnItemClickListener(word -> {
//                Intent intent = new Intent(getContext(), WordDetailActivity.class);
//                intent.putExtra(EXTRA_WORD_ID, word.getWordId());
//                intent.putExtra(EXTRA_WORD_WORD, word.getWord());
//                intent.putExtra(EXTRA_WORD_TRANSLATION, word.getTranslation());
//                intent.putExtra(EXTRA_WORD_EXAMPLE_SENTENCE, word.getExampleSentence());
//                startActivity(intent);
//            });
        } else {

        }

        if (mParam1 == Word.WORD_REVISION) {
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    if (i == ItemTouchHelper.LEFT || i == ItemTouchHelper.RIGHT) {

                        Word word = wordAdapter.getWordAt(viewHolder.getAdapterPosition());

                        Log.wtf("ELAPSED MINUTES", String.valueOf(word.getTimeElapsedInMinutes()));
                        Log.wtf("DATE SAVED", String.valueOf(word.getDateSaved()));
                        Log.wtf("CURRENT DATE", String.valueOf(new Date()));

                        word.setRevisionPeriodCount(word.getRevisionPeriodCount() + 1);
                        wordViewModel.update(word);

                        Snackbar snackbar = Snackbar.make(view,
                                "Kelime gözden geçirildi: " + word.getWord(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setAction("İPTAL ET", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                word.setRevisionPeriodCount(word.getRevisionPeriodCount() - 1);
                                wordViewModel.update(word);
                            }
                        });
                        snackbar.show();
                    }
                }
            }).attachToRecyclerView(recyclerView);
        }

        if (mParam1 == Word.WORD_MASTERED || mParam1 == Word.WORD_LEARNING) {
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    if (mParam1 == Word.WORD_LEARNING || mParam1 == Word.WORD_MASTERED) {
                        if (i == ItemTouchHelper.DOWN) {
                            //TODO: show item details like in the adapter
                            Snackbar.make(view, "SWIPED DOWN, word item details shown", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }).attachToRecyclerView(recyclerView);
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORD_REQUEST && resultCode == RESULT_OK) {
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
        } else {
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
