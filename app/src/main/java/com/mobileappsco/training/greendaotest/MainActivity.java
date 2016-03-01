package com.mobileappsco.training.greendaotest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileappsco.training.greendaotest.db.DaoMaster;
import com.mobileappsco.training.greendaotest.db.DaoSession;
import com.mobileappsco.training.greendaotest.db.Note;
import com.mobileappsco.training.greendaotest.db.NoteDao;

import java.util.Date;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    DaoMaster.DevOpenHelper devOpenHelper;
    SQLiteDatabase sqldb;
    DaoMaster daoMaster;
    DaoSession daoSession;
    NoteDao noteDao;
    TextView tvNotes;
    EditText etNoteText;
    EditText etNoteComment;
    EditText etNoteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GreenDao
        devOpenHelper = new DaoMaster.DevOpenHelper(this, "com.mobileappsco.training.greendaotest.db", null);
        sqldb = devOpenHelper.getWritableDatabase();
        daoMaster = new DaoMaster(sqldb);
        daoSession = daoMaster.newSession();
        noteDao = daoSession.getNoteDao();

        // Interface
        etNoteText = (EditText) findViewById(R.id.etNoteText);
        etNoteComment = (EditText) findViewById(R.id.etNoteComment);
        etNoteDate = (EditText) findViewById(R.id.etNoteDate);
        tvNotes = (TextView) findViewById(R.id.tvNotes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayNotes();
    }

    public void saveNote(View view) {
        Note note = new Note();
        note.setComment(etNoteComment.getText().toString());
        note.setDate(new Date(etNoteDate.getText().toString()));
        note.setText(etNoteText.getText().toString());
        noteDao.insert(note);
        Toast.makeText(this, "Inserting new note", Toast.LENGTH_SHORT).show();
        displayNotes();
    }

    public void displayNotes() {
        List<Note> notes = noteDao.queryBuilder().orderDesc().list();
        tvNotes.setText("");
        for (Note nt : notes) {
            tvNotes.setText(tvNotes.getText().toString()+
                            nt.getId()+" >> "+
                            nt.getText()+"\n"+
                            nt.getComment()+"\n"+
                            nt.getDate()+"\n"
            );
        }
    }
}
