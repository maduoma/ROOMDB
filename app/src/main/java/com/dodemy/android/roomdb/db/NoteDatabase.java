package com.dodemy.android.roomdb.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dodemy.android.roomdb.dao.DaoAccess;
import com.dodemy.android.roomdb.model.Note;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
