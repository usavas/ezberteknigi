package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.Adapters.TranslationAdapter;
import com.example.savas.ezberteknigi.R;

import java.util.ArrayList;
import java.util.List;

public class TranslationActivity extends AppCompatActivity {

    public static String EXTRA_TRANSLATION_RESULT = "EXTRA_TRANSLATION_RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        final List<String> arrResultOfTranslation = new ArrayList<>();
        final TranslationAdapter adapter = new TranslationAdapter();


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        List<String> translations = new ArrayList<>();
        translations.add("deneme");
        translations.add("deneme2");
        translations.add("deneme3");

        adapter.setTranslations(translations);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String wordToSearch = intent.getStringExtra(AddWordActivity.EXTRA_WORD_TO_GET_TRANSLATION);
        //TODO: implement search and return the translation into adapter here

        Button btnReturn = findViewById(R.id.button_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultOfTranslations = "";
                for (int i = 0; i < arrResultOfTranslation.size(); i++){
                    resultOfTranslations += arrResultOfTranslation.get(i) + ", ";
                }

                Intent intent = new Intent();
                intent.putExtra(EXTRA_TRANSLATION_RESULT, removeLastChar(resultOfTranslations.trim()));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        adapter.setOnItemClickListener(new TranslationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String translation) {
                Toast.makeText(TranslationActivity.this, translation, Toast.LENGTH_SHORT).show();

                if (arrResultOfTranslation.contains(translation)){
                    arrResultOfTranslation.remove(translation);
                }
                else {
                    arrResultOfTranslation.add(translation);
                }
            }
        });

    }

    @NonNull
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
