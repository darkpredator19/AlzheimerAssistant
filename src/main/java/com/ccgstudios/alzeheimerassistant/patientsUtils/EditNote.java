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

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class EditNote extends AppCompatActivity {
    private EditText mQuestion, mContent;
    private Button mAddNote;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String question = extras.getString("question");
            String answer = extras.getString("answer");
            String userUid = extras.getString("userUid");
            String type = extras.getString("type");
            getIds();

            if(type.equals("notes") && question != null){
                    mAddNote.setText("Update Note");
            } else if(type.equals("medication") && question != null){
                    mAddNote.setText("Update Medication");
                    mQuestion.setHint("Time");
                    mContent.setHint("Medication Name");
            } else if(type.equals("medication")){
                    mAddNote.setText("Add Medication");
                    mQuestion.setHint("Time");
                    mContent.setHint("Medication Name");
            }



            mQuestion.setText(question);
            mContent.setText(answer);

            setButtons(userUid, type);

        }


    }

    public void getIds(){
        mQuestion = findViewById(R.id.questionText);
        mContent = findViewById(R.id.noteText);
        mAddNote = findViewById(R.id.addNote);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }

    public void setButtons(final String userUid, final String type){
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = mQuestion.getText().toString().trim();
                String content = mContent.getText().toString().trim();

                if (TextUtils.isEmpty(question)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please enter question", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(content)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please enter content", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Assisted").child(userUid).child(type).child(question).setValue(content);

                onBackPressed();

            }
        });
    }

}
