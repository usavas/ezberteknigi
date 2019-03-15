package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "word_table"
//        ,foreignKeys = @ForeignKey(
//                entity = ReadingText.class,
//                parentColumns = "wordId",
//                childColumns = "reading_text_id",
//                onUpdate = CASCADE)
)
public class Word {
    public final static int WORD_LEARNING = 0;
    public final static int WORD_MASTERED = 1;
    public final static int WORD_REVISION = 2;
    public final static int WORD_ALL = 3;

    public final static String EXAMPLE_SENTENCE_DELIMITER_REGEX = "##";
    public final static int READING_TEXT_ID_DEFAULT = 0;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "word_id")
    private int wordId;
    private String word;
    private String translation;
    @ColumnInfo(name = "reading_text_id")
    private int readingTextId;

    //TODO: either make this another class or add logic to store multiple sentences
    @ColumnInfo(name = "example_sentence")
    private String exampleSentence;

    @ColumnInfo(name = "date_saved")
    @TypeConverters({TimeStampConverter.class})
    private Date dateSaved;

    @ColumnInfo(name = "revision_date")
    @TypeConverters(TimeStampConverter.class)
    private Date dateLastRevision;

    @ColumnInfo(name = "word_state")
    private int wordState;
    @ColumnInfo(name = "revision_period_count")
    private int revisionPeriodCount;
    @ColumnInfo(name = "error_count")
    private int errorCount;
    @ColumnInfo(name = "correct_count")
    private int correctCount;
    @ColumnInfo(name = "screening_count")
    private int screeningCount;
    @ColumnInfo(name = "details_seen_count")
    private int detailsSeenCount;

    @Ignore
    public Word() {
        this.dateSaved = new Date();
    }

    @Ignore
    public Word(String word,
                String translation,
                int readingTextId,
                String exampleSentence) {
        this.word = word;
        this.translation = translation;
        this.readingTextId = readingTextId;
        this.exampleSentence = exampleSentence;

        this.dateSaved = new Date();
        this.dateLastRevision = new Date();
        this.revisionPeriodCount = 0;

        this.wordState = 0;
        this.errorCount = 0;
        this.correctCount = 0;
        this.screeningCount = 0;
        this.detailsSeenCount = 0;
    }

    public Word(String word,
                String translation,
                int readingTextId,
                String exampleSentence,
                int wordState,
                int revisionPeriodCount,
                int errorCount,
                int correctCount,
                int screeningCount,
                int detailsSeenCount) {
        this.word = word;
        this.translation = translation;
        this.readingTextId = readingTextId;
        this.exampleSentence = exampleSentence;
        this.wordState = wordState;
        this.revisionPeriodCount = revisionPeriodCount;
        this.errorCount = errorCount;
        this.correctCount = correctCount;
        this.screeningCount = screeningCount;
        this.detailsSeenCount = detailsSeenCount;

        this.dateSaved = new Date();
        this.dateLastRevision = new Date();
    }

    @Ignore
    public long getTimeElapsedInSeconds() {
        return getDateDiff(TimeUnit.SECONDS);
    }

    @Ignore
    public long getTimeElapsedInMinutes() {
        return getDateDiff(TimeUnit.MINUTES);
    }

    @Ignore
    public long getTimeElapsedInHours() {
        return getDateDiff(TimeUnit.HOURS);
    }

    @Ignore
    public long getTimeElapsedInDays() {
        return getDateDiff(TimeUnit.DAYS);
    }

    @Ignore
    private long getDateDiff(TimeUnit timeUnit) {
        Date currDate = new Date();
        long currTime = currDate.getTime();
        long updateTime = dateLastRevision.getTime();

        long diffInMilliSeconds = currTime - updateTime;
        return timeUnit.convert(diffInMilliSeconds, TimeUnit.MILLISECONDS);
    }

    public int getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public int getReadingTextId() {
        return readingTextId;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public Date getDateSaved() {
        return dateSaved;
    }

    public Date getDateLastRevision() {
        return dateLastRevision;
    }

    public int getWordState() {
        return wordState;
    }

    public int getRevisionPeriodCount() {
        return revisionPeriodCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getScreeningCount() {
        return screeningCount;
    }

    public int getDetailsSeenCount() {
        return detailsSeenCount;
    }


    public void setWordId(int id) {
        this.wordId = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setReadingTextId(int readingTextId) {
        this.readingTextId = readingTextId;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }

    public void setDateLastRevision(Date dateLastRevision) {
        this.dateLastRevision = dateLastRevision;
    }

    public void setWordState(int wordState) {
        this.wordState = wordState;
    }

    public void setRevisionPeriodCount(int revisionPeriodCount) {
        this.revisionPeriodCount = revisionPeriodCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public void setScreeningCount(int screeningCount) {
        this.screeningCount = screeningCount;
    }

    public void setDetailsSeenCount(int detailsSeenCount) {
        this.detailsSeenCount = detailsSeenCount;
    }

    @Ignore
    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void revisionCompleted(){
        this.revisionPeriodCount += 1;
        this.setDateLastRevision(new Date());
    }

    public static List<Word> getWordsToRevise(List<Word> _words){
        List<Word> resultWords = new ArrayList<>();
//        resultWords.addAll(getWordRevisionList(_words, 0, TimeType.MINUTE, REV_1_MIN));
//        resultWords.addAll(getWordRevisionList(_words, 1, TimeType.HOUR, REV_2_HOUR));
//        resultWords.addAll(getWordRevisionList(_words, 2, TimeType.HOUR, REV_3_HOUR));
//        resultWords.addAll(getWordRevisionList(_words, 3, TimeType.HOUR, REV_4_HOUR));
//        resultWords.addAll(getWordRevisionList(_words, 4, TimeType.HOUR, REV_5_HOUR));
//        resultWords.addAll(getWordRevisionList(_words, 5, TimeType.HOUR, REV_6_HOUR));
//        resultWords.addAll(getWordRevisionList(_words, 6, TimeType.DAY, REV_7_DAY));

        resultWords.addAll(getWordRevisionList(_words, 0, TimeType.MINUTE, REV_1_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 1, TimeType.MINUTE, REV_2_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 2, TimeType.MINUTE, REV_3_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 3, TimeType.MINUTE, REV_4_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 4, TimeType.MINUTE, REV_5_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 5, TimeType.MINUTE, REV_6_MIN_MOCK));
        resultWords.addAll(getWordRevisionList(_words, 6, TimeType.MINUTE, REV_7_MIN_MOCK));
        return resultWords;
    }

    private static List<Word> getWordRevisionList(List<Word> _words, int _periodCount, TimeType timeType, int _timeAmount){
        List<Word> listToRevise = new ArrayList<>();
        long elapsedTime = 0;

        for (Word word: _words) {
            switch (timeType){
                case SECOND:
                    elapsedTime = word.getTimeElapsedInSeconds();
                    break;
                case MINUTE:
                    elapsedTime = word.getTimeElapsedInMinutes();
                    break;
                case HOUR:
                    elapsedTime = word.getTimeElapsedInHours();
                    break;
                case DAY:
                    elapsedTime = word.getTimeElapsedInDays();
                    break;
            }

            if (word.getRevisionPeriodCount() == _periodCount && elapsedTime >= _timeAmount){
                listToRevise.add(word);
            }
        }
        return listToRevise;
    }

//    private static int REV_1_MIN = 30;
//    private static int REV_2_HOUR = 1;
//    private static int REV_3_HOUR = 2;
//    private static int REV_4_HOUR = 6;
//    private static int REV_5_HOUR = 12;
//    private static int REV_6_HOUR = 24;
//    private static int REV_7_DAY = 5;

    private static int REV_1_MIN_MOCK = 1;
    private static int REV_2_MIN_MOCK = 1;
    private static int REV_3_MIN_MOCK = 1;
    private static int REV_4_MIN_MOCK = 1;
    private static int REV_5_MIN_MOCK = 1;
    private static int REV_6_MIN_MOCK = 1;
    private static int REV_7_MIN_MOCK = 3;

    private enum TimeType{
        MINUTE, HOUR, DAY, SECOND
    }

    @Override
    public String toString(){
        return String.format("wordId: %d, word: %s, translation: %s", this.wordId, this.word, this.translation);
    }
}
