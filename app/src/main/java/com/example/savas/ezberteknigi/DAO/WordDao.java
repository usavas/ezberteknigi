package com.example.savas.ezberteknigi.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.Models.WordMinimal;

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

    @Query("SELECT * FROM word_table")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM word_table WHERE word_id = :wordId")
    Word getWordById (int wordId);

    @Query("SELECT EXISTS(SELECT * FROM word_table WHERE word = :word)")
    boolean existsWord(String word);

    @Query("SELECT * FROM word_table WHERE word_state = :wordState")
    LiveData<List<Word>> getWordsByWordState(int wordState);

    @Query("SELECT word_id, word, translation, reading_text_id, example_sentence, date_saved, word_state FROM word_table")
    List<WordMinimal> getAllWordsMinimal();
}
