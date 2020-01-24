package com.ccgstudios.alzeheimerassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.ccgstudios.alzeheimerassistant.Util.savePushToken;


public class RegisterActivity extends AppCompatActivity {
    private EditText mEmail, mPassword, mConfirmPassword;
    private Button mSignUp;
    private RelativeLayout mRelativeLayout;
    private FirebaseAuth auth;
    private String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getIds();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String type = extras.getString("type");
            registration(type);


        }


    }

    private void registration(final String type) {

        auth = FirebaseAuth.getInstance();
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Enter email address", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }


                if (TextUtils.isEmpty(password)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Enter password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Re-enter password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }


                if (password.length() < 8) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Password too short, enter minimum 8 characters", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Snackbar snackbar = Snackbar
                                            .make(mRelativeLayout, "REGISTRATION FAILED", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }
                                else{

                                    String uid = auth.getCurrentUser().getUid();

                                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                    savePushToken(refreshedToken, uid);


                                    Intent intent = new Intent(RegisterActivity.this, Assisted.class);
                                    intent.putExtra("type", type);
                                    startActivity(intent);

                                }


                            }
                        });




            }
        });
    }


    private void getIds(){
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        mSignUp = findViewById(R.id.sign_up);
        mRelativeLayout = findViewById(R.id.relativeLayout);
    }
}
