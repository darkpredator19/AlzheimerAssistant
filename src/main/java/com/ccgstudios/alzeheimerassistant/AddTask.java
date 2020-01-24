package com.ccgstudios.alzeheimerassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class AddTask extends AppCompatActivity {
    private EditText mTask;
    private Button mAddTask;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getIds();
        setButtons();
    }

    public void getIds(){
        mTask = findViewById(R.id.task);
        mAddTask = findViewById(R.id.addTask);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }

    public void setButtons(){
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = mTask.getText().toString().trim();

                if (TextUtils.isEmpty(task)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please enter task", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Assisted").child(getCurrentUserId()).child("tasks").push();
                databaseReference.setValue(task);
                onBackPressed();


            }
        });
    }
}
