package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ccgstudios.alzeheimerassistant.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditTask extends AppCompatActivity {
    private EditText mTask;
    private Button mEditTask;
    private Button mDeleteTask;
    private RelativeLayout mRelativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            final String key = extras.getString("key");
            String text = extras.getString("text");
            final String userUid = extras.getString("userUid");
            getIds();

            if(key == null || text == null){
                mDeleteTask.setVisibility(View.INVISIBLE);

                mEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = mTask.getText().toString().trim();

                        if (TextUtils.isEmpty(content)) {
                            Snackbar snackbar = Snackbar
                                    .make(mRelativeLayout, "Please enter task", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            return;
                        }

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Assisted").child(userUid).child("tasks").push();
                        databaseReference.setValue(content);
                        onBackPressed();
                    }
                });
            } else {
                mTask.setText(text);

                setButtons(key, userUid);
            }




        }


    }

    public void getIds(){
        mTask = findViewById(R.id.task);
        mEditTask = findViewById(R.id.addTask);
        mEditTask.setText("Update Task");
        mDeleteTask = findViewById(R.id.deleteTask);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }

    public void setButtons(final String key, final String userUid){
        mEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mTask.getText().toString().trim();

                if (TextUtils.isEmpty(content)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please enter task", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Assisted").child(userUid).child("tasks").child(key).setValue(content);
                onBackPressed();
            }
        });

        mDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Assisted").child(userUid).child("tasks").child(key).setValue(null);

                onBackPressed();
            }
        });
    }

}
