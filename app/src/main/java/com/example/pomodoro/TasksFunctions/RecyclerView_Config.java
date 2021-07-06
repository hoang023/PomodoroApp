package com.example.pomodoro.TasksFunctions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
import java.util.List;
import java.util.Locale;

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
        private List<Data> todoList;
        private List<String> keys;
        private FirebaseDatabase database;
        private DatabaseReference mData;
        private FirebaseUser currentU;
        private String year,month,UId;
        private String key;

        public DataAdapter (List<Data> todoList, List<String> keys){
            this.todoList = todoList;
            this.keys = keys;
        }

        // Set layout cho từng listitem
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(parent);
        }

        // Update cho checkbox và lấy tên task
        @Override
        public void onBindViewHolder(@NonNull RecyclerView_Config.MyViewHolder holder, int position) {

            holder.bind(todoList.get(position), keys.get(position));

            currentU =FirebaseAuth.getInstance().getCurrentUser();
            UId =currentU.getUid();
            year = new SimpleDateFormat("yyyy",Locale.getDefault()).format(new Date());
            month = new SimpleDateFormat("MMM",Locale.getDefault()).format(new Date());
            database =FirebaseDatabase.getInstance();
            mData = database.getReference("User").child(UId).child(year).child(month)
                    .child("Task").child(keys.get(position)).child("Status");

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mData.setValue(1);
                    } else {
                        mData.setValue(0);
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

    class MyViewHolder extends RecyclerView.ViewHolder{

        private CheckBox checkBox;
        private String key;

        public  MyViewHolder (ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.item_tasks, parent, false));

            checkBox = itemView.findViewById(R.id.checkBox);
        }
        public void bind(Data data, String key){
            checkBox.setText(data.getContent());
            checkBox.setChecked(toBoolean(data.getStatus()));
            this.key = key;
        }
    }
}