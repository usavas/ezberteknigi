package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;

import java.util.List;

public class WordRevisionViewModel extends AndroidViewModel {

    private WordRepository repository;
    private LiveData<List<Word>> revisionWords;

    public WordRevisionViewModel(@NonNull Application application) {
        super(application);
        repository = new WordRepository(application);
        revisionWords = repository.getWordsRevision();
    }

    public void update(Word word){ repository.update(word); }

    public void delete(Word word){
        repository.delete(word);
    }

    public LiveData<List<Word>> getRevisionWords(){
        return revisionWords;
    }
}
