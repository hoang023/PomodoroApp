package com.example.pomodoro.Task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.R;

import java.util.Date;

public class AddDataActivity extends AppCompatActivity {
    EditText subjectEt;
    Button cancelBt,saveBt,shareBt;
    SqliteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prompts2);

        mydb = new SqliteDatabase(this);

        subjectEt = findViewById(R.id.subjectEditTextId);

        cancelBt = findViewById(R.id.cacelButtonId);
        saveBt = findViewById(R.id.saveButtonId);
        shareBt = findViewById(R.id.shareButtonId);

        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passing data via intent
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sub = subjectEt.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                backToMain();
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMain();
            }
        });

    }

    //for inserting new data
    public void insertData(){
        long l = -1;

        Date date = new Date();
        String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy  hh:mm:ss",date);

        if(subjectEt.getText().length() == 0){
            Toast.makeText(getApplicationContext(),"You didn't add any subject",Toast.LENGTH_SHORT).show();
        }
        else{
            l = mydb.insertData(subjectEt.getText().toString(),d);
        }

        if(l>=0){
            Toast.makeText(getApplicationContext(),"Data added",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Data not added", Toast.LENGTH_SHORT).show();
        }
    }
    public void backToMain()
    {
        Intent intent = new Intent(AddDataActivity.this,MainTask.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}