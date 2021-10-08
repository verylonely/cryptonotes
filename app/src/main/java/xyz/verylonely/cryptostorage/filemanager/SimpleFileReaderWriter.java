package xyz.verylonely.cryptostorage.filemanager;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import xyz.verylonely.cryptostorage.activities.MainActivity;

public abstract class SimpleFileReaderWriter {

    private static final String FILE_MANAGER_FOLDER = "user_folder";

    public static void writeFile(Context context, String filename, byte[] data) {
        File dir = new File(context.getFilesDir(), FILE_MANAGER_FOLDER);
        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dir, filename);

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data);

            Toast.makeText(MainActivity.getInstance(), "file saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readFile(Context context, String filename)
    {
        FileInputStream fis = null;
        byte[] data = null;

        try {
            fis = context.openFileInput(filename);

            InputStreamReader isr = new InputStreamReader(fis);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
