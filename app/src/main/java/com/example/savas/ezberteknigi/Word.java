package com.example.savas.ezberteknigi;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.RequiresPermission;

import java.time.OffsetDateTime;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static android.arch.persistence.room.ForeignKey.NO_ACTION;

@Entity(tableName = "word_table"
//        ,foreignKeys = @ForeignKey(
//                entity = ReadingText.class,
//                parentColumns = "wordId",
//                childColumns = "reading_text_id",
//                onUpdate = CASCADE)
)
public class Word {

    @PrimaryKey(autoGenerate = true)
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

    public Word(String word, String translation, int readingTextId, String exampleSentence, Date dateSaved, int wordState, int revisionPeriodCount, int errorCount, int correctCount, int screeningCount, int detailsSeenCount) {
        this.word = word;
        this.translation = translation;
        this.readingTextId = readingTextId;
        this.exampleSentence = exampleSentence;
        this.dateSaved =  dateSaved;
        this.wordState = wordState;
        this.revisionPeriodCount = revisionPeriodCount;
        this.errorCount = errorCount;
        this.correctCount = correctCount;
        this.screeningCount = screeningCount;
        this.detailsSeenCount = detailsSeenCount;
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
}
