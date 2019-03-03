package com.example.savas.ezberteknigi.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;

public class WordsFragmentsContainerFragment extends Fragment
        implements WordsFragment.OnFragmentInteractionListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_words_fragments_container, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Kelimeler");

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        TabLayout tabLayout = getActivity().findViewById(R.id.fragment_word_activity_tabs);
        ViewPager mViewPager = getActivity().findViewById(R.id.fragment_word_activity_container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void onFragmentInteraction(String title) {
        getActivity().setTitle(title);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
