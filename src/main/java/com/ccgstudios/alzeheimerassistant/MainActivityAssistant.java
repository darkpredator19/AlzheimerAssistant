package com.ccgstudios.alzeheimerassistant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.ccgstudios.alzeheimerassistant.objects.Patient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class MainActivityAssistant extends AppCompatActivity {
    private Button mAddPatient;
    private List<Patient> mPatients = new ArrayList<>();
    private PatientsAdapter mPatientsAdapter;
    private RecyclerView mPatientRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_assistant);
        getIds();

        mPatientsAdapter = new PatientsAdapter(mPatients, this);
        mPatientRecyclerView.setAdapter(mPatientsAdapter);
        RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
        mPatientRecyclerView.setLayoutManager(notesLayout);


        setButtons();
        getData();


    }

    public void getIds(){
        mAddPatient = findViewById(R.id.newPatient);
        mPatientRecyclerView = findViewById(R.id.patientsList);
    }

    public void setButtons(){
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAssistant.this, AddPatient.class);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assistant").child(getCurrentUserId()).child("patients").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userUid = String.valueOf(dataSnapshot.getValue());
                String name = dataSnapshot.getKey();
                final Patient patient = new Patient();
                patient.setName(name);
                patient.setUserUid(userUid);


                DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
                dr.child("Assisted").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int age = Integer.parseInt(String.valueOf(dataSnapshot.child("age").getValue()));
                        patient.setAge(age);

                        if(dataSnapshot.child("danger").exists()){
                            if(Boolean.parseBoolean(String.valueOf(dataSnapshot.child("danger").getValue()))){
                                patient.setDanger(true);

                            }

                            if(!mPatients.contains(patient)){
                                mPatients.add(patient);
                                mPatientsAdapter.notifyDataSetChanged();
                            }
                        }

                        if(!mPatients.contains(patient)){
                            mPatients.add(patient);
                            mPatientsAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
