package com.ccgstudios.alzeheimerassistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class Assisted extends AppCompatActivity {
    private EditText mSurname, mName, mAge;
    private ImageView mProfilePicture;
    private Button mNext;
    private RelativeLayout mRelativeLayout;
    private boolean selected = false;
    private String path;
    private Uri selectedImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_avatar);
        getIds();


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String type = extras.getString("type");
            setButtons(type);


        }

    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,100);

    }

    private void getIds(){
        mSurname = findViewById(R.id.surname);
        mName = findViewById(R.id.name);
        mNext = findViewById(R.id.next);
        mProfilePicture = findViewById(R.id.profilePicture);
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mAge = findViewById(R.id.age);
    }

    public void setButtons(final String type){

        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String surname = mSurname.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                final String age = mAge.getText().toString().trim();

                if (TextUtils.isEmpty(surname)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Enter surname", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Enter name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Enter age", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if(!selected){
                    Snackbar snackbar = Snackbar
                            .make(mRelativeLayout, "Please select a profile picture", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                uploadFile();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReference();
                databaseReference.child(type).child(getCurrentUserId()).child("surname").setValue(surname);
                databaseReference.child(type).child(getCurrentUserId()).child("name").setValue(name);
                databaseReference.child(type).child(getCurrentUserId()).child("age").setValue(age);

                if(type.equals("Assisted")){
                    databaseReference.child("AssistedNames").child(surname + " " + name).setValue(getCurrentUserId());
                    Intent intent = new Intent(Assisted.this, MainActivityAssisted.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Assisted.this, MainActivityAssistant.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 100:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();
                    path = selectedImage.getPath();
                    mProfilePicture.setImageURI(selectedImage);
                    selected = true;
                    break;
            }

    }

    private void uploadFile() {
        //if there is a file to upload
        if (path != null && selected) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("profilePictures/" + getCurrentUserId());
            riversRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getApplicationContext(), "No file selected ", Toast.LENGTH_LONG).show();
        }
    }
}
