package com.example.savas.ezberteknigi.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.WordRevisionScheduler;

import java.util.concurrent.ExecutionException;

public class WordRevisedService extends IntentService {

    public WordRevisedService(String name) {
        super(name);
    }

    public WordRevisedService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int wordId = intent.getIntExtra(WordRevisionScheduler.EXTRA_WORD_TO_SAVE_REVISED, 0);
        wordRevised(wordId);
    }

    private void wordRevised(int wordId) {
        WordRepository repo = new WordRepository(getApplication());
        try {
            Word word = repo.getWordById(wordId);
            word.revisionCompleted();
            repo.update(word);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}