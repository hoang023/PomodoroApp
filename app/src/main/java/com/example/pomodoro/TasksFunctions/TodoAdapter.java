package com.example.pomodoro.TasksFunctions;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<TodoTASK> tasks=new ArrayList<>();
    private CheckBox checkBox;

    @NonNull
    @NotNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks, parent, false);
        return new TodoViewHolder(view);
    }

    public void setTasks(List<TodoTASK> tasks){
        this.tasks=tasks;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoAdapter.TodoViewHolder holder, int position) {
        TodoTASK currentTask = tasks.get(position);
        checkBox = holder.itemView.findViewById(R.id.checkBox);

        final String id = currentTask.getId();
        final String content = currentTask.getContent();
        holder.id = id;
        holder.content = content;

        checkBox.setText(holder.content);
        checkBox.setChecked(currentTask.getStatus() != 0);

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Task");

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataRef.child(holder.id).child("Status").setValue(isChecked ? 1 : 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("", holder.id);
                    } else {
                        //TODO handling error
                    }
                }
            });
        });
    }

        public void updateData(Context mContext, String taskID, String content){
            AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.detail_tasks, null);
            myDialog.setView(view);

            AlertDialog dialog = myDialog.create();

            EditText updateEdt = view.findViewById(R.id.updateEdt);
            updateEdt.setText(content);
            updateEdt.setSelection(checkBox.getText().length());

            Button saveuptBtn = view.findViewById(R.id.saveuptBtn);
            Button cancelBtn = view.findViewById(R.id.cancelBtn);

            //Sua task
            saveuptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = updateEdt.getText().toString();

                    new FirebaseDatabaseHelper().updateData(taskID, content, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<TodoTASK> mList) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {
                            Toast.makeText(mContext, "Task updated", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                    dialog.dismiss();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            notifyDataSetChanged();
        }
        public void deleteData(Context mContext, String taskID) {
            new FirebaseDatabaseHelper().deleteData(taskID, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<TodoTASK> mList) {

                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {
                    Toast.makeText(mContext, "Task deleted", Toast.LENGTH_SHORT).show();
                }
            });
            notifyDataSetChanged();
        }

    @Override
    public int getItemCount() {
        return tasks==null?0:tasks.size();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder{

        public String id, content;

        public TodoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
