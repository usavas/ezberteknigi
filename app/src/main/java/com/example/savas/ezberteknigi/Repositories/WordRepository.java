package com.example.savas.ezberteknigi.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.savas.ezberteknigi.DAO.WordDao;
import com.example.savas.ezberteknigi.Models.EzberTeknigiDatabase;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Models.WordMinimal;


public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allWords;
    private LiveData<List<Word>> allWordsLearning;
    private LiveData<List<Word>> allWordsMastered;
    private List<WordMinimal> wordsMinimal;

    public WordRepository(Application application) {
        EzberTeknigiDatabase ezberTeknigiDatabase = EzberTeknigiDatabase.getInstance(application);
        wordDao = ezberTeknigiDatabase.wordDao();
        allWords = wordDao.getAllWords();
        allWordsLearning = wordDao.getWordsByWordState(Word.WORD_LEARNING);
        allWordsMastered = wordDao.getWordsByWordState(Word.WORD_MASTERED);

        try {
            wordsMinimal = getWordsMinimal();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Word>> getAllWords(){
        return allWords;
    }

    public LiveData<List<Word>> getAllWordsBasedOnState(int wordState){
        if (wordState == Word.WORD_LEARNING) {
            return allWordsLearning;
        } else if (wordState == Word.WORD_MASTERED){
            return allWordsMastered;
        } else {
            return null;
        }
    }

    public List<WordMinimal> getWordsMinimal() throws ExecutionException, InterruptedException {
        return new GetWordsMinimalAsyncTask(wordDao).execute().get();
    }

    private static class GetWordsMinimalAsyncTask extends AsyncTask<Word, Void, List<WordMinimal>> {
        private WordDao wordDao;

        private GetWordsMinimalAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected List<WordMinimal> doInBackground(Word... words) {
            return wordDao.getAllWordsMinimal();
        }
    }

    public void insert(Word word){
        new InsertWordAsyncTask(wordDao).execute(word);
    }

    public void update(Word word){
        new UpdateWordAsyncTask(wordDao).execute(word);
    }

    public void delete(Word word){
        new DeleteWordAsyncTask(wordDao).execute(word);
    }

    public Boolean existsWord(String word) throws ExecutionException, InterruptedException {
        return new ExistsWordAsyncTask(wordDao).execute(word).get();
    }

    public Word getWordById(int id) throws ExecutionException, InterruptedException {
        return new GetWordByIdAsyncTask(wordDao).execute(id).get();
    }

    public Word getWordByWord(String word) throws ExecutionException, InterruptedException {
        return new GetWordByWordAsyncTask(wordDao).execute(word).get();
    }

    private static class InsertWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        private InsertWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insert(words[0]);
            return null;
        }
    }

    private static class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        private UpdateWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.update(words[0]);
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        private DeleteWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.delete(words[0]);
            return null;
        }
    }

    private static class GetWordByIdAsyncTask extends AsyncTask<Integer, Void, Word>{
        private WordDao wordDao;

        private GetWordByIdAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Word doInBackground(Integer... ints){
            return wordDao.getWordById(ints[0]);
        }
    }

    private static class ExistsWordAsyncTask extends AsyncTask<String, Void, Boolean>{
        private WordDao wordDao;

        private ExistsWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Boolean doInBackground(String... strings){
            return wordDao.wordExists(strings[0]);
        }
    }

    private static class GetWordByWordAsyncTask extends AsyncTask<String, Void, Word>{
        private WordDao wordDao;

        private GetWordByWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Word doInBackground(String ... strings){
            return wordDao.getWordByWord(strings[0]);
        }
    }


}
