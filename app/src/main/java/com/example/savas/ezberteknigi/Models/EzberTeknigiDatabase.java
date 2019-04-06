package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.savas.ezberteknigi.AppStarter;
import com.example.savas.ezberteknigi.DAO.ReadingDao;
import com.example.savas.ezberteknigi.DAO.WordDao;
import com.example.savas.ezberteknigi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = {Reading.class, Word.class},
        version = 20,
        exportSchema = false)
public abstract class EzberTeknigiDatabase extends RoomDatabase {

    private static EzberTeknigiDatabase instance;

    public abstract ReadingDao readingTextDao();
    public abstract WordDao wordDao();

    private static String mLargeText = "";
    private static String mLoremIpsum = "";

    public static synchronized EzberTeknigiDatabase getInstance(Context context){
        if (instance == null){

            mLargeText = context.getResources().getString(R.string.large_text);
            mLoremIpsum = context.getResources().getString(R.string.lorem_ipsum);

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
            new PopulateDbAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReadingDao readingDao;
        private WordDao wordDao;

        private PopulateDbAsyncTask(EzberTeknigiDatabase db) {
            readingDao = db.readingTextDao();
            wordDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            wordDao.deleteAllWords();
            readingDao.deleteAllReadingTexts();

            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLargeText)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLargeText)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLargeText)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLoremIpsum)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLoremIpsum)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLoremIpsum)));
            readingDao.insert(new Reading(Reading.DOCUMENT_TYPE_PLAIN, "en", new SimpleArticle(mLoremIpsum)));

            wordDao.insert(new Word("word", "kelime", 1, "this is a word example sentence"));
            wordDao.insert(new Word("coffee", "kahve", 0, "I drink coffee"));
            wordDao.insert(new Word("soda", "maden suyu", 0, "I drink soda"));
            wordDao.insert(new Word("world", "dünya", 0, "I see world"));
            wordDao.insert(new Word("point", "nokta", 0, "I draw point"));
            wordDao.insert(new Word("sentence", "cümle", 0, "This is rather a long sentence, because some sentences should be longer than usual for test purposes"));
            wordDao.insert(new Word("advertisement", "reklam", 0, "I see advertisement in every single video on youtube"));

            return null;
        }

        Date toDateFromString(String dateInStr){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:MM:SS");
            try {
                return formatter.parse(dateInStr);

            } catch (ParseException e) {
                return null;
            }
        }
    }
}
