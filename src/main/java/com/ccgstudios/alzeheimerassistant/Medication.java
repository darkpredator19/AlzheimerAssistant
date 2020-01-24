package com.ccgstudios.alzeheimerassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class Medication extends AppCompatActivity {
    private List<Note> mMedics = new ArrayList<>();
    private MedicationAdapter mMedicationAdapter;
    private RecyclerView mMedicsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        getIds();

        mMedicationAdapter = new MedicationAdapter(mMedics, this);
        mMedicsRecyclerView.setAdapter(mMedicationAdapter);
        RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
        mMedicsRecyclerView.setLayoutManager(notesLayout);

        getData();


    }

    public void getIds(){
        mMedicsRecyclerView = findViewById(R.id.medication);
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(getCurrentUserId()).child("medication").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String time = dataSnapshot.getKey();
                String content = String.valueOf(dataSnapshot.getValue());


                Note note = new Note();

                note.setQuestion(time);
                note.setContent(content);

                if(!mMedics.contains(note)){
                    mMedics.add(note);
                    mMedicationAdapter.notifyDataSetChanged();
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
