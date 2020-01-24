package com.ccgstudios.alzeheimerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class Recap extends AppCompatActivity {
    private List<Note> mNotes = new ArrayList<>();
    private NotesAdapter mNotesAdapter;
    private RecyclerView mNotesRecyclerView;
    private TextToSpeech tts;
    private Button mAddNote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        getIds();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        mNotesAdapter = new NotesAdapter( mNotes, this, tts);
        mNotesRecyclerView.setAdapter(mNotesAdapter);
        RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
        mNotesRecyclerView.setLayoutManager(notesLayout);

        getNotes();


    }

    public void getIds(){
        mNotesRecyclerView = findViewById(R.id.notesList);
        mAddNote = findViewById(R.id.newNote);
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recap.this, AddNote.class);
                startActivity(intent);
            }
        });
    }

    public void getNotes(){
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(getCurrentUserId()).child("notes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String question = dataSnapshot.getKey();
                String content = String.valueOf(dataSnapshot.getValue());


                Note note = new Note();
                note.setQuestion(question);
                note.setContent(content);

                if(!mNotes.contains(note)){
                    mNotes.add(note);
                    mNotesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
