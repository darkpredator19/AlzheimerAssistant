package com.ccgstudios.alzeheimerassistant.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.R;

public class FirstSplash extends AppCompatActivity {
    TextView mContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_splash);

        mContinue = findViewById(R.id.cont);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstSplash.this, SecondSplash.class);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                startActivity(intent);
            }
        });
    }
}
