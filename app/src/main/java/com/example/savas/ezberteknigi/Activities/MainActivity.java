package com.example.savas.ezberteknigi.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.savas.ezberteknigi.Models.ReadingText.DOCUMENT_TYPE_NEWS;

public class MainActivity extends AppCompatActivity
implements WordsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //On device rotation
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,
                    new ReadingTextsFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_documents:
                    selectedFragment = new ReadingTextsFragment();
                    break;
                case R.id.navigation_words:
                    selectedFragment = new WordsFragmentsContainerFragment();
                    break;
                case R.id.navigation_revision_words:
                    selectedFragment = new WordRevisionFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.bottom_navigation_fragment_container,
                    selectedFragment).commit();

            return true;
        }
    };

    @Override
    public void onFragmentInteraction(String title) {
        setTitle(title);
    }


    private void sendDataToFirebase() {
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("en", "BBC", "sample header", DOCUMENT_TYPE_NEWS, 7, newsContent));
    }

    private void addSampleWord(){
        WordRepository repo = new WordRepository(getApplication());
        repo.insert(new Word("word_sample", "translation_sample", 0, "sample_example_sentence"));
    }
}
