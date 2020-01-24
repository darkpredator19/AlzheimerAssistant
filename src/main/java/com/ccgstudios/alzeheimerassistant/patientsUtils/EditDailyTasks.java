package com.ccgstudios.alzeheimerassistant.patientsUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccgstudios.alzeheimerassistant.R;
import com.ccgstudios.alzeheimerassistant.objects.Note;
import com.ccgstudios.alzeheimerassistant.objects.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EditDailyTasks extends AppCompatActivity {
    private List<Task> mTasks = new ArrayList<>();
    private EditTasksAdapter mEditTasksAdapter;
    private RecyclerView mRecyclerView;
    private Button mAddTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_daily);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String userUid = extras.getString("userUid");

            getIds();
            mEditTasksAdapter = new EditTasksAdapter(mTasks, this);
            mRecyclerView.setAdapter(mEditTasksAdapter);
            RecyclerView.LayoutManager notesLayout = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(notesLayout);

            setButtons(userUid);
            getData(userUid);
        }
    }

    public void getIds(){
        mRecyclerView = findViewById(R.id.dailyTasks);
        mAddTask = findViewById(R.id.newTask);
        mAddTask.setText("Add Task");
    }

    public void setButtons(final String userUid){
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDailyTasks.this, EditTask.class);
                intent.putExtra("userUid", userUid);
                startActivity(intent);
            }
        });
    }

    public void getData(final String userUid){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Assisted").child(userUid).child("tasks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());
                t.setUserUid(userUid);

                if(!mTasks.contains(t)){
                    mTasks.add(t);
                    mEditTasksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());
                t.setUserUid(userUid);

                int find = search(mTasks, t);
                if(find != -1){
                    mTasks.set(find, t);
                    mEditTasksAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String task = String.valueOf(dataSnapshot.getValue());
                Task t = new Task();
                t.setText(task);
                t.setKey(dataSnapshot.getKey());
                t.setUserUid(userUid);

                delete(t);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public int search(List<Task> list, Task note){

        int i = 0;
        for(Task n : list){

            if(n.getKey().equals(note.getKey())){
                return i;
            }
            i++;
        }
        return -1;
    }

    public void delete(Task t){
        int i = 0;
        for (Task task : mTasks){
            if (task.getKey().equals(t.getKey())){
                mTasks.remove(i);
                mEditTasksAdapter.notifyItemRemoved(i);
                mEditTasksAdapter.notifyItemRangeChanged(i, mTasks.size());
                break;
            }
            i++;
        }
    }

}
