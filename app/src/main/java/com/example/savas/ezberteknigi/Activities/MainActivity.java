package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Activities.BottomNavFragments.BooksFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.ArticlesFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.SettingsFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordRevisionActivity;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordRevisionFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordsFragmentsPagerFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordsMainFragment;
import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.tools.ant.Main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // default child fragment
        startChildFragment(new WordsMainFragment());

        if (getIntent().getBooleanExtra(
                NavigatorActivity.IS_WORD_FRAGMENT_START,
                false)){
            Intent i = new Intent(MainActivity.this, WordRevisionActivity.class);
            startActivity(i);
        }

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.bottom_navigation_fragment_container).setVisibility(View.VISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_words:
                    selectedFragment = (AppStarter.existsWordsToRevise(getApplication()))
                            ? new WordRevisionFragment()
                            : new WordsMainFragment();
                    break;

                //TODO: this remains for test purposes
                case R.id.navigation_pager_for_test:
                    selectedFragment = new WordsFragmentsPagerFragment();
                    break;

                case R.id.navigation_articles:
                    selectedFragment = new ArticlesFragment();
                    break;
                case R.id.navigation_books:
                    selectedFragment = new BooksFragment();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = new SettingsFragment();
                    break;
            }
            if (selectedFragment != null) {
                startChildFragment(selectedFragment);
            }
            return true;
        }
    };

    private void startChildFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_navigation_fragment_container,
                        fragment)
                .commit();
    }


    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private boolean returnToMainActivityOnBackPressedActive;

    @Override
    public void onBackPressed() {

        if (returnToMainActivityOnBackPressedActive) {
            super.onBackPressed();
            returnToMainActivityOnBackPressedActive = false;
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Çıkmak için tekrar GERİ tuşuna basın!", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 1500);
    }


    private void sendSampleDataToFirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

}
