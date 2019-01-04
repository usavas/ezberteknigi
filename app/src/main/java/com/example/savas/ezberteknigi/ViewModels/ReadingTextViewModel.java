package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.app.ListActivity;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

import java.util.List;

public class ReadingTextViewModel extends AndroidViewModel {

    ReadingTextRepository repository;
    private LiveData<List<ReadingText>> allReadingTexts;

    public ReadingTextViewModel(@NonNull Application application) {
        super(application);
        repository = new ReadingTextRepository(application);
        allReadingTexts = repository.getAllReadingTexts();
    }

    public void insert(ReadingText readingText){
        repository.insert(readingText);
    }

    public void update(ReadingText readingText){
        repository.update(readingText);
    }

    public void delete(ReadingText readingText){
        repository.delete(readingText);
    }

    public LiveData<List<ReadingText>> getAllReadingTexts(){
        return allReadingTexts;
    }
}
