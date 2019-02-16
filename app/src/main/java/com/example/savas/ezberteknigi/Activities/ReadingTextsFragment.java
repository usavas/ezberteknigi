package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Adapters.ReadingTextAdapter;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;

public class ReadingTextsFragment extends Fragment {

    ReadingTextViewModel readingTextViewModel;

    public static String EXTRA_READING_TEXT_DETAIL_HEADER = "EXTRA_READING_TEXT_DETAIL_HEADER";
    public static String EXTRA_READING_TEXT_DETAIL_CONTENT = "EXTRA_READING_TEXT_DETAIL_CONTENT";
    public static String EXTRA_READING_TEXT_DETAIL_ID = "EXTRA_READING_TEXT_DETAIL_ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading_texts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Kayıtlı Hikayeler");

        RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view_reading_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        final ReadingTextAdapter readingTextAdapter = new ReadingTextAdapter();
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
    }
}
