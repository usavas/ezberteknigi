package com.example.savas.ezberteknigi.Data.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.savas.ezberteknigi.Data.Models.Word;

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

    @Query("SELECT * FROM word_table")
    List<Word> getAllWordsAsList();

    @Query("SELECT * FROM word_table WHERE word_id = :wordId")
    Word getWordById (int wordId);

    @Query("SELECT * FROM word_table WHERE word = :word")
    Word getWordByWord (String word);

    @Query("SELECT * FROM word_table WHERE word_state = :wordState AND is_archived = 0")
    LiveData<List<Word>> getWordsByWordState(int wordState);

    @Query("SELECT EXISTS(SELECT 1 FROM word_table WHERE word = :word)")
    boolean wordExists(String word);
}
