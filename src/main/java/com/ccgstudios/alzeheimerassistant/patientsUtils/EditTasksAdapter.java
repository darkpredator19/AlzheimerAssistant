package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.R;
import com.ccgstudios.alzeheimerassistant.TaskAdapter;
import com.ccgstudios.alzeheimerassistant.objects.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.ccgstudios.alzeheimerassistant.Util.getCurrentUserId;

public class EditTasksAdapter extends RecyclerView.Adapter {
    private List<Task> mItems;
    private Context context;


    public EditTasksAdapter(List<Task> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_task, viewGroup, false);
        return new EditTasksAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((EditTasksAdapter.ListViewHolder) viewHolder).bindView(i);
        final Task task = mItems.get(i);
        final String text = task.getText();
        final String key = task.getKey();
        final String userUid = task.getUserUid();

        ((EditTasksAdapter.ListViewHolder) viewHolder).mTask.setText(text);
        ((ListViewHolder) viewHolder).mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTask.class);
                intent.putExtra("key", key);
                intent.putExtra("text", text);
                intent.putExtra("userUid", userUid);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTask;
        private RelativeLayout mRelativeLayout;

        private ListViewHolder(View itemView) {
            super(itemView);
            mTask = itemView.findViewById(R.id.task);
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
