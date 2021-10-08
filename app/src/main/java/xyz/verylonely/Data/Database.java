package xyz.verylonely.Data;

import androidx.room.RoomDatabase;

import xyz.verylonely.Model.Note;

@androidx.room.Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract NoteDao noteDao();

}
