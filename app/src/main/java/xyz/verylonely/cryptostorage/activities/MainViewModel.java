package xyz.verylonely.cryptostorage.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import xyz.verylonely.Model.Note;
import xyz.verylonely.cryptostorage.App;

public class MainViewModel extends ViewModel {

    private LiveData<List<Note>> notesLiveData = App.getInstance().getNoteDao().getAllLiveData();

    public LiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }
}
