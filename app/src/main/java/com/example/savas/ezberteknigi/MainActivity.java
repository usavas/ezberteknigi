package com.example.savas.ezberteknigi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void AddSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getApplication());
        String newsContent = "this is the content of a BBC news page, 109";
        int wordCount = newsContent.length();
        repository.insert(new ReadingText("BBC", "news", 7, newsContent, wordCount));

        Log.d("MainAct", "news inserted");
        Toast.makeText(MainActivity.this, "row saved", Toast.LENGTH_SHORT).show();
    }
}
