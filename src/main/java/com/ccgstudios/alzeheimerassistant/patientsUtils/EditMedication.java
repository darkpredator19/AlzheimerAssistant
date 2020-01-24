package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ccgstudios.alzeheimerassistant.R;
import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditMedication extends AppCompatActivity {
    private List<Note> mNotes = new ArrayList<>();
    private EditNodesAdapter mEditNodesAdapter;
    private RecyclerView mNotesRecyclerView;
    private Button mAddMedication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String userUid = extras.getString("userUid");

            getIds(userUid);
            mEditNodesAdapter = new EditNodesAdapter(mNotes, this, "medication");
            mNotesRecyclerView.setAdapter(mEditNodesAdapter);
            RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
            mNotesRecyclerView.setLayoutManager(notesLayout);

            getMedication(userUid);
        }

    }

    public void getIds(final String userUid){
        mNotesRecyclerView = findViewById(R.id.notesList);
        mAddMedication = findViewById(R.id.newNote);
        mAddMedication.setText("Add medication");
        mAddMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditMedication.this, EditNote.class);
                intent.putExtra("userUid", userUid);
                intent.putExtra("type", "medication");
                startActivity(intent);
            }
        });
    }

    public void getMedication(final String userUid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(userUid).child("medication").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String time = dataSnapshot.getKey();
                String content = String.valueOf(dataSnapshot.getValue());

                Note note = new Note();
                note.setUserUid(userUid);
                note.setQuestion(time);
                note.setContent(content);

                if(!mNotes.contains(note)){
                    mNotes.add(note);
                    mEditNodesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String time = dataSnapshot.getKey();
                String content = String.valueOf(dataSnapshot.getValue());

                Note note = new Note();
                note.setUserUid(userUid);
                note.setQuestion(time);
                note.setContent(content);
                int find = search(mNotes, note);

                if(find != -1){
                    mNotes.set(find, note);
                    mEditNodesAdapter.notifyDataSetChanged();
                }
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

    public int search(List<Note> list, Note note){

        int i = 0;
        for(Note n : list){

            if(n.getQuestion().equals(note.getQuestion())){
                return i;
            }
            i++;
        }
        return -1;
    }

}
