package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.savas.ezberteknigi.Adapters.ReadingTextAdapter;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;

import static android.support.constraint.Constraints.TAG;
import static com.example.savas.ezberteknigi.Activities.WordDetailActivityDeprecated.word;

public class ReadingTextsFragment extends Fragment {

    ReadingTextViewModel readingTextViewModel;

    public static String EXTRA_READING_TEXT_DETAIL_HEADER = "EXTRA_READING_TEXT_DETAIL_HEADER";
    public static String EXTRA_READING_TEXT_DETAIL_CONTENT = "EXTRA_READING_TEXT_DETAIL_CONTENT";
    public static String EXTRA_READING_TEXT_DETAIL_ID = "EXTRA_READING_TEXT_DETAIL_ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_reading_texts, container, false);
        getActivity().setTitle("Kayıtlı Hikayeler");

        final ReadingTextAdapter readingTextAdapter = new ReadingTextAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_reading_text);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(readingTextAdapter);

        readingTextViewModel = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingTextViewModel.getAllReadingTexts().observe(this, readingTexts -> readingTextAdapter.setReadingTexts(readingTexts));

        readingTextAdapter.setOnItemClickListener(new ReadingTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReadingText readingText) {
                Intent intent = new Intent(getActivity(), ReadingTextDetailActivity.class);
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_ID, readingText.getReadingTextId());
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_HEADER, readingText.getHeader());
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_CONTENT, readingText.getContent());
                startActivity(intent);
            }
        });

        Log.d(TAG, "onCreateView: recyclerView: " + recyclerView.toString());
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (i == ItemTouchHelper.LEFT | i == ItemTouchHelper.RIGHT){
                    ReadingText rt = readingTextAdapter.getReadingTextAt(viewHolder.getAdapterPosition());
                    ReadingTextViewModel rtViewModel = new ReadingTextViewModel(getActivity().getApplication());
                    rtViewModel.delete(rt);

                    Snackbar snackbar = Snackbar.make(view,
                            "Okuma metni silindi",
                            Snackbar.LENGTH_LONG);
                    snackbar.setAction("GERİ AL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rtViewModel.insert(rt);
                        }
                    });
                    snackbar.show();
                }
            }
        }).attachToRecyclerView(recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
