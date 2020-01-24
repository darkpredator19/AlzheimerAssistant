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

public class AddNote extends AppCompatActivity {
    private EditText mQuestion, mContent;
    private Button mAddNote;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        getIds();
        setButtons();
    }

    public void getIds(){
        mQuestion = findViewById(R.id.questionText);
        mContent = findViewById(R.id.noteText);
        mAddNote = findViewById(R.id.addNote);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }

    public void setButtons(){
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
                databaseReference.child("Assisted").child(getCurrentUserId()).child("notes").child(question).setValue(content);

                onBackPressed();

            }
        });
    }

}
