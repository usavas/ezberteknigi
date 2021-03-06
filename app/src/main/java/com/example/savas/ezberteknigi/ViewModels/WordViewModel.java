package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;

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
        allWordsLearning = repository.getWordsBasedOnState(Word.WORD_LEARNING);
        allWordsMastered = repository.getWordsBasedOnState(Word.WORD_MASTERED);

    }

    public void insert(Word word){
        repository.insert(word);
    }

    public void update(Word word){
        repository.update(word);
    }

    public void markAsMastered(Word word) {
        word.setWordState(Word.WORD_MASTERED);
        repository.update(word);
    }

    public void markAsLearning(Word word){
        word.setWordState(Word.WORD_LEARNING);
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

    public LiveData<List<Word>> getAllWordsBasedOnStateAndReading(int wordState, int readingId) {
        if (wordState == Word.WORD_LEARNING){
            LiveData<List<Word>> allWordsLearningByReading
                    = repository.getWordsBasedOnStateAndReading(Word.WORD_LEARNING, readingId);
            return allWordsLearningByReading;
        }
        else if (wordState == Word.WORD_MASTERED){
            LiveData<List<Word>> allWordsMasteredByReading
                    = repository.getWordsBasedOnStateAndReading(Word.WORD_MASTERED, readingId);
            return allWordsMasteredByReading;
        }
        else {
            return  null;
        }
    }

}
