package xyz.verylonely.cryptostorage;

import android.app.Application;

import androidx.room.Room;

import xyz.verylonely.Data.Database;
import xyz.verylonely.Data.NoteDao;

public class App extends Application {

    private Database database;
    private NoteDao noteDao;

    private static App instance;

    public static App getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        database = Room.databaseBuilder(getApplicationContext(),
                Database.class, "notes-database")
                .allowMainThreadQueries().build();

        noteDao = database.noteDao();
    }



    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
