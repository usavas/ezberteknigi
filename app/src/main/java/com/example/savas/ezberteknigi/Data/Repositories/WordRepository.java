package com.example.savas.ezberteknigi.Data.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.savas.ezberteknigi.Data.DAO.WordDao;
import com.example.savas.ezberteknigi.Data.Models.EzberTeknigiDatabase;
import com.example.savas.ezberteknigi.Data.Models.Word;


public class WordRepository {
    private WordDao wordDao;
    private LiveData<List<Word>> allWords;
    private LiveData<List<Word>> wordsLearning;
    private LiveData<List<Word>> wordsMastered;
    private LiveData<List<Word>> wordsRevision;

    public WordRepository(Application application) {
        EzberTeknigiDatabase ezberTeknigiDatabase = EzberTeknigiDatabase.getInstance(application);
        wordDao = ezberTeknigiDatabase.wordDao();
        allWords = wordDao.getAllWords();
        wordsLearning = wordDao.getWordsByWordState(Word.WORD_LEARNING);
        wordsMastered = wordDao.getWordsByWordState(Word.WORD_MASTERED);
    }

    public LiveData<List<Word>> getAllWords(){
        return allWords;
    }

    public LiveData<List<Word>> getWordsBasedOnState(int wordState){
        if (wordState == Word.WORD_LEARNING) {
            return wordsLearning;
        } else if (wordState == Word.WORD_MASTERED){
            return wordsMastered;
        } else {
            return null;
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

    public List<Word> getAllWordsAsList(){
        try {
            return new GetAllWordsAsListAsyncTask(wordDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

    private static class GetAllWordsAsListAsyncTask extends AsyncTask<Void, Void, List<Word>>{
        private WordDao wordDao;

        private GetAllWordsAsListAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected List<Word> doInBackground(Void... voids){
            return wordDao.getAllWordsAsList();
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
