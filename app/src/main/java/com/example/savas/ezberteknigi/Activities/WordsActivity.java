package com.example.savas.ezberteknigi.Activities;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordsActivity extends AppCompatActivity
        implements WordsFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String title) {
        setTitle(title);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return WordsFragment.newInstance(Word.WORD_ALL);
            } else if (position == 1){
                return WordsFragment.newInstance(Word.WORD_LEARNING);
            } else if (position == 2){
                return WordsFragment.newInstance(Word.WORD_MASTERED);
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0){
                return "Tüm Kelimeler";
            } else if (position == 1){
                return "Öğreneceklerim";
            } else if (position == 2){
                return "Öğrendiklerim";
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
