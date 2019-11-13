package com.dodemy.android.roomdb.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.dodemy.android.roomdb.db.NoteDatabase;
import com.dodemy.android.roomdb.model.Note;
import com.dodemy.android.roomdb.util.AppUtils;

import java.util.List;

public class NoteRepository {

    private String DB_NAME = "db_task";

    private static NoteDatabase noteDatabase;
    public NoteRepository(Context context) {
        noteDatabase = Room.databaseBuilder(context, NoteDatabase.class, DB_NAME).build();
    }

    public void insertTask(String title, String description) {
        insertTask(title, description, false, null);
    }

    public void insertTask(String title, String description, boolean encrypt, String password) {
        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setCreatedAt(AppUtils.getCurrentDateTime());
        note.setModifiedAt(AppUtils.getCurrentDateTime());
        note.setEncrypt(encrypt);


        if(encrypt) {
            note.setPassword(AppUtils.generateHash(password));
        } else note.setPassword(null);

        insertTask(note);
    }

    public  static void insertTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().insertTask(note);
                return null;
            }
        }.execute();
    }

    public static void updateTask(final Note note) {
        note.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().updateTask(note);
                return null;
            }
        }.execute();
    }

    public static void deleteTask(final int id) {
        final LiveData<Note> task = getTask(id);
        if(task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    noteDatabase.daoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    //@SuppressLint("StaticFieldLeak")
    public static void deleteTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                noteDatabase.daoAccess().deleteTask(note);
                return null;
            }
        }.execute();
    }

    public static LiveData<Note> getTask(int id) {
        return noteDatabase.daoAccess().getTask(id);
    }

    public LiveData<List<Note>> getTasks() {
        return noteDatabase.daoAccess().fetchAllTasks();
    }
}
