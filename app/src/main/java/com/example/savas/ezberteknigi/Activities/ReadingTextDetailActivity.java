 package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;

 public class ReadingTextDetailActivity extends AppCompatActivity {

     private ReadingText readingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_text_detail);

        readingText = new ReadingText();
        Intent sender = getIntent();
        readingText.setHeader(sender.getStringExtra(ReadingTextsActivity.EXTRA_READING_TEXT_DETAIL_HEADER));
        readingText.setContent(sender.getStringExtra(ReadingTextsActivity.EXTRA_READING_TEXT_DETAIL_CONTENT));

        TextView tvHeader = findViewById(R.id.text_view_reading_text_detail_header);
        TextView tvContent = findViewById(R.id.text_view_reading_text_detail_content);

        tvHeader.setText(readingText.getHeader());
        tvContent.setText(readingText.getContent());

    }
}
