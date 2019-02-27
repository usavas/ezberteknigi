package com.example.savas.ezberteknigi.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.RequiresPermission;

import com.example.savas.ezberteknigi.DAO.ReadingTextDao;
import com.example.savas.ezberteknigi.Models.EzberTeknigiDatabase;
import com.example.savas.ezberteknigi.Models.ReadingText;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReadingTextRepository {
    private ReadingTextDao readingTextDao;
    private LiveData<List<ReadingText>> allReadingTexts;

    public ReadingTextRepository(Application application) {
        EzberTeknigiDatabase database = EzberTeknigiDatabase.getInstance(application);
        readingTextDao = database.readingTextDao();
        allReadingTexts = readingTextDao.getAllReadingTexts();
    }

    public void insert(ReadingText readingText) {
        new InsertReadingTextAsyncTask(readingTextDao).execute(readingText);
    }

    public void update(ReadingText readingText) {
        new UpdateReadingTextAsyncTask(readingTextDao).execute(readingText);
    }

    public void delete(ReadingText readingText) {
        new DeleteReadingTextAsyncTask(readingTextDao).execute(readingText);
    }

    public ReadingText getReadingTextById(int readingTextId){
        try {
            return new GetReadingTextById(readingTextDao).execute(readingTextId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllReadingTexts() {
        new DeleteAllReadingTextsAsyncTask(readingTextDao).execute();
    }

    public LiveData<List<ReadingText>> getAllReadingTexts() {
        return allReadingTexts;
    }

    private static class InsertReadingTextAsyncTask extends AsyncTask<ReadingText, Void, Void> {
        private ReadingTextDao readingTextDao;

        private InsertReadingTextAsyncTask(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected Void doInBackground(ReadingText... readingTexts) {
            readingTextDao.insert(readingTexts[0]);
            return null;
        }
    }

    private static class GetReadingTextById extends AsyncTask<Integer, Void, ReadingText> {
        private ReadingTextDao readingTextDao;

        private GetReadingTextById(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected ReadingText doInBackground(Integer... ints) {
            return readingTextDao.getReadingTextById(ints[0]);
        }
    }

    private static class UpdateReadingTextAsyncTask extends AsyncTask<ReadingText, Void, Void> {
        private ReadingTextDao readingTextDao;

        private UpdateReadingTextAsyncTask(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected Void doInBackground(ReadingText... readingTexts) {
            readingTextDao.update(readingTexts[0]);
            return null;
        }
    }

    private static class DeleteReadingTextAsyncTask extends AsyncTask<ReadingText, Void, Void> {
        private ReadingTextDao readingTextDao;

        private DeleteReadingTextAsyncTask(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected Void doInBackground(ReadingText... readingTexts) {
            readingTextDao.delete(readingTexts[0]);
            return null;
        }
    }

    private static class DeleteAllReadingTextsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReadingTextDao readingTextDao;

        private DeleteAllReadingTextsAsyncTask(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readingTextDao.deleteAllReadingTexts();
            return null;
        }
    }
}
