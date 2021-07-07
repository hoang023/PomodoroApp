package com.example.pomodoro.TasksFunctions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecyclerView_Config {
    private Context mContext;
    private DataAdapter mDataAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Data> data, List<String> keys){
        mContext = context;
        mDataAdapter = new DataAdapter(data,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mDataAdapter);
    }


    class DataAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private final List<Data> todoList;
        private final List<String> keys;
        private final FirebaseDatabase database;
        private final DatabaseReference mData;
        private final FirebaseUser currentU;
        private final String year;
        private final String month;
        private final String UId;

        public DataAdapter (List<Data> todoList, List<String> keys){
            this.todoList = todoList;
            this.keys = keys;

            currentU = FirebaseAuth.getInstance().getCurrentUser();
            UId = currentU.getUid();
            year = new SimpleDateFormat("yyyy",Locale.getDefault()).format(new Date());
            month = new SimpleDateFormat("MMM",Locale.getDefault()).format(new Date());
            database = FirebaseDatabase.getInstance();
            mData = database.getReference("User").child(UId).child(year).child(month).child("Task");
        }

        // Set layout cho từng listitem
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(parent);
        }

        public Context getContext(){return mContext;}

        // Update cho checkbox và lấy tên task
        @Override
        public void onBindViewHolder(@NonNull RecyclerView_Config.MyViewHolder holder, int position) {

            holder.bind(todoList.get(position), keys.get(position));

            holder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.updateData();
                    return true;
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        mData.child(keys.get(position)).child("Status").setValue(1);
                    } else {
                        holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        mData.child(keys.get(position)).child("Status").setValue(0);
                    }
                }
            });
        }
        // Hàm đếm item
        @Override
        public int getItemCount() {
            return todoList.size();
        }
    }

    // Hàm chuyển sang kiểu số cho status: 0 là CHT & 1 là HT
    private boolean toBoolean(int status){
        return status != 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private String key;

        public MyViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.item_tasks, parent, false));

            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(Data data, String key) {
            checkBox.setText(data.getContent());
            checkBox.setChecked(toBoolean(data.getStatus()));
            this.key = key;
        }

        public void updateData() {
            AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.detail_tasks, null);
            myDialog.setView(view);

            AlertDialog dialog = myDialog.create();

            EditText updateEdt = view.findViewById(R.id.updateEdt);

            updateEdt.setText(checkBox.getText().toString());
            updateEdt.setSelection(checkBox.getText().toString().length());

            Button deleteBtn = view.findViewById(R.id.deleteBtn);
            Button saveuptBtn = view.findViewById(R.id.saveuptBtn);

            saveuptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String task = updateEdt.getText().toString();

                    new FirebaseDatabaseHelper().updateData(key, task, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Data> mList, List<String> keys) {

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
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FirebaseDatabaseHelper().deleteData(key, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Data> mList, List<String> keys) {

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
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}