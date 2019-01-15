package com.example.savas.ezberteknigi.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
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

    public final static String EXAMPLE_SENTENCE_DELIMITER_REGEX = "##";

    public final static int READING_TEXT_ID_DEFAULT = -1;

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
    public Word(){

    }

    @Ignore
    public Word(String word,
                String translation,
                int readingTextId,
                String exampleSentence){
        this.word = word;
        this.translation = translation;
        this.readingTextId = readingTextId;
        this.exampleSentence = exampleSentence;

        this.dateSaved =  new Date();
        this.wordState = 0;
        this.revisionPeriodCount = 0;
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

        this.dateSaved =  new Date();
    }

    @Ignore
    public long getTimeElapsedInMinutes () {
        return getDateDiff(getDateSaved(), new Date(), TimeUnit.MINUTES);
    }

    @Ignore
    public long getTimeElapsedInHours () {
        return getDateDiff(getDateSaved(), new Date(), TimeUnit.HOURS);
    }

    @Ignore
    public long getTimeElapsedInDays () {
        return getDateDiff(getDateSaved(), new Date(), TimeUnit.DAYS);
    }

    @Ignore
    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        return 35;
//        long diffInMilliSeconds = date2.getTime() - date1.getTime();
//        return timeUnit.convert(diffInMilliSeconds, TimeUnit.MILLISECONDS);
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

    public int getDetailsSeenCount() { return detailsSeenCount;}

    public void setWordId(int id) {
        this.wordId = id;
    }

    public void setWord(String word) {this.word = word;}

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
}
