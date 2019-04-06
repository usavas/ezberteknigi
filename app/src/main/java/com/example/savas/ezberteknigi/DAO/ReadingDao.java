package com.example.savas.ezberteknigi.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.savas.ezberteknigi.Models.Reading;

import java.util.List;

@Dao
public interface ReadingDao extends BaseDao {

    @Insert
    void insert(Reading reading);

    @Update
    void update(Reading reading);

    @Delete
    void delete(Reading reading);

    @Query("SELECT * FROM reading_table")
    LiveData<List<Reading>> getReadingTexts();

    @Query("SELECT * FROM reading_table WHERE document_type = :docType")
    LiveData<List<Reading>> getReadingTexts(int docType);

    @Query("SELECT * FROM reading_table WHERE document_type = :docType OR document_type = :docType2")
    LiveData<List<Reading>> getReadingTexts(int docType, int docType2);

    @Query("DELETE FROM reading_table")
    void deleteAllReadingTexts();

    @Query("SELECT * FROM reading_table WHERE reading_id = :readingTextId")
    Reading getReadingTextById (int readingTextId);

}
