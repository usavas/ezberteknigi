package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.savas.ezberteknigi.DAO.ReadingTextDao;
import com.example.savas.ezberteknigi.DAO.WordDao;

import java.util.Date;

@Database(entities = {ReadingText.class, Word.class}, version = 5)
public abstract class EzberTeknigiDatabase extends RoomDatabase {

    private static EzberTeknigiDatabase instance;

    public abstract ReadingTextDao readingTextDao();
    public abstract WordDao wordDao();

    public static synchronized EzberTeknigiDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EzberTeknigiDatabase.class,
                    "ezber_teknigi_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("EzberTeknigiDatabase", "instance= " + ((instance == null) ? "null" : instance.toString()));
            //new PopulateDbAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReadingTextDao readingTextDao;
        private WordDao wordDao;

        private PopulateDbAsyncTask(EzberTeknigiDatabase db) {
            readingTextDao = db.readingTextDao();
            wordDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            wordDao.deleteAllWords();
            readingTextDao.deleteAllReadingTexts();

            readingTextDao.insert(new ReadingText("BBC", "header",  "news", 7, "this is the news content. reading text may be as long as it would", 109));
            readingTextDao.insert(new ReadingText("Guardian", "header1", "news", 6, "this is the news content. reading text may be as long as it would", 99));
            readingTextDao.insert(new ReadingText("Tom Sawyer", "header2", "story", 3, "this is the story content. reading text may be as long as it would", 300));

            wordDao.insert(new Word("word", "kelime", 1, "this is a word example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("book", "kitap", 1, "this is a book example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("music", "müzik", 2, "this is a music example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("art", "sanat", 2, "this is an art example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("ballet", "bale", 2, "this is a ballet example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("theatre", "tiyatro", 2, "this is a theatre example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("nay", "ney", 2, "this is a nay example sentence", new Date(), 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("violin", "keman", 3, "this is a violin example sentence", new Date(), 1, 0, 0, 0, 0, 0));

            return null;
        }
    }
}
