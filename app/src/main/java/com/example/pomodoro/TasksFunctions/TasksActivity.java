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
import com.example.pomodoro.StatisticalActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksActivity extends AppCompatActivity {

    private Button set, detail;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DataAdapter adapter;
    private List<Data> mList;
    private FirebaseDatabase database;

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
            }
        });

        detail = (Button) findViewById(R.id.detailtbutton );
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TasksActivity.this, StatisticalActivity.class);
                startActivity(intent);
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

        mList = new ArrayList<>();
        adapter = new DataAdapter(TasksActivity.this, mList);

        recyclerView.setAdapter(adapter);
    }

        private void showData(){
        FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mData = database.getReference("Users");
        String UId = currentU.getUid();
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());

        mData.child(UId).child(year).child(month).child("Task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    mList.add(data);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}