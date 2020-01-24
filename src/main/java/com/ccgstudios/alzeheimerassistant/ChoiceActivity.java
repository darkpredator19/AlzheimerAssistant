package com.ccgstudios.alzeheimerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
    Button mPatient, mAssistant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        getIds();
        setButtons();
    }


    public void getIds(){
        mPatient = findViewById(R.id.patient);
        mAssistant = findViewById(R.id.assistant);
    }

    public void setButtons(){
        mPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, RegisterActivity.class);
                intent.putExtra("type", "Assisted");
                startActivity(intent);
            }
        });

        mAssistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceActivity.this, RegisterActivity.class);
                intent.putExtra("type", "Assistant");
                startActivity(intent);
            }
        });
    }
}
