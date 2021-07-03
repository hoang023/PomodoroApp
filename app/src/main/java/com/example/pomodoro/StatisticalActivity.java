package com.example.pomodoro;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;

public class StatisticalActivity extends AppCompatActivity {

    private LineChart lineChart;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        lineChart.setScaleEnabled(true);
        lineChart.setDragEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public class DataPoint {
        int xValue, yValue;

        public DataPoint(int xValue, int yValue) {
            this.xValue = xValue;
            this.yValue = yValue;
        }

        public int getxValue() {
            return xValue;
        }

        public int getyValue() {
            return yValue;
        }
    }
}




