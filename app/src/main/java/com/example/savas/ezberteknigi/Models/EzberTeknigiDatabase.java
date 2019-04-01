package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.savas.ezberteknigi.DAO.ReadingTextDao;
import com.example.savas.ezberteknigi.DAO.WordDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = {ReadingText.class, Word.class},
        version = 17,
        exportSchema = false)
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
            new PopulateDbAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
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
            String newsContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Aliquam nulla facilisi cras fermentum odio eu feugiat pretium. Sagittis purus sit amet volutpat consequat mauris nunc congue nisi. Eget felis eget nunc lobortis mattis. Duis tristique sollicitudin nibh sit amet commodo nulla. Morbi enim nunc faucibus a pellentesque sit. Enim sed faucibus turpis in eu mi bibendum neque egestas. Mauris cursus mattis molestie a iaculis. Dignissim convallis aenean et tortor at risus viverra adipiscing. Urna cursus eget nunc scelerisque viverra. Eu sem integer vitae justo eget magna fermentum iaculis. Auctor eu augue ut lectus arcu bibendum at. Fermentum iaculis eu non diam. Tellus orci ac auctor augue mauris augue neque gravida in. Ullamcorper sit amet risus nullam eget. Sodales ut etiam sit amet nisl purus in mollis. Amet consectetur adipiscing elit ut aliquam. Ullamcorper velit sed ullamcorper morbi tincidunt ornare massa.\n" +
                    "\n" +
                    "Laoreet id donec ultrices tincidunt. Commodo odio aenean sed adipiscing diam. Nunc vel risus commodo viverra maecenas accumsan lacus. Ultrices gravida dictum fusce ut placerat. Sed viverra tellus in hac. Vitae ultricies leo integer malesuada nunc. Turpis egestas integer eget aliquet. Feugiat nisl pretium fusce id. Elementum eu facilisis sed odio morbi quis commodo odio. Fames ac turpis egestas integer eget aliquet nibh. Cras tincidunt lobortis feugiat vivamus at augue eget arcu. Sed euismod nisi porta lorem mollis aliquam. Aliquet porttitor lacus luctus accumsan tortor posuere ac. Elementum curabitur vitae nunc sed. Vel quam elementum pulvinar etiam non quam lacus suspendisse faucibus. Venenatis lectus magna fringilla urna porttitor rhoncus dolor purus non. Id aliquet risus feugiat in. Tellus cras adipiscing enim eu turpis egestas. Ut enim blandit volutpat maecenas volutpat.";

            readingTextDao.insert(new ReadingText("en", "BBC", ReadingText.DOCUMENT_TYPE_PLAIN, news));
            readingTextDao.insert(new ReadingText("en", "Guardian", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
            readingTextDao.insert(new ReadingText("en", "New York Times", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
            readingTextDao.insert(new ReadingText("en", "Readaing Text", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
            readingTextDao.insert(new ReadingText("en", "Another Reading Text", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
            readingTextDao.insert(new ReadingText("en", "Dünya Halleri", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
            readingTextDao.insert(new ReadingText("en", "Hocam abdest bozulur mu", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));

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
