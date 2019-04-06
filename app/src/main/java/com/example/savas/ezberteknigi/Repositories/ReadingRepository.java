package com.example.savas.ezberteknigi.Repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.savas.ezberteknigi.DAO.ReadingDao;
import com.example.savas.ezberteknigi.Models.EzberTeknigiDatabase;
import com.example.savas.ezberteknigi.Models.Reading;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReadingRepository {
    private ReadingDao readingDao;
    private LiveData<List<Reading>> allReadingTexts;

    public ReadingRepository(Application application) {
        EzberTeknigiDatabase database = EzberTeknigiDatabase.getInstance(application);
        readingDao = database.readingTextDao();
        allReadingTexts = readingDao.getReadingTexts();
    }

    public ReadingRepository(Application application, int docType) {
        EzberTeknigiDatabase database = EzberTeknigiDatabase.getInstance(application);
        readingDao = database.readingTextDao();
        allReadingTexts = readingDao.getReadingTexts(docType);
    }

    public ReadingRepository(Application application, int docType, int docType2) {
        EzberTeknigiDatabase database = EzberTeknigiDatabase.getInstance(application);
        readingDao = database.readingTextDao();
        allReadingTexts = readingDao.getReadingTexts(docType, docType2);
    }

    public void insert(Reading reading) {
        new InsertReadingTextAsyncTask(readingDao).execute(reading);
    }

    public void update(Reading reading) {
        new UpdateReadingTextAsyncTask(readingDao).execute(reading);
    }

    public void delete(Reading reading) {
        new DeleteReadingTextAsyncTask(readingDao).execute(reading);
    }

    public Reading getReadingTextById(int readingTextId){
        try {
            return new GetReadingTextById(readingDao).execute(readingTextId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllReadingTexts() {
        new DeleteAllReadingTextsAsyncTask(readingDao).execute();
    }

    public LiveData<List<Reading>> getAllReadingTexts() {
        return allReadingTexts;
    }

    private static class InsertReadingTextAsyncTask extends AsyncTask<Reading, Void, Void> {
        private ReadingDao readingDao;

        private InsertReadingTextAsyncTask(ReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(Reading... readings) {
            readingDao.insert(readings[0]);
            return null;
        }
    }

    private static class GetReadingTextById extends AsyncTask<Integer, Void, Reading> {
        private ReadingDao readingDao;

        private GetReadingTextById(ReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Reading doInBackground(Integer... ints) {
            return readingDao.getReadingTextById(ints[0]);
        }
    }

    private static class UpdateReadingTextAsyncTask extends AsyncTask<Reading, Void, Void> {
        private ReadingDao readingDao;

        private UpdateReadingTextAsyncTask(ReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(Reading... readings) {
            readingDao.update(readings[0]);
            return null;
        }
    }

    private static class DeleteReadingTextAsyncTask extends AsyncTask<Reading, Void, Void> {
        private ReadingDao readingDao;

        private DeleteReadingTextAsyncTask(ReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(Reading... readings) {
            readingDao.delete(readings[0]);
            return null;
        }
    }

    private static class DeleteAllReadingTextsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReadingDao readingDao;

        private DeleteAllReadingTextsAsyncTask(ReadingDao readingDao) {
            this.readingDao = readingDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readingDao.deleteAllReadingTexts();
            return null;
        }
    }
}
