package com.example.pomodoro.TasksFunctions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro.R;
import com.example.pomodoro.SetTimeFunctions.SettimeActivity;
import com.example.pomodoro.StatisticalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TasksActivity extends AppCompatActivity {

    private Button set, detail;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

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