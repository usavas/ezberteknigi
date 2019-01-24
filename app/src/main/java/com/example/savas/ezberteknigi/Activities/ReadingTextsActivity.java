package com.example.savas.ezberteknigi.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Adapters.ReadingTextAdapter;
import com.example.savas.ezberteknigi.ViewModels.ReadingTextViewModel;

import java.util.List;

public class ReadingTextsActivity extends AppCompatActivity {
    ReadingTextViewModel readingTextViewModel;

    public static String EXTRA_READING_TEXT_DETAIL_HEADER = "EXTRA_READING_TEXT_DETAIL_HEADER";
    public static String EXTRA_READING_TEXT_DETAIL_CONTENT = "EXTRA_READING_TEXT_DETAIL_CONTENT";
    public static String EXTRA_READING_TEXT_DETAIL_ID = "EXTRA_READING_TEXT_DETAIL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_texts);
        setTitle("Kayıtlı Hikayeler");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_reading_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ReadingTextAdapter readingTextAdapter = new ReadingTextAdapter();
        recyclerView.setAdapter(readingTextAdapter);

        readingTextViewModel = ViewModelProviders.of(this).get(ReadingTextViewModel.class);
        readingTextViewModel.getAllReadingTexts().observe(this, readingTexts -> readingTextAdapter.setReadingTexts(readingTexts));

        readingTextAdapter.setOnItemClickListener(new ReadingTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReadingText readingText) {
                Intent intent = new Intent(ReadingTextsActivity.this.getApplicationContext(), ReadingTextDetailActivity.class);
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_ID, readingText.getReadingTextId());
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_HEADER, readingText.getHeader());
                intent.putExtra(EXTRA_READING_TEXT_DETAIL_CONTENT, readingText.getContent());
                ReadingTextsActivity.this.startActivity(intent);
            }
        });
    }
}
