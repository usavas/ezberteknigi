package com.example.savas.ezberteknigi.Data.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.savas.ezberteknigi.Data.Models.Word;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface WordDao extends BaseDao {

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);

    @Query("DELETE FROM word_table")
    void deleteAllWords();

    @Query("SELECT * FROM word_table WHERE is_archived = 0")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table WHERE is_archived = 0")
    List<Word> getAllWordsAsList();

    @Query("SELECT * FROM word_table WHERE word_id = :wordId AND is_archived = 0")
    Word getWordById(int wordId);

    @Query("SELECT * FROM word_table WHERE word = :word AND is_archived = 0")
    Word getWordByWord(String word);

    @Query("SELECT * FROM word_table " +
            "WHERE word_state = :wordState " +
            "AND is_archived = 0")
    LiveData<List<Word>> getWordsByWordState(int wordState);

    @Query("SELECT * FROM word_table " +
            "WHERE word_state = :wordState " +
            "AND reading_text_id = :readingId " +
            "AND is_archived = 0")
    LiveData<List<Word>> getWordsByWordStateAndReading(int wordState, int readingId);

    /**
     * revision_period_count: how many times this word will be called for revision
     * in each iteration how much time should pass in milliseconds
     * @return
     */
    @Query("SELECT * FROM word_table " +
            "WHERE is_archived = 0 AND (" +
            "(revision_period_count = 0 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 1 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 2 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 3 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 4 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 5 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 6 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) OR " +
            "(revision_period_count = 7 AND (strftime('%s','now', 'localtime') - strftime('%s', revision_date)) / (60) >= 1) " +
            ")"
    )
    LiveData<List<Word>> getWordsToRevise();


    @Query("SELECT EXISTS(SELECT 1 FROM word_table WHERE word = :word)")
    boolean wordExists(String word);
}
