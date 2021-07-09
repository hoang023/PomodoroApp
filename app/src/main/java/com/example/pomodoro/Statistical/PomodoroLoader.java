package com.example.pomodoro.Statistical;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pomodoro.Pomodoro;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PomodoroLoader {
    private Context mContext;
    private String mUserID;

    public PomodoroLoader(Context mContext, String mUserID) {
        this.mContext = mContext;
        this.mUserID = mUserID;
    }
    private String[] monthList = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    private PomodoroLoadListener mPomodoroLoadListener;

    private ArrayList<Entry> pomodoroList = new ArrayList<>();

    public ArrayList<Entry> getPomodoroList(){
        return pomodoroList;
    }

    public void getTotalTimePomodoro(){
        mPomodoroLoadListener = (PomodoroLoadListener) mContext;
        for (int i = 0; i < monthList.length; i++) {
            int finalI = i;
            pomodoroList.add(new Entry(finalI + 1, 0));
            FirebaseDatabase.getInstance().getReference().child("User")
                    .child(mUserID)
                    .child("2021")
                    .child(monthList[i])
                    .child("Pomodoro")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Pomodoro pomodoro = snapshot.getValue(Pomodoro.class);
                    Log.d("ChartPomodoro 1", "" + pomodoroList.toString());
                    if (pomodoro != null) {
                        int totalTime = Integer.parseInt(pomodoro.getTotalTime());
                        pomodoroList.set(finalI,new  Entry(finalI + 1,totalTime));
                    }
                    if (finalI == monthList.length - 1){
                        mPomodoroLoadListener.onPomodoroLoadComlete();
                }
                    Log.d("ChartPomodoro", "" + pomodoroList.toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }
    }
}
