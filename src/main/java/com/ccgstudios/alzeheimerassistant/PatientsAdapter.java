package com.ccgstudios.alzeheimerassistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.ccgstudios.alzeheimerassistant.objects.Patient;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientsAdapter extends RecyclerView.Adapter {
    private List<Patient> mItems;
    private Context context;

    public PatientsAdapter(List<Patient> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient, parent, false);
        return new PatientsAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((PatientsAdapter.ListViewHolder) holder).bindView(position);
        final Patient patient = mItems.get(position);
        final String name = patient.getName();
        final int age = patient.getAge();
        final String userUid = patient.getUserUid();
        boolean danger = patient.isDanger();

        if(danger){
            ((ListViewHolder) holder).mCardView.setBackgroundColor(Color.RED);
        }

        ((ListViewHolder) holder).mName.setText(name);
        ((ListViewHolder) holder).mAge.setText("Age " + age);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profilePictures/" + userUid);
        Glide.with(context).using(new FirebaseImageLoader()).load(storageReference).into(((ListViewHolder) holder).mProfilePic);

        ((ListViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PatientPage.class);
                intent.putExtra("userUid", userUid);
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private CircleImageView mProfilePic;
        private TextView mName, mAge;
        private CardView mCardView;

        private ListViewHolder(View itemView) {
            super(itemView);
            mProfilePic = itemView.findViewById(R.id.profilePicture);
            mName = itemView.findViewById(R.id.name);
            mAge = itemView.findViewById(R.id.age);
            mCardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position){


        }


        @Override
        public void onClick(View v) {

        }
    }
}
