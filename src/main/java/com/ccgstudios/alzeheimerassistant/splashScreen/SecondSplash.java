package com.ccgstudios.alzeheimerassistant.splashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.LoginActivity;
import com.ccgstudios.alzeheimerassistant.R;

public class SecondSplash extends AppCompatActivity {
    TextView mContinue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_splash);

        mContinue = findViewById(R.id.cont);
        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondSplash.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
