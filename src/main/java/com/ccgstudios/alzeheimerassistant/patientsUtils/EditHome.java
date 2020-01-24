package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ccgstudios.alzeheimerassistant.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditHome extends AppCompatActivity {

    private EditText mLatitude, mLongitude;
    private Button mUpdateHome;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_home);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            final String userUid = extras.getString("userUid");
            final String latitude = extras.getString("latitude");
            String longitude = extras.getString("longitude");

            getIds();

            if(latitude != null && longitude != null){
                mLatitude.setText(latitude);
                mLongitude.setText(longitude);
            }

            mUpdateHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newLatitude = mLatitude.getText().toString().trim();
                    String newLongitude = mLongitude.getText().toString().trim();

                    if (TextUtils.isEmpty(newLatitude)) {
                        Snackbar snackbar = Snackbar
                                .make(mRelativeLayout, "Please enter latitude", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }

                    if (TextUtils.isEmpty(newLongitude)) {
                        Snackbar snackbar = Snackbar
                                .make(mRelativeLayout, "Please enter longitude", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }

                    if(userUid != null) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Assisted").child(userUid).child("home").child("latitude").setValue(newLatitude);
                        databaseReference.child("Assisted").child(userUid).child("home").child("longitude").setValue(newLongitude);

                        onBackPressed();
                    }

                }
            });

        }
    }


    public void getIds(){
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mLatitude = findViewById(R.id.latitude);
        mLongitude = findViewById(R.id.longitude);
        mUpdateHome = findViewById(R.id.updateCoords);
    }
}
