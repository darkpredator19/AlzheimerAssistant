package com.ccgstudios.alzeheimerassistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ccgstudios.alzeheimerassistant.objects.Task;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditNodesAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class MainActivityAssisted extends AppCompatActivity implements LocationListener {
    private Button mRecap, mMedication, mAddTask;
    protected LocationManager locationManager;
    private TaskAdapter mTaskAdapter;
    private RecyclerView mRecyclerView;
    protected LocationListener locationListener;
    private List<Task> mItems = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_assisted);

        getIds();
        setButtons();
        getData();

        mTaskAdapter = new TaskAdapter(mItems, this);
        mRecyclerView.setAdapter(mTaskAdapter);
        RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(notesLayout);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    101);

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    public void getIds(){
        mRecap = findViewById(R.id.recap);
        mMedication = findViewById(R.id.medication);
        mAddTask = findViewById(R.id.addTask);
        mRecyclerView = findViewById(R.id.dailyTasks);
    }

    public void setButtons(){

        mRecap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAssisted.this, Recap.class);
                startActivity(intent);
            }
        });

        mMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAssisted.this, Medication.class);
                startActivity(intent);
            }
        });

        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityAssisted.this, AddTask.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(getCurrentUserId()).child("location").child("latitude").setValue(latitude);
        databaseReference.child("Assisted").child(getCurrentUserId()).child("location").child("longitude").setValue(longitude);

        verifyDistance(latitude, longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void verifyDistance(final double latitude, final double longitude){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double lat = Double.parseDouble(String.valueOf(dataSnapshot.child("home").child("latitude").getValue()));
                double lon = Double.parseDouble(String.valueOf(dataSnapshot.child("home").child("longitude").getValue()));

                double dist = Math.sqrt((latitude - lat) * (latitude - lat) + (longitude - lon) * (longitude - lon)) * 6700;
                databaseReference.child("Assisted").child(getCurrentUserId()).child("distance").setValue(dist);

                if(dist > 100){
                    databaseReference.child("Assisted").child(getCurrentUserId()).child("danger").setValue(true);
                } else
                    databaseReference.child("Assisted").child(getCurrentUserId()).child("danger").setValue(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(getCurrentUserId()).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());

                if(!mItems.contains(t)){
                    mItems.add(t);
                    mTaskAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());

                int find = search(mItems, t);
                if(find != -1){
                    mItems.set(find, t);
                    mTaskAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());

                mItems.remove(t);
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public int search(List<Task> list, Task note){

        int i = 0;
        for(Task n : list){

            if(n.getText().equals(note.getText())){
                return i;
            }
            i++;
        }
        return -1;
    }


}
