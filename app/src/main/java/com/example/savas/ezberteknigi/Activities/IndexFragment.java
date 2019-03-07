package com.example.savas.ezberteknigi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.BLL.InternetConnectivitySocket;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.Services.WordAndTextService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class IndexFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_index, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Index");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn = getActivity().findViewById(R.id.button_index_deneme);
        btn.setVisibility(View.GONE);
        btn.setText("DENEME");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnSearchBooks = getActivity().findViewById(R.id.button_search_books);
        btnSearchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new InternetConnectivitySocket().isConnectedToInternet()){
                    Intent i = new Intent(getContext(), SearchBooksActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "İnternet bağlantısı mevcut değil", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startShareTextService() {
        Log.d(TAG, "onClick: button index deneme clicked");

        Intent intent = new Intent(getActivity(), WordAndTextService.class);
        intent.putExtra(Intent.EXTRA_TEXT, "deneme sharedText");
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        getActivity().startService(intent);
    }

    private void sendDataToFirebase() {
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getActivity().getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("en", "BBC", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
    }

    private void addSampleWord(){
        WordRepository repo = new WordRepository(getActivity().getApplication());
        repo.insert(new Word("word_sample", "translation_sample", 0, "sample_example_sentence"));
    }
}
