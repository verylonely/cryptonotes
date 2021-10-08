package xyz.verylonely.cryptostorage.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import xyz.verylonely.CryptoSDK.HashUtil;
import xyz.verylonely.CryptoSDK.KeyGeneratorUtil;
import xyz.verylonely.Model.Note;
import xyz.verylonely.cryptostorage.R;
import xyz.verylonely.cryptostorage.StartType;
import xyz.verylonely.cryptostorage.filemanager.SimpleFileReaderWriter;

public class MainActivity extends AppCompatActivity {

    private static SecretKey key;
    private RecyclerView recyclerView;
    public StartType startType;
    public static MainActivity instance;
    public static final int FLAG_SECURE = WindowManager.LayoutParams.FLAG_SECURE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);

        instance = this;

        SharedPreferences pref = getSharedPreferences("app_keystore", MODE_PRIVATE);
        if(pref.getString("master_key", "").isEmpty())
        {
            startType = StartType.FIRST_START;
        }else{
            startType = StartType.LOAD_KEYS;
        }

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
    }

    public static MainActivity getInstance()
    {
        return instance;
    }

    public SecretKey getKey()
    {
        return key;
    }

    public void setKey(byte[] key)
    {
        this.key = new SecretKeySpec(key, "AES");
    }


    private void FirstRun() throws NoSuchAlgorithmException {
        key = KeyGeneratorUtil.GenerateKey();
        Toast.makeText(this, R.string.key_generated, Toast.LENGTH_SHORT).show();
        SaveKey();
    }

    private void SaveKey()
    {
       SharedPreferences pref = getPreferences(MODE_PRIVATE);
       SharedPreferences.Editor editor = pref.edit();

       editor.putString("secret_key", KeyGeneratorUtil.GetBase64(key));
       editor.commit();
    }

    private void SavePassword()
    {

    }

    private void LoadKey() throws NoSuchAlgorithmException {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String keyBase64 = pref.getString("secret_key", "");


        if(keyBase64.isEmpty())
        {
            FirstRun();
        }

        else
            {
                byte[] key = Base64.getDecoder().decode(keyBase64);
                this.key = new SecretKeySpec(key, 0, key.length, "AES" );
                Toast.makeText(this, R.string.key_loaded, Toast.LENGTH_SHORT).show();
            }

    }

    public void start()
    {
        recyclerView = findViewById(R.id.taskList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        findViewById(R.id.createNewTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDetailsActivity.start(MainActivity.this, null, OpenTaskType.CREATE);
            }
        });

        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.getNotesLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems(notes);
            }
        });


        getWindow().setFlags(FLAG_SECURE, FLAG_SECURE);
    }
}