package com.example.savas.ezberteknigi.Activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.WordRevisionViewPagerAdapter;
import com.example.savas.ezberteknigi.Helpers.OnSwipeTouchListener;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.ViewModels.WordViewModel;

import java.util.List;

public class WordRevisionActivity extends AppCompatActivity {

//    WordViewModel wordViewModel;
    ViewPager viewPager;
    WordRevisionViewPagerAdapter adapter;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_revision);

        WordRepository repo = new WordRepository(getApplication());
        List<Word> words = repo.getAllWordsAsList();

        adapter = new WordRevisionViewPagerAdapter(words, this);
        viewPager = findViewById(R.id.view_pager_word_revision);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(0, 0, 0, 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

//        viewPager.setOnTouchListener(new OnSwipeTouchListener(WordRevisionActivity.this){
//            public void onSwipeTop() {
//                Toast.makeText(WordRevisionActivity.this, "swiped up", Toast.LENGTH_LONG).show();
//            }
//            public void onSwipeRight() {
//            }
//            public void onSwipeLeft() {
//            }
//            public void onSwipeBottom() {
//            }
//        });

        viewPager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Toast.makeText(WordRevisionActivity.this, "dragged", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


//        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
//        wordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onChanged(@Nullable List<Word> words) {
//                adapter.setWordsRevision(words);
//            }
//        });

    }
}
