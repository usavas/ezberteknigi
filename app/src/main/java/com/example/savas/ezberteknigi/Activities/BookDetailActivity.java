package com.example.savas.ezberteknigi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.savas.ezberteknigi.R;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        getWindow().setEnterTransition(null);


    }
}
