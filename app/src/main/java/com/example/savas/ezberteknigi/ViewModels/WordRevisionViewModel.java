package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Repositories.WordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordRevisionViewModel extends AndroidViewModel {

    private WordRepository repository;

    public WordRevisionViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
    }

    public void update(Word word){ repository.update(word); }

    public void delete(Word word){
        repository.delete(word);
    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public LiveData<List<Word>> getNextSeriesForRevision(){
//
//        List<Word> words = new ArrayList<>();
//        List<Word> resultWords;
//
//        resultWords = words.stream()
//                .filter(w-> w.getRevisionPeriodCount() == 0 && w.getTimeElapsedInMinutes() >= 1)
//                .collect(Collectors.toList());
//        if (resultWords.size() > 0) {
//            return (LiveData<List<Word>>) resultWords;
//        }
//
//        resultWords = words.stream()
//                .filter(w-> w.getRevisionPeriodCount() == 1 && w.getTimeElapsedInMinutes() >= 2)
//                .collect(Collectors.toList());
//        if (resultWords.size() > 0) {
//            return (LiveData<List<Word>>) resultWords;
//        }
//
//        resultWords = words.stream()
//                .filter(w-> w.getRevisionPeriodCount() == 2 && w.getTimeElapsedInMinutes() >= 3)
//                .collect(Collectors.toList());
//        if (resultWords.size() > 0) {
//            return (LiveData<List<Word>>) resultWords;
//        }
//
//        resultWords = words.stream()
//                .filter(w-> w.getRevisionPeriodCount() == 3 && w.getTimeElapsedInMinutes() >= 4)
//                .collect(Collectors.toList());
//        if (resultWords.size() > 0) {
//            return (LiveData<List<Word>>) resultWords;
//        }
//
//        return null;
//    }

}
