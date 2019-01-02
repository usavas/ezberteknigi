package com.example.savas.ezberteknigi;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ReadingTextDao extends BaseDao {

    @Insert
    void insert(ReadingText readingText);

    @Update
    void update(ReadingText readingText);

    @Delete
    void delete(ReadingText readingText);

    @Query("SELECT * FROM reading_text_table ORDER BY difficulty_rate DESC")
    LiveData<List<ReadingText>> getAllReadingTexts();




}
