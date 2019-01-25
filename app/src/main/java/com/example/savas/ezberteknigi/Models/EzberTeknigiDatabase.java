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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Database(entities = {ReadingText.class, Word.class}, version = 8)
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
//            new PopulateDbAsyncTask(instance).execute();
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

            String news = "Robert Lloyd Schellenberg was originally given a 15-year jail term in 2018 but after an appeal the court said the sentence was too lenient.\n" +
                    "\n" +
                    "Monday's ruling comes weeks after Canada arrested Meng Wanzhou, a top official at Chinese telecoms giant Huawei, on a request from the US.\n" +
                    "\n" +
                    "Canada condemned the latest ruling.\n" +
                    "\n" +
                    "Prime Minister Justin Trudeau said: \"It is of extreme concern to us as a government, as it should be to all our international friends and allies, that China has chosen to begin to arbitrarily apply the death penalty, as in this case facing a Canadian.\"\n" +
                    "\n" +
                    "China was angered by the arrest of Ms Meng, 46, the daughter of Huawei's founder, and the case has soured its relations with both Canada and the US. She was granted bail in December.\n" +
                    "\n" +
                    "China has since detained two Canadian nationals, accusing them of endangering national security." +
                    "Life of Huawei's high-flying heiress\n" +
                    "What's going on with Huawei?\n" +
                    "What is Schellenberg's case?\n" +
                    "Schellenberg, who is believed to be 36, was arrested in 2014 and accused of planning to smuggle almost 500lb (227kg) of methamphetamine from China to Australia.\n" +
                    "\n" +
                    "He was sentenced to 15 years in prison in November 2018. But following an appeal, a high court in the north-eastern city of Dalian on Monday sentenced the Canadian national to death.\n" +
                    "\n" +
                    "The court also ruled that all of his financial assets must be confiscated.\n" +
                    "\n" +
                    "\"I am not a drug smuggler. I came to China as a tourist,\" Schellenberg said just before the verdict was announced, the AFP news agency reports.\n" +
                    "\n" +
                    "He has 10 days to appeal.\n" +
                    "\n" +
                    "\"All I can really say at this moment is, it is our worst case fear confirmed,\" his aunt, Lauri Nelson-Jones, told the BBC via email.\n" +
                    "\n" +
                    "\"Our thoughts are with Robert at this time. It is rather unimaginable what he must be feeling and thinking. It is a horrific, unfortunate, heartbreaking situation. We anxiously anticipate any news regarding an appeal.\"\n" +
                    "\n" +
                    "China has denied that it is using its legal system to take hostages as bargaining chips in the Huawei case.\n" +
                    "\n" +
                    "But for whatever reason China has suddenly begun working hard to push Schellenberg's case to international prominence, taking the highly unusual step of inviting foreign journalists into the court, the BBC's John Sudworth in Beijing reports.\n" +
                    "\n" +
                    "And despite the Canadian's insistence that he is innocent, his retrial lasted just a day, with his death sentence coming barely an hour after its conclusion, our correspondent says.\n" +
                    "\n" +
                    "What about Meng Wanzhou?\n" +
                    "She was arrested in Vancouver on 1 December, but was granted bail by a Canadian court several days later.\n" +
                    "\n" +
                    "A judge in Canada's western city ruled that she would be under surveillance 24 hours a day and must wear an electronic ankle tag." +
                    "Ms Meng is accused in the US of using a Huawei subsidiary called Skycom to evade sanctions on Iran between 2009 and 2014.\n" +
                    "\n" +
                    "She denies any wrongdoing and says she will contest the allegations.\n" +
                    "\n" +
                    "US President Donald Trump has said he is willing to intervene in the case.\n" +
                    "\n" +
                    "The arrest came against the background of an increasingly acrimonious trade dispute between the US and China.";
            String newsContent = "this is the news content. reading text may be as long as it would this is the news content. " +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it wouldthis " +
                    "is the news content. reading text may be as long as it wouldthis is the news content. reading text may be as " +
                    "long as it wouldthis is the news content. reading text may be as long as it wouldthis is the news content. " +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it wouldthis " +
                    "is the news content. reading text may be as long as it wouldthis is the news content. reading text may be as " +
                    "long as it wouldthis is the news content. reading text may be as long as it wouldthis is the news content. " +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it wouldthis " +
                    "is the news content. reading text may be as long as it wouldthis is the news content. reading text may be as l" +
                    "ong as it wouldthis is the news content. reading text may be as long as it wouldthis is the news content. " +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would" +
                    "reading text may be as long as it wouldthis is the news content. reading text may be as long as it would";

            readingTextDao.insert(new ReadingText("en", "BBC", "header", ReadingText.DOCUMENT_TYPE_NEWS, 7, newsContent));
            readingTextDao.insert(new ReadingText("en", "Guardian", "header1", ReadingText.DOCUMENT_TYPE_NEWS, 6, news));
            readingTextDao.insert(new ReadingText("en", "Tom Sawyer", "header2", ReadingText.DOCUMENT_TYPE_BOOK, 3, "this is the story content. reading text may be as long as it would"));


            long t = Calendar.getInstance().getTimeInMillis();

//            wordDao.insert(new Word("word", "kelime", 1, "this is a word example sentence", new Date(t + 60000)));
//            wordDao.insert(new Word("coffee", "kahve", 0, "I drink coffee", new Date(t + 60000)));
//            wordDao.insert(new Word("soda", "maden suyu", 0, "I drink soda", new Date(t - 10000)) );
//            wordDao.insert(new Word("world", "dünya", 0, "I see world",new Date(t - 10000)));
//            wordDao.insert(new Word("point", "nokta", 0, "I draw point", new Date(t + 10000)));
//            wordDao.insert(new Word("sentence", "cümle", 0, "This is rather a long sentence, because some sentences should be longer than usual for test purposes", new Date(t + 30000)));
//            wordDao.insert(new Word("advertisement", "reklam", 0, "I see advertisement in every single video on youtube", new Date(t + 20000)));

            wordDao.insert(new Word("book", "kitap", 1, "this is a book example sentence",0, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("music", "müzik", 2, "this is a music example sentence", 0, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("art", "sanat", 2, "this is an art example sentence", 0, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("ballet", "bale", 2, "this is a ballet example sentence", 0, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("theatre", "tiyatro", 2, "this is a theatre example sentence", 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("nay", "ney", 2, "this is a nay example sentence", 1, 0, 0, 0, 0, 0));
            wordDao.insert(new Word("violin", "keman", 3, "this is a violin example sentence", 1, 0, 0, 0, 0, 0));

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
