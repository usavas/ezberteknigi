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

public class WordViewModel extends AndroidViewModel {

    private WordRepository repository;
    private LiveData<List<Word>> allWords;
    private LiveData<List<Word>> allWordsLearning;
    private LiveData<List<Word>> allWordsMastered;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
        allWords = repository.getAllWords();
        allWordsLearning = repository.getAllWordsBasedOnState(Word.WORD_LEARNING);
        allWordsMastered = repository.getAllWordsBasedOnState(Word.WORD_MASTERED);
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

    public LiveData<List<Word>> getAllWordsBasedOnState(int wordState) {
        if (wordState == Word.WORD_LEARNING){
            return allWordsLearning;
        }
        else if (wordState == Word.WORD_MASTERED){
            return allWordsMastered;
        }
        else {
            return  null;
        }
    }

}
