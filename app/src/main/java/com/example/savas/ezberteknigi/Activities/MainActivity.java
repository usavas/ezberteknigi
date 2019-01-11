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

import static com.example.savas.ezberteknigi.Models.ReadingText.DOCUMENT_TYPE_NEWS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnReadingTexts;
        btnReadingTexts = findViewById(R.id.button_reading_texts);
        btnReadingTexts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReadingTextsActivity.class);
                startActivity(intent);
            }
        });

        Button btnWords;
        btnWords = findViewById(R.id.button_words);
        btnWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WordsActivity.class);
                startActivity(intent);
            }
        });

        Button btnDeneme;
        btnDeneme = findViewById(R.id.button_deneme);
        btnDeneme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("OPEN ACTIVITY", "method called");
                OpenActivity();
            }
        });
    }

    private void OpenActivity(){
        Intent intent = new Intent(this.getApplicationContext(), WordActivity2.class);
        startActivity(intent);
    }

    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("BBC", "sample header", DOCUMENT_TYPE_NEWS, 7, newsContent));

        Log.d("MainAct", "news inserted");
        Toast.makeText(MainActivity.this, "row saved", Toast.LENGTH_SHORT).show();
    }

}
