package com.example.savas.ezberteknigi.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.savas.ezberteknigi.R;

public class BottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //On device rotation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ReadingTextsFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_documents:
                    selectedFragment = new ReadingTextsFragment();
                    break;
                case R.id.navigation_words:
                    selectedFragment = new WordsFragment();
                    break;
//                case R.id.navigation_revision_words:
//                    selectedFragment = new SearchFragment();
//                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

            return true;

//            switch (item.getItemId()) {
//                case R.id.navigation_documents:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_words:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_revision_words:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
        }
    };


}
