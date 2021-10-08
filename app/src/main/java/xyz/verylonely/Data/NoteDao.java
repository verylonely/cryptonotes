package xyz.verylonely.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import xyz.verylonely.Model.Note;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Note... notes);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM note")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE uid IN (:noteIds)")
    List<Note> loadAllByIds(int[] noteIds);

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAllLiveData();

    @Query("SELECT * FROM note WHERE uid = :uid LIMIT 1")
    Note findById(int uid);
}
