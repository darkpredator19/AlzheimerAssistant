package com.ccgstudios.alzeheimerassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditNodesAdapter;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter {
    private List<Note> mItems;
    private Context context;

    public MedicationAdapter(List<Note> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_recap, viewGroup, false);
        return new MedicationAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((MedicationAdapter.ListViewHolder) viewHolder).bindView(i);
        final Note note = mItems.get(i);
        final String time = note.getQuestion();
        final String content = note.getContent();

        ((ListViewHolder) viewHolder).mTime.setText(time);
        ((ListViewHolder) viewHolder).mContent.setText(content);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTime, mContent;
        private RelativeLayout mRelativeLayout;

        private ListViewHolder(View itemView) {
            super(itemView);

            mTime = itemView.findViewById(R.id.question);
            mContent = itemView.findViewById(R.id.answer);
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
