package com.example.pomodoro.TasksFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.SetTimeFunctions.SettimeActivity;
import com.example.pomodoro.StatisticalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private Button set, detail;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final TodoAdapter adapter=new TodoAdapter();
    //private RecyclerView_Config.DataAdapter adapter = new RecyclerView_Config.DataAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //Chuyen man hinh qua set time
        set = (Button) findViewById(R.id.setbutton);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TasksActivity.this, SettimeActivity.class);
                startActivity(intent);
            }
        });

        //Chuyen man hinh qua detail
        detail = (Button) findViewById(R.id.detailtbutton);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TasksActivity.this, StatisticalActivity.class);
                startActivity(intent);
            }
        });

        //Anh xa
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TasksActivity.this));
        recyclerView.setAdapter(adapter);

        //Button them task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksAdd.newInstance().show(getSupportFragmentManager(), TasksAdd.TAG);
            }
        });

        //Su kien scroll cho floating action button (an/hien)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //Hien data tu firebase len recyclerview
        new FirebaseDatabaseHelper().showData(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<TodoTASK> mList) {
                adapter.setTasks(mList);
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