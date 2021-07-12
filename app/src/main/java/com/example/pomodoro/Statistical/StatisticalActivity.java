package com.example.pomodoro.Statistical;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.R;
import com.example.pomodoro.SetTimeFunctions.SettimeActivity;
import com.example.pomodoro.TasksFunctions.TasksActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatisticalActivity extends AppCompatActivity implements DataLoadListener, PomodoroLoadListener{

    private static final String TAG = "StatisticalActivity";

    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
    String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());

    ArrayList<Entry> taskList = new ArrayList<>();
    ArrayList<Entry> pomodoroList = new ArrayList<>();
    DataLoader mDataloader = new DataLoader(this, userId);
    PomodoroLoader mPomodotoloader = new PomodoroLoader(this, userId);
    private LineChart task_lineChart, pomodoro_lineChart;
    FirebaseDatabase firebaseDatabase, monthDatabase2;
    DatabaseReference databaseReference;
    private Button task_btn, pomodoro_btn;
    private Button setButton, taskButton;
    private ImageView returnButton;
    private TextView stage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        stage =(TextView) findViewById(R.id.stage);
        stage.setText(getIntent().getStringExtra("key_stage"));
        task_lineChart = findViewById(R.id.task_lineChart);
        task_lineChart.setScaleEnabled(false);
        task_lineChart.setDragEnabled(true);
        pomodoro_lineChart = findViewById(R.id.pomodoro_lineChart);
        pomodoro_lineChart.setScaleEnabled(false);
        pomodoro_lineChart.setDragEnabled(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("User").child(userId).child(year);
        monthDatabase2 = FirebaseDatabase.getInstance();
        task_btn = (Button) findViewById(R.id.task_statistic_btn);
        pomodoro_btn = (Button) findViewById(R.id.pomodoro_statistic_btn);
        setButton =(Button) findViewById(R.id.setbutton);
        taskButton = (Button) findViewById(R.id.taskbutton);
        returnButton =(ImageView) findViewById(R.id.detailtbutton);
        mPomodotoloader.getTotalTimePomodoro();
        mDataloader.getData();
        task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoro_lineChart.setVisibility(View.INVISIBLE);
                task_lineChart.setVisibility(View.VISIBLE);
            }
        });
//        mDataloader.getData();
        pomodoro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoro_lineChart.setVisibility(View.VISIBLE);
                task_lineChart.setVisibility(View.INVISIBLE);
            }
        });
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strStage = stage.getText().toString();
                Intent intent = new Intent(StatisticalActivity.this, SettimeActivity.class);
                intent.putExtra("key_stage",strStage);
                startActivity(intent);
                finish();
            }
        });
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strStage = stage.getText().toString();
                Intent intent = new Intent(StatisticalActivity.this, TasksActivity.class);
                intent.putExtra("key_stage",strStage);
                startActivity(intent);
                finish();
            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void onDataLoadComplete() {
        taskList = mDataloader.getTaskList();
        LineDataSet set1 =new LineDataSet(taskList, "Task statistical");
        task_lineChart.getLegend().setTextColor(Color.WHITE);
        task_lineChart.getLegend().setForm(Legend.LegendForm.LINE);
        task_lineChart.setViewPortOffsets(100,100,100,70);
        task_lineChart.setPinchZoom(false);
        task_lineChart.setDrawGridBackground(false);
        task_lineChart.getDescription().setEnabled(false);
        task_lineChart.setDrawBorders(true);
        task_lineChart.setBackgroundColor(Color.rgb(32,2,43));
        set1.setColor(Color.rgb(167,191,251));
        set1.setCircleColor(Color.rgb(231,15,250));
        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setValueTextSize(10);
        set1.setDrawFilled(true);
        set1.setCircleRadius(4);
        set1.setCircleHoleRadius(3);
        set1.setValueTextColor(Color.WHITE);
        XAxis x1 = task_lineChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setTextSize(10);
        YAxis y1 = task_lineChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setTextSize(10);
        x1.setLabelCount(12, true);
        YAxis y2 = task_lineChart.getAxisRight();
        y2.setTextColor(Color.WHITE);
        y2.setTextSize(10);
        x1.setDrawGridLines(true);
        y1.setDrawGridLines(true);
        y2.setDrawGridLines(false);
        set1.setFillAlpha(255);
        set1.setHighLightColor(Color.WHITE);
        set1.setFillColor(Color.rgb(131,16,149));
        set1.setLineWidth(2f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        task_lineChart.setData(data);
    }
    @Override
    public void onPomodoroLoadComlete() {
        pomodoroList = mPomodotoloader.getPomodoroList();
        LineDataSet set =new LineDataSet(pomodoroList, "Pomodoro statistical");
        pomodoro_lineChart.getLegend().setTextColor(Color.WHITE);
        task_lineChart.getLegend().setForm(Legend.LegendForm.LINE);
        pomodoro_lineChart.setViewPortOffsets(100,100,100,70);
        pomodoro_lineChart.setPinchZoom(false);
        pomodoro_lineChart.setDrawGridBackground(false);
        pomodoro_lineChart.getDescription().setEnabled(false);
        pomodoro_lineChart.setDrawBorders(true);
        pomodoro_lineChart.setBackgroundColor(Color.rgb(32,2,43));
        set.setColor(Color.rgb(167,191,251));
        set.setCircleColor(Color.rgb(231,15,250));
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setValueTextSize(10);
        set.setDrawFilled(true);
        set.setCircleRadius(4);
        set.setCircleHoleRadius(3);
        set.setValueTextColor(Color.WHITE);
        XAxis x1 = pomodoro_lineChart.getXAxis();
        x1.setLabelCount(12, true);
        x1.setTextColor(Color.WHITE);
        x1.setTextSize(10);
        YAxis y1 = pomodoro_lineChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setTextSize(10);
        YAxis y2 = pomodoro_lineChart.getAxisRight();
        y2.setTextColor(Color.WHITE);
        y2.setTextSize(10);
        x1.setDrawGridLines(true);
        y1.setDrawGridLines(true);
        y2.setDrawGridLines(false);
        set.setFillAlpha(255);
        set.setHighLightColor(Color.WHITE);
        set.setFillColor(Color.rgb(131,16,149));
        set.setLineWidth(2f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);
        LineData data = new LineData(dataSets);
        pomodoro_lineChart.setData(data);
        Log.d("Data chart", "" + pomodoroList.toString());
    }
}




