package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.savas.ezberteknigi.Models.ReadingText.DOCUMENT_TYPE_NEWS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartsReadingTexts;
        btnStartsReadingTexts = findViewById(R.id.button_start_reading_texts);
        btnStartsReadingTexts.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReadingTextsActivity.class);
            startActivity(intent);
        });

        Button btnStartWordsLearningMastered;
        btnStartWordsLearningMastered = findViewById(R.id.button_start_words);
        btnStartWordsLearningMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), WordsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        Button btnStartWordsRevision;
        btnStartWordsRevision = findViewById(R.id.button_start_words_revision);
        btnStartWordsRevision.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WordRevisionActivity.class);
            startActivity(intent);
        });

        Button btnDeneme = findViewById(R.id.button);
        btnDeneme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
                startActivity(intent);
            }
        });
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

        Log.d("MainAct", "news inserted");
        Toast.makeText(MainActivity.this, "row saved", Toast.LENGTH_SHORT).show();
    }

}
