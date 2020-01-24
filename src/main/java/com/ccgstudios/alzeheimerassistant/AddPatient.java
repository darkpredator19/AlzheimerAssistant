package com.ccgstudios.alzeheimerassistant;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class AddPatient extends AppCompatActivity {
    private EditText mPatientName;
    private RelativeLayout mRelativeLayout;
    private Button mAddPatient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        getIds();
        setButtons();
    }

    public void getIds(){
        mAddPatient = findViewById(R.id.addPatient);
        mPatientName = findViewById(R.id.patientName);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }

    public void setButtons(){
        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mPatientName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please enter your patient's name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("AssistedNames").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(name).exists()){
                            String newPatient = String.valueOf(dataSnapshot.child(name).getValue());
                            databaseReference.child("Assistant").child(getCurrentUserId()).child("patients").child(name).setValue(newPatient);
                            onBackPressed();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(mRelativeLayout, "Wrong patient name", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
