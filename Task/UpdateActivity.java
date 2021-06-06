package com.example.pomodoro.Task;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.R;

import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    EditText subjectEt;
    Button cancelBt,updateBt,shareBtOnUpdate;
    SqliteDatabase dbUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);

        //passing Update Activity's context to database alass
        dbUpdate = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = dbUpdate.getWritableDatabase();

        subjectEt = findViewById(R.id.subjectEditTextIdUpdate);

        cancelBt = findViewById(R.id.cacelButtonIdUpdate);
        updateBt = findViewById(R.id.saveButtonIdUpdate);
        shareBtOnUpdate = findViewById(R.id.shareButtonIdUpdate);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        String sub = intent.getStringExtra("subject");
        final String id = intent.getStringExtra("listId");


        subjectEt.setText(sub);

        //for sharing data to social media
        shareBtOnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sub = subjectEt.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //for updating database data
        updateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy  hh:mm:ss",date);

                if(dbUpdate.update(subjectEt.getText().toString(),d,id)==true){
                    Toast.makeText(getApplicationContext(),"Data updated",Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
        });
    }

    //this method to clearing top activity and starting new activity
    public void backToMain()
    {
        Intent intent = new Intent(UpdateActivity.this,MainTask.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}