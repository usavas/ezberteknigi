package com.example.savas.ezberteknigi.Activities;

import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Activities.DialogFragments.AddWordFragment;
import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

public class WordsFragment extends Fragment {

    private static final String WORD_LIST_TYPE_PARAM = "param1";

    WordViewModel wordViewModel;
    private OnFragmentInteractionListener mListener;

    private int WORD_LIST_TYPE;

    public WordsFragment() {
    }

    public static WordsFragment newInstance(int wordListType) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putInt(WORD_LIST_TYPE_PARAM, wordListType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            WORD_LIST_TYPE = getArguments().getInt(WORD_LIST_TYPE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_words, container, false);
        FloatingActionButton btnAddNewWord = view.findViewById(R.id.fab_add_word_learning);

        WordAdapter wordAdapter;
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_word);

        if (WORD_LIST_TYPE == Word.WORD_REVISION){
            wordAdapter = new WordAdapter(getContext(), true);
            makeRecyclerViewSingleItemAtATime(recyclerView);
        } else {
            wordAdapter = new WordAdapter(getContext());
        }

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
//        if (WORD_LIST_TYPE == Word.WORD_ALL) {
//            btnAddNewWord.setVisibility(View.VISIBLE);
//            wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
//                @Override
//                public void onChanged(@Nullable List<Word> words) {
//                    wordAdapter.setWords(words);
////                    tvItemCount.setText(String.valueOf(words.size() + " words listed"));
//                }
//            });
//        }
        if (WORD_LIST_TYPE == 0) { //if learning
            btnAddNewWord.setVisibility(View.VISIBLE);
            wordViewModel.getAllWordsBasedOnState(Word.WORD_LEARNING).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                }
            });
        } else if (WORD_LIST_TYPE == 1) { // if mastered
//            btnAddNewWord.setVisibility(View.GONE);
            wordViewModel.getAllWordsBasedOnState(Word.WORD_MASTERED).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                }
            });
        } else if (WORD_LIST_TYPE == Word.WORD_REVISION) { //if word revision
            btnAddNewWord.setVisibility(View.GONE);
            getActivity().setTitle("Tekrar Edilecek Kelimeler");
            wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWordsRevision(words);
                }
            });
        }

        wordAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word word) {
                Toast.makeText(getActivity(), "if this works, it means overrides the adapter in act.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onMarkClick(Word word) {
                if (word.getWordState() == Word.WORD_LEARNING){
                    word.setWordState(Word.WORD_MASTERED);
                } else if (word.getWordState() == Word.WORD_MASTERED){
                    word.setWordState(Word.WORD_LEARNING);
                }
                wordViewModel.update(word);
            }
        });

        if (WORD_LIST_TYPE == 0 || WORD_LIST_TYPE == 1) { // if word is learning or mastered = not revised
            btnAddNewWord.setOnClickListener(v -> {
                AddWordFragment wordDialogFragment = new AddWordFragment();
                if (WORD_LIST_TYPE == Word.WORD_ALL){
                    wordDialogFragment = AddWordFragment.newInstance(Word.WORD_ALL);
                } else if(WORD_LIST_TYPE == Word.WORD_LEARNING){
                    wordDialogFragment = AddWordFragment.newInstance(Word.WORD_LEARNING);
                }
                else if (WORD_LIST_TYPE == Word.WORD_MASTERED){
                    wordDialogFragment = AddWordFragment.newInstance(Word.WORD_MASTERED);
                }
                wordDialogFragment.show(getFragmentManager(), "Kelime Ekle");
            });
        }

        ImplementOnSwipedOnWords(view, wordAdapter, recyclerView);

        return view;
    }

    /**
     * Makes the given recyclerView item to show one item at a time
     * @param recyclerView
     */
    private void makeRecyclerViewSingleItemAtATime(RecyclerView recyclerView) {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    private void ImplementOnSwipedOnWords(View view, WordAdapter wordAdapter, RecyclerView recyclerView) {
        if (WORD_LIST_TYPE == Word.WORD_REVISION) {
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
                        Date prevDateToRollBack = word.getDateLastRevision();
                        updateRevision(word);

                        clearWordFromNotificationMenuIfExists(word);

                        Snackbar snackbar = Snackbar.make(view,
                                "Kelime tekrar edildi: " + word.getWord(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setAction("İPTAL ET", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rollBackRevision(word, prevDateToRollBack);
                            }
                        });
                        snackbar.show();
                    }
                }
            }).attachToRecyclerView(recyclerView);
        } else if (WORD_LIST_TYPE == Word.WORD_LEARNING) {
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    if (i == ItemTouchHelper.RIGHT) {

                        Word word = wordAdapter.getWordAt(viewHolder.getAdapterPosition());
                        wordViewModel.markAsMastered(word);

                        Snackbar snackbar = Snackbar.make(view,
                                "Öğrenildi olarak işaretlendi: " + word.getWord(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setAction("GERİ AL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.markAsLearning(word);
                            }
                        });
                        snackbar.show();
                    }
                }
            }).attachToRecyclerView(recyclerView);
        } else if (WORD_LIST_TYPE == Word.WORD_MASTERED) {
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    if (i == ItemTouchHelper.LEFT) {

                        Word word = wordAdapter.getWordAt(viewHolder.getAdapterPosition());
                        wordViewModel.markAsLearning(word);

                        Snackbar snackbar = Snackbar.make(view,
                                "Öğrenilecekler listesine eklendi: " + word.getWord(),
                                Snackbar.LENGTH_LONG);
                        snackbar.setAction("GERİ AL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.markAsMastered(word);
                            }
                        });
                        snackbar.show();
                    }
                }
            }).attachToRecyclerView(recyclerView);
        }
    }

    private void updateRevision(Word word) {
        word.revisionCompleted();
        wordViewModel.update(word);
    }
    private void rollBackRevision(Word word, Date prevDateToRollBack) {
        word.setRevisionPeriodCount(word.getRevisionPeriodCount() - 1);
        word.setDateLastRevision(prevDateToRollBack);
        wordViewModel.update(word);
    }

    /**
     * Checks whether there is any notification currently showing up related to a given word and clears the notification with the given word's id
     * @param word the word which might be shown in the notification
     */
    private void clearWordFromNotificationMenuIfExists(Word word) {
        try{
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(word.getWordId());

            //TODO: if this (group notif) is the last notification, then cancel it, else leave it
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (notificationManager.getActiveNotifications().length == 1){
                    notificationManager.cancel(-1);
                }
            }
        } catch (Exception e){
            Log.d(TAG, "clearWordFromNotificationMenuIfExists: " + e.getMessage());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void updateActivityVariables(String title) {
        if (mListener != null) {
            mListener.onFragmentInteraction(title);
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
        void onFragmentInteraction(String title);
    }
}
