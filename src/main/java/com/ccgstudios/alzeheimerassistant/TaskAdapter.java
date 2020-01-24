package com.ccgstudios.alzeheimerassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.ccgstudios.alzeheimerassistant.objects.Task;
import com.ccgstudios.alzeheimerassistant.patientsUtils.EditNodesAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.Handler;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class TaskAdapter extends RecyclerView.Adapter {
    private List<Task> mItems;
    private Context context;


    public TaskAdapter(List<Task> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task, viewGroup, false);
        return new TaskAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        ((TaskAdapter.ListViewHolder) viewHolder).bindView(i);
        final Task task = mItems.get(i);
        String text = task.getText();
        final String key = task.getKey();

        ((ListViewHolder) viewHolder).mTask.setText(text);
        ((ListViewHolder) viewHolder).mTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Assisted").child(getCurrentUserId()).child("tasks").child(key).setValue(null);
                        mItems.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, mItems.size());




            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTask;
        private RelativeLayout mTick;
        private ProgressBar mProgressBar;

        private ListViewHolder(View itemView) {
            super(itemView);

            mTask = itemView.findViewById(R.id.task);
            mTick = itemView.findViewById(R.id.tick);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(this);
        }

        private void bindView(int position){


        }


        @Override
        public void onClick(View v) {

        }
    }
}
