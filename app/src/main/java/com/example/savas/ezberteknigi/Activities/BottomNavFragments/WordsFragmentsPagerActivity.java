package com.example.savas.ezberteknigi.Activities.BottomNavFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordsFragmentsPagerActivity extends AppCompatActivity {

    public static final String KEY_READING_ID = "KEY_READING_ID";

    public static int READING_ID = Word.READING_TEXT_ID_DEFAULT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_words_fragment_pager);

        ViewPager mViewPager = findViewById(
                R.id.fragment_word_activity_container);

        mViewPager.setAdapter(
                new SectionsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.fragment_word_activity_tabs);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (READING_ID == Word.READING_TEXT_ID_DEFAULT) {
                if (position == 0) {
                    return WordsFragment.newInstance(Word.WORD_LEARNING);
                } else if (position == 1) {
                    return WordsFragment.newInstance(Word.WORD_MASTERED);
                }
            } else {
                if (position == 0) {
                    return WordsFragment.newInstance(Word.WORD_LEARNING, READING_ID);
                } else if (position == 1) {
                    return WordsFragment.newInstance(Word.WORD_MASTERED, READING_ID);
                }
            }

            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Ezberlenecekler";
            } else if (position == 1) {
                return "Ezberlenenler";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
