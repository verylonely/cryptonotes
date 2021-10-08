package xyz.verylonely.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import xyz.verylonely.CryptoSDK.EncryptDecrypt;
import xyz.verylonely.cryptostorage.activities.MainActivity;

@Entity
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "completed")
    private boolean completed;



    public Note()
    {

    }

    protected Note(Parcel in) {
        uid = in.readInt();
        title = in.readString();
        text = in.readString();
        timestamp = in.readLong();
        completed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (completed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return uid == note.uid && timestamp == note.timestamp && completed == note.completed && Objects.equals(title, note.title) && Objects.equals(text, note.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, title, text, timestamp, completed);
    }

    public String getText() {
        return text;
    }

    public int getUid() {
        return uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
