package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettimeActivity extends AppCompatActivity {

    private Button tasksbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);
//        tasksbutton = (Button) findViewById(R.id.tasksbutton);
//
//        tasksbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettimeActivity.this,TaskActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
