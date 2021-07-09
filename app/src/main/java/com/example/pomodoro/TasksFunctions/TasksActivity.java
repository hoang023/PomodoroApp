package com.example.pomodoro.TasksFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.SetTimeFunctions.SettimeActivity;
import com.example.pomodoro.Statistical.StatisticalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private Button set, detail;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private RecyclerView_Config adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        set = (Button) findViewById(R.id.setbutton);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TasksActivity.this, SettimeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        detail = (Button) findViewById(R.id.detailtbutton );
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TasksActivity.this, StatisticalActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TasksActivity.this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksAdd.newInstance().show(getSupportFragmentManager(), TasksAdd.TAG);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy>0){
                    fab.hide();
                } else {
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        new FirebaseDatabaseHelper().showData(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Data> mList, List<String> keys) {
                new RecyclerView_Config().setConfig(recyclerView, TasksActivity.this, mList, keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
}