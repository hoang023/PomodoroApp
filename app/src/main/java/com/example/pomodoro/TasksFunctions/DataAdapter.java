package com.example.pomodoro.TasksFunctions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<Data> todoList;
    private TasksActivity activity;
    private FirebaseDatabase database;

    public DataAdapter(TasksActivity mainActivity, List<Data> todoList){
        this.todoList = todoList;
        activity = mainActivity;
    }
    // Set layout cho từng listitem
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_tasks, parent, false);
        database = FirebaseDatabase.getInstance();

        return new MyViewHolder(view);
    }
    // Update cho checkbox và lấy tên task
    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyViewHolder holder, int position) {

        Data data = todoList.get(position);
        holder.checkBox.setText(data.getTask());
        holder.checkBox.setChecked(toBoolean(data.getStatus()));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference mData = database.getReference("User");
                FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
                String UId = currentU.getUid();
                DatabaseReference mData1 = mData.child(UId);
                DatabaseReference mData2 = mData1.child(data.getYear());
                DatabaseReference mData3 = mData2.child(data.getMonth());
                DatabaseReference mData4 = mData3.child("Task");
                DatabaseReference mData5 = mData4.child(data.TaskId);
                DatabaseReference mData6 = mData5.child("Status");

                if (isChecked){
                    mData6.setValue(1);

                }else{
                    mData6.setValue(0);
                }
            }
        });
    }
    // Hàm chuyển sang kiểu số cho status: 0 là CHT & 1 là HT
    private boolean toBoolean(int status){
        return status != 0;
    }
    // Hàm đếm item
    @Override
    public int getItemCount() {
        return todoList.size();
    }
    // Ánh xạ checkbox
    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public  MyViewHolder (@NonNull View itemView){
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);

        }
    }
}