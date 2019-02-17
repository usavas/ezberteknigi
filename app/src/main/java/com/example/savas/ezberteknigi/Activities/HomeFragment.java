package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.savas.ezberteknigi.Models.ReadingText.DOCUMENT_TYPE_NEWS;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnStartsReadingTexts;
        btnStartsReadingTexts = getActivity().findViewById(R.id.button_start_reading_texts);
        btnStartsReadingTexts.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReadingTextsFragment.class);
            startActivity(intent);
        });

        Button btnStartWordsLearningMastered;
        btnStartWordsLearningMastered = getActivity().findViewById(R.id.button_start_words);
        btnStartWordsLearningMastered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), WordsFragment.class);
                startActivity(intent);
            }
        });

        Button btnStartWordsRevision;
        btnStartWordsRevision = getActivity().findViewById(R.id.button_start_words_revision);
        btnStartWordsRevision.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WordRevisionFragment.class);
            startActivity(intent);
        });

        Button btnDeneme = getActivity().findViewById(R.id.button);
        btnDeneme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendDataToFirebase() {
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getActivity().getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("en", "BBC", "sample header", DOCUMENT_TYPE_NEWS, 7, newsContent));
    }

    private void addSampleWord(){
        WordRepository repo = new WordRepository(getActivity().getApplication());
        repo.insert(new Word("word_sample", "translation_sample", 0, "sample_example_sentence"));
    }

}
