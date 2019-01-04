package com.example.savas.ezberteknigi;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.savas.ezberteknigi.DAO.ReadingTextDao;
import com.example.savas.ezberteknigi.DAO.WordDao;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;

@Database(entities = {ReadingText.class, Word.class}, version = 1)
public abstract class EzberTeknigiDatabase extends RoomDatabase {

    private static EzberTeknigiDatabase instance;

    public abstract ReadingTextDao readingTextDao();
    public abstract WordDao wordDao();

    public static synchronized EzberTeknigiDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EzberTeknigiDatabase.class,
                    "ezber_teknigi_database")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
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
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReadingTextDao readingTextDao;

        private PopulateDbAsyncTask(EzberTeknigiDatabase db) {
            readingTextDao = db.readingTextDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readingTextDao.insert(new ReadingText("BBC", "news", 7, "this is the news content. reading text may be as long as it would", 109));
            readingTextDao.insert(new ReadingText("Guardian", "news", 6, "this is the news content. reading text may be as long as it would", 99));
            readingTextDao.insert(new ReadingText("Tom Sawyer", "story", 3, "this is the story content. reading text may be as long as it would", 300));
            return null;
        }
    }
}
