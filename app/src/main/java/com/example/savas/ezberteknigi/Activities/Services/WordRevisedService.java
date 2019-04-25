package com.example.savas.ezberteknigi.Activities.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.savas.ezberteknigi.Data.Models.Word;
import com.example.savas.ezberteknigi.Data.Repositories.WordRepository;

import java.util.concurrent.ExecutionException;

public class WordRevisedService extends IntentService {

    /**
     * these 2 methods are required by the system
     */
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
            if (word != null){
                word.revisionCompleted();
                repo.update(word);

                Log.d("XXXXXXXXXXXX", "wordRevised: " + word.toString());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
