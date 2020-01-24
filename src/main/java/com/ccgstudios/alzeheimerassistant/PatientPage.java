package com.ccgstudios.alzeheimerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccgstudios.alzeheimerassistant.objects.Patient;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditDailyTasks;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditHome;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditMedication;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditRecap;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class PatientPage extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private TextView mName, mAge;
    private CircleImageView mProfilePicture;
    LatLng latLng;
    Button mEditRecap, mEditMedication, mEditHome, mEditDailyTasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_page);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String userUid = extras.getString("userUid");
            String name = extras.getString("name");

            int age = extras.getInt("age");
            getIds();
            getData(userUid, name, age);
            setButtons(userUid);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if(latLng != null) {
            map.addMarker(new MarkerOptions().position(latLng).title("Patient"));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void getIds(){
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mProfilePicture = findViewById(R.id.profilePicture);
        mEditRecap = findViewById(R.id.editRecap);
        mEditMedication = findViewById(R.id.editMedication);
        mEditHome = findViewById(R.id.editHomeCoords);
        mEditDailyTasks = findViewById(R.id.editDailyTasks);
    }

    public void setButtons(final String userUid){
        mEditRecap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientPage.this, EditRecap.class);
                intent.putExtra("userUid", userUid);
                startActivity(intent);
            }
        });

        mEditMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientPage.this, EditMedication.class);
                intent.putExtra("userUid", userUid);
                startActivity(intent);
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("home").exists()){

                    mEditHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PatientPage.this, EditHome.class);
                            intent.putExtra("userUid", userUid);
                            intent.putExtra("latitude", String.valueOf(dataSnapshot.child("home").child("latitude").getValue()));
                            intent.putExtra("longitude", String.valueOf(dataSnapshot.child("home").child("longitude").getValue()));
                            startActivity(intent);
                        }
                    });

                } else {

                    mEditHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PatientPage.this, EditHome.class);
                            intent.putExtra("userUid", userUid);
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mEditDailyTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientPage.this, EditDailyTasks.class);
                intent.putExtra("userUid", userUid);
                startActivity(intent);
            }
        });

    }

    public void getData(String userUid, String name, int age){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userUid);
        Glide.with(this).using(new FirebaseImageLoader()).load(storageReference).into(mProfilePicture);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(userUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                if(dataSnapshot.getKey().equals("location")) {
                    double latitude = Double.parseDouble(String.valueOf(dataSnapshot.child("latitude").getValue()));
                    double longitude = Double.parseDouble(String.valueOf(dataSnapshot.child("longitude").getValue()));

                    latLng = new LatLng(latitude, longitude);
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.addMarker(new MarkerOptions().position(latLng).title("Patient"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

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


        mName.setText(name);
        mAge.setText("Age " + age);


    }
}
