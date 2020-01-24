package com.ccgstudios.alzeheimerassistant;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccgstudios.alzeheimerassistant.objects.Note;

import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter {
    private List<Note> mItems;
    private Context context;
    TextToSpeech tts;

    public NotesAdapter(List<Note> mItems, Context context, TextToSpeech tts) {
        this.mItems = mItems;
        this.context = context;
        this.tts = tts;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note, parent, false);
        return new NotesAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((NotesAdapter.ListViewHolder) holder).bindView(position);
        final Note note = mItems.get(position);
        final String question = note.getQuestion();
        final String content = note.getContent();

        ((ListViewHolder) holder).mQuestion.setText(question);

        ((ListViewHolder) holder).mNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(content, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout mNote;
        private TextView mQuestion;

        private ListViewHolder(View itemView) {
            super(itemView);
            mNote = itemView.findViewById(R.id.noteRelativeLayout);
            mQuestion = itemView.findViewById(R.id.question);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position){


        }


        @Override
        public void onClick(View v) {

        }
    }

}
