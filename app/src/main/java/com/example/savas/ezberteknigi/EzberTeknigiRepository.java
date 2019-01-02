package com.example.savas.ezberteknigi;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.RequiresPermission;

import java.util.List;

public class EzberTeknigiRepository {
    private ReadingTextDao readingTextDao;
    private LiveData<List<ReadingText>> allReadingTexts;

    public EzberTeknigiRepository(Application application) {
        EzberTeknigiDatabase database = EzberTeknigiDatabase.getInstance(application);
        readingTextDao = database.readingTextDao();
        allReadingTexts = readingTextDao.getAllReadingTexts();
    }

    public void insert(ReadingText readingText) {
        new InsertNoteAsyncTask(readingTextDao).execute(readingText);
    }

//    public void update(Note note) {
//        new UpdateNoteAsyncTask(noteDao).execute(note);
//    }
//
//    public void delete(Note note) {
//        new DeleteNoteAsyncTask(noteDao).execute(note);
//    }
//
//    public void deleteAllNotes() {
//        new DeleteAllNotesAsyncTask(noteDao).execute();
//    }
//
//    public LiveData<List<Note>> getAllNotes() {
//        return allNotes;
//    }

    private static class InsertNoteAsyncTask extends AsyncTask<ReadingText, Void, Void> {
        private ReadingTextDao readingTextDao;

        private InsertNoteAsyncTask(ReadingTextDao readingTextDao) {
            this.readingTextDao = readingTextDao;
        }

        @Override
        protected Void doInBackground(ReadingText... readingTexts) {
            readingTextDao.insert(readingTexts[0]);
            return null;
        }
    }

//    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
//        private NoteDao noteDao;
//
//        private UpdateNoteAsyncTask(NoteDao noteDao) {
//            this.noteDao = noteDao;
//        }
//
//        @Override
//        protected Void doInBackground(Note... notes) {
//            noteDao.update(notes[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
//        private NoteDao noteDao;
//
//        private DeleteNoteAsyncTask(NoteDao noteDao) {
//            this.noteDao = noteDao;
//        }
//
//        @Override
//        protected Void doInBackground(Note... notes) {
//            noteDao.delete(notes[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
//        private NoteDao noteDao;
//
//        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
//            this.noteDao = noteDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            noteDao.deleteAllNotes();
//            return null;
//        }
//    }
}
