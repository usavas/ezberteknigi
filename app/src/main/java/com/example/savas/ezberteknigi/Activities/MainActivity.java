package com.example.savas.ezberteknigi.Activities;

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
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.ReadingsFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.SettingsFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordRevisionFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordsFragment;
import com.example.savas.ezberteknigi.Activities.BottomNavFragments.WordsFragmentsContainerFragment;
import com.example.savas.ezberteknigi.Data.Models.Reading;
import com.example.savas.ezberteknigi.Data.Models.SimpleArticle;
import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Data.Repositories.ReadingRepository;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
implements WordsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (getIntent().getBooleanExtra(NavigatorActivity.IS_WORD_FRAGMENT_START, false)){
            getSupportFragmentManager()
                    .beginTransaction().replace(
                            R.id.bottom_navigation_fragment_container,
                            new WordRevisionFragment())
                    .commit();
            View view = navigation.findViewById(R.id.navigation_revision_words);
            view.performClick();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,
                    new WordsFragmentsContainerFragment()).commit();
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
                    selectedFragment = new WordsFragmentsContainerFragment();
                    break;
                case R.id.navigation_revision_words:
                    selectedFragment = new WordRevisionFragment();
                    break;
                case R.id.navigation_articles:
                    selectedFragment = new ReadingsFragment();
                    break;
                case R.id.navigation_books:
                    selectedFragment = new BooksFragment();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = new SettingsFragment();
                    break;
            }
            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,
                        selectedFragment).commit();
            }
            return true;
        }
    };

    @Override
    public void onFragmentInteraction(String title) {
        setTitle(title);
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
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Çıkmak için tekrar GERİ tuşuna basın!", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 1500);
    }

    private void sendDataToFirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void addSampleNews() {
        ReadingRepository repository = new ReadingRepository(getApplication(), Reading.DOCUMENT_TYPE_PLAIN);
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";

        Reading r = new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(newsContent));

        repository.insert(r);
    }

    private void addSampleWord(){
        WordRepository repo = new WordRepository(getApplication());
        repo.insert(new Word("word_sample", "translation_sample", 0, "sample_example_sentence"));
    }
}
