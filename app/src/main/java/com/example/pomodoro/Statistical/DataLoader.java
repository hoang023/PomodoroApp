package com.example.pomodoro.Statistical;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.pomodoro.TasksFunctions.TodoTASK;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DataLoader {

    private Context mContext;
    private String mUserId;

    public DataLoader(Context mContext, String mUserId) {
        this.mContext = mContext;
        this.mUserId = mUserId;
    }

    private String[] monthList = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private DataLoadListener mDataLoadListener;

    private ArrayList<Entry> taskList = new ArrayList<>();

    public ArrayList<Entry> getTaskList() {
        return taskList;
    }
    public void getData() {
        mDataLoadListener = (DataLoadListener) mContext;
        for (int i = 0; i < monthList.length; i++) {
            int finalI = i;
            FirebaseDatabase.getInstance().getReference().child("User")
                    .child(mUserId)
                    .child("2021")
                    .child(monthList[i])
                    .child("Task")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            int x = 0;
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                TodoTASK todoTASK = dataSnapshot.getValue(TodoTASK.class);
                                if (todoTASK.getStatus() == 1) {
                                    x+=1;
                                };
                            }
                            taskList.add(new Entry(finalI + 1, x));
                            if (finalI == monthList.length - 1) {
                                mDataLoadListener.onDataLoadComplete();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }
}
