package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.NotesAdapter;
import com.ccgstudios.alzeheimerassistant.R;
import com.ccgstudios.alzeheimerassistant.objects.Note;

import java.util.List;

public class EditNodesAdapter extends RecyclerView.Adapter{
    private List<Note> mItems;
    private Context context;
    private String type;


    public EditNodesAdapter(List<Note> mItems, Context context, String type) {
        this.mItems = mItems;
        this.context = context;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_recap, parent, false);
        return new EditNodesAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((EditNodesAdapter.ListViewHolder) holder).bindView(position);
        final Note note = mItems.get(position);
        final String question = note.getQuestion();
        final String content = note.getContent();
        final String userUid = note.getUserUid();

        ((EditNodesAdapter.ListViewHolder) holder).mQuestion.setText(question);
        ((ListViewHolder) holder).mAnswer.setText(content);
        ((ListViewHolder) holder).mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNote.class);
                intent.putExtra("question", question);
                intent.putExtra("answer", content);
                intent.putExtra("userUid", userUid);
                intent.putExtra("type", type);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView mQuestion, mAnswer;
        private RelativeLayout mRelativeLayout;

        private ListViewHolder(View itemView) {
            super(itemView);

            mQuestion = itemView.findViewById(R.id.question);
            mAnswer = itemView.findViewById(R.id.answer);
            mRelativeLayout = itemView.findViewById(R.id.relativeLayout);


            itemView.setOnClickListener(this);
        }

        private void bindView(int position){


        }


        @Override
        public void onClick(View v) {

        }
    }

}
