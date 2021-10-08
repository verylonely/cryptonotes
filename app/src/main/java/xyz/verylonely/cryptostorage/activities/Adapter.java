package xyz.verylonely.cryptostorage.activities;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import xyz.verylonely.CryptoSDK.EncryptDecrypt;
import xyz.verylonely.Model.Note;
import xyz.verylonely.cryptostorage.App;
import xyz.verylonely.cryptostorage.R;

public class Adapter extends RecyclerView.Adapter<Adapter.NoteViewHolder> {


    private SortedList<Note> sortedList;

    public Adapter()
    {
        sortedList = new SortedList<>(Note.class, new SortedList.Callback<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if(!o2.isCompleted() && o1.isCompleted())
                    return 1;
                if(o2.isCompleted() && !o1.isCompleted())
                    return -1;
                return (int) (o2.getTimestamp() - o1.getTimestamp());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Note oldItem, Note newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Note item1, Note item2) {
                return item1.getUid() == item2.getUid();
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view_activity, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        try {
            holder.bind(sortedList.get(position));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void setItems(List<Note> notes)
    {
        sortedList.replaceAll(notes);
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder
    {

        TextView noteTitle;
        CheckBox completed;
        View delete;

        Note note;

        boolean silentUpdate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title_view);
            //noteText = itemView.findViewById(R.id.note_text_view);
            completed = itemView.findViewById(R.id.note_completed_checkbox);
            delete = itemView.findViewById(R.id.deleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoteDetailsActivity.start((Activity) itemView.getContext(), note, OpenTaskType.OPEN);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.getInstance().getNoteDao().delete(note);
                }
            });

            completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if(!silentUpdate) {
                        note.setCompleted(checked);
                        App.getInstance().getNoteDao().update(note);
                    }
                    updateStrokeOut();
                }
            });
        }

        public void bind(Note note) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
            this.note = note;

            //noteText.setText(EncryptDecrypt.DecryptString(note.getText(), MainActivity.getInstance().getKey()));
            noteTitle.setText(EncryptDecrypt.DecryptString(note.getTitle(), MainActivity.getInstance().getKey()));
            updateStrokeOut();

            silentUpdate = true;
            completed.setChecked(note.isCompleted());
            silentUpdate = false;
        }

        private void updateStrokeOut()
        {
            if(note.isCompleted())
            {
                noteTitle.setPaintFlags(noteTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                noteTitle.setPaintFlags(noteTitle.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

}
