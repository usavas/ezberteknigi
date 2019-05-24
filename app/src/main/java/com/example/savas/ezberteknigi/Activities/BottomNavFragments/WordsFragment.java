package com.example.savas.ezberteknigi.Activities.BottomNavFragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Activities.DialogFragments.AddWordFragment;
import com.example.savas.ezberteknigi.Activities.DialogFragments.WordDetailFragment;
import com.example.savas.ezberteknigi.Activities.DialogFragments.WordEditFragment;
import com.example.savas.ezberteknigi.Adapters.WordAdapter;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.List;

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

        wordAdapter = new WordAdapter(getContext());

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(wordAdapter);

        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        if (WORD_LIST_TYPE == 0) { //if learning
            wordViewModel.getAllWordsBasedOnState(Word.WORD_LEARNING).observe(this, words -> wordAdapter.setWords(words));
        } else if (WORD_LIST_TYPE == 1) { // if mastered
            wordViewModel.getAllWordsBasedOnState(Word.WORD_MASTERED).observe(this, new Observer<List<Word>>() {
                @Override
                public void onChanged(@Nullable List<Word> words) {
                    wordAdapter.setWords(words);
                }
            });
        }

        wordAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Word word) {
                Toast.makeText(getContext(), 
                        "if this works, it means overrides the adapter in act.", 
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onItemLongClick(Word word) {
                Toast.makeText(getContext(),
                        "Long click not implemented",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        wordAdapter.setOnOptionsClickListener(new WordAdapter.OnOptionsClickListener() {
            @Override
            public void onDetailClick(Word word) {
                openWordDetailsDialog(word.getWordId());
            }

            @Override
            public void onArchiveClick(Word word, int position) {
                word.setArchived(true);
                wordViewModel.update(word);
                wordAdapter.notifyItemRemoved(position);

                Snackbar mySnackbar = Snackbar.make(view,
                        "Kelime arşivlendi", Snackbar.LENGTH_LONG);
                mySnackbar.setAction("GERİ AL", f -> {
                    word.setArchived(false);
                    wordViewModel.update(word);
                });
                mySnackbar.show();
            }

            @Override
            public void onDeleteClick(Word word) {
                deleteWordWithRollback(word, view);
            }

            @Override
            public void onEditClick(Word word) {
                openWordEditDialog(word);
            }

            @Override
            public void onShareClick(Word word) {
                //TODO: implement word share
            }
        });

        btnAddNewWord.setOnClickListener(v -> {
            openAddNewWordDialog();
        });

        ImplementOnSwipedOnWords(view, wordAdapter, recyclerView);

        return view;
    }

    private void deleteWordWithRollback(Word word, View view) {
        wordViewModel.delete(word);

        Snackbar snackbar = Snackbar.make(view,
                "Kelime silindi", Snackbar.LENGTH_LONG);
        snackbar.setAction("GERİ AL", f-> wordViewModel.insert(word));
        snackbar.show();
    }

    private void openWordDetailsDialog(int wordId) {
        WordDetailFragment wordDetailFragment = WordDetailFragment.newInstance(wordId);
        wordDetailFragment.show(getFragmentManager(), "Kelime Detaylarını Gör");
    }

    private void openAddNewWordDialog() {
        AddWordFragment wordDialogFragment = new AddWordFragment();
        if (WORD_LIST_TYPE == Word.WORD_LEARNING) {
            wordDialogFragment = AddWordFragment.newInstance(Word.WORD_LEARNING);
        } else if (WORD_LIST_TYPE == Word.WORD_MASTERED) {
            wordDialogFragment = AddWordFragment.newInstance(Word.WORD_MASTERED);
        }
        wordDialogFragment.show(getFragmentManager(), "Kelime Ekle");
    }

    private void openWordEditDialog(Word word) {
        if (word.getReadingTextId() != Word.READING_TEXT_ID_DEFAULT) {

            FragmentManager fm = getFragmentManager();
            WordEditFragment fragment = WordEditFragment.newInstance(word);

            fragment.setTargetFragment(WordsFragment.this, 1);
            fragment.show(getFragmentManager(), "Kelimeyi düzenle");

        } else
            Toast.makeText(getContext(),
                    "Bu kelime metinden alındığı için değiştirilemez",
                    Toast.LENGTH_SHORT).show();
    }


    private void ImplementOnSwipedOnWords(View view,
                                          WordAdapter wordAdapter,
                                          RecyclerView recyclerView) {
        if (WORD_LIST_TYPE == Word.WORD_LEARNING) {
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
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
