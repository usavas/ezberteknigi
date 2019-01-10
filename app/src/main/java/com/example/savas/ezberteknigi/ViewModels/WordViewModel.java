package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.List;
import java.util.stream.Collectors;

public class WordViewModel extends AndroidViewModel {

    private WordRepository repository;
    private LiveData<List<Word>> allWords;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
        allWords = repository.getAllWords();
    }

    public void insert(Word word){
        repository.insert(word);
    }

    public void update(Word word){
        repository.update(word);
    }

    public void delete(Word word){
        repository.delete(word);
    }

    public LiveData<List<Word>> getAllWords(){
        return allWords;
    }
}
