package xyz.verylonely.cryptostorage.activities;

import static xyz.verylonely.cryptostorage.activities.MainActivity.FLAG_SECURE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import xyz.verylonely.CryptoSDK.EncryptDecrypt;
import xyz.verylonely.Model.Note;
import xyz.verylonely.cryptostorage.App;
import xyz.verylonely.cryptostorage.R;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "NoteDetailsActivity.EXTRA_NOTE";

    Note note;

    private EditText editText, editTextTitle;

    private TextView timestamp;

    private Button saveButton;

    private static OpenTaskType openTaskType;

    public static void start(Activity caller, Note note, OpenTaskType openType)
    {
        Intent intent = new Intent(caller, NoteDetailsActivity.class);
        if(note != null)
        {
            intent.putExtra(EXTRA_NOTE, note);
        }

        openTaskType = openType;

        caller.startActivity(intent);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notedetails_activity );

        getWindow().setFlags(FLAG_SECURE, FLAG_SECURE);

        setTitle(getString(R.string.note_details_title));

        editText = findViewById(R.id.taskText);

        editTextTitle = findViewById(R.id.taskTitle);

        timestamp = findViewById(R.id.note_timestamp);


        if(getIntent().hasExtra(EXTRA_NOTE))
        {
            note = getIntent().getParcelableExtra(EXTRA_NOTE);
            try {
                editText.setText(EncryptDecrypt.DecryptString(note.getText(), MainActivity.getInstance().getKey()));
                editTextTitle.setText(EncryptDecrypt.DecryptString(note.getTitle(), MainActivity.getInstance().getKey()));
                timestamp.setText(new Date(note.getTimestamp()).toString());
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        }else {
            note = new Note();
        }

        saveButton = findViewById(R.id.saveNoteButton);

        if(openTaskType == OpenTaskType.OPEN)
            saveButton.setText(R.string.update);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    note.setText(EncryptDecrypt.EncryptString(MainActivity.getInstance().getKey(), String.valueOf(editText.getText())));
                    note.setTitle(EncryptDecrypt.EncryptString(MainActivity.getInstance().getKey(), String.valueOf(editTextTitle.getText())));
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

                note.setCompleted(false);
                note.setTimestamp(System.currentTimeMillis());
                if(getIntent().hasExtra(EXTRA_NOTE))
                    App.getInstance().getNoteDao().update(note);
                else
                    App.getInstance().getNoteDao().insert(note);

                finish();
            }
        });
    }





}
