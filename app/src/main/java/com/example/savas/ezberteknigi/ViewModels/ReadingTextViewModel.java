package com.example.savas.ezberteknigi.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.savas.ezberteknigi.Models.Reading;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;

import java.util.List;

public class ReadingTextViewModel extends AndroidViewModel {

    ReadingTextRepository repository;
    private LiveData<List<Reading>> allReadingTexts;

    public ReadingTextViewModel(@NonNull Application application) {
        super(application);
        repository = new ReadingTextRepository(application);
        allReadingTexts = repository.getAllReadingTexts();
    }

    public void insert(Reading reading){
        repository.insert(reading);
    }

    public void update(Reading reading){
        repository.update(reading);
    }

    public void delete(Reading reading){
        repository.delete(reading);
    }

    public LiveData<List<Reading>> getAllReadingTexts(){
        return allReadingTexts;
    }
}
