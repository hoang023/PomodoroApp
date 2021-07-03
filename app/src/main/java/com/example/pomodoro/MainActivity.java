package com.example.pomodoro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.TransitionManager;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.SetTimeFunctions.SettimeActivity;
import com.example.pomodoro.SetTimeFunctions.Status;
import com.example.pomodoro.TasksFunctions.TasksActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    private static final String TAG = "MyActivity";
    private static final long startTime = 0*60000; //1p
    private Button play;
    private ViewGroup pause;
    private Button skipb;
    private ViewGroup transitionsContainer;

    private ImageView countdownButton;
    private TextView countdownText;
    private Button countdownStop;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = startTime;
    private boolean timerRunning ;

    private Button detail;
    private Button set;
    private Button tasks;

    private ProgressBar progressBarCircle;

    FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
    String UId = currentU.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference().child("User").child(UId).child("SetTime");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupUIView();

        //Data
        mref.addValueEventListener(this);


        //Play
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeLeftInMilliseconds !=0) {
                    TransitionManager.beginDelayedTransition(transitionsContainer);
                    play.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    countdownStop.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.righttoleft);
                    pause.startAnimation(animation);
                    Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.lefttoright);
                    countdownStop.startAnimation(animation1);
                    setProgressBarValues();
                    startTimer();
                }
                else {
                    Toast.makeText(MainActivity.this,"you have to set the time",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Chuyển màn hình detail
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, StatisticalActivity.class);
                startActivity(intent);
            }
        });
        //Chuyển màn hình settime
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, SettimeActivity.class);
                startActivity(intent);
            }
        });
        //Chuyển màn hình task
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent =new Intent(MainActivity.this, TasksActivity.class);
                startActivity(intent);
            }
        });
        //Skip
        skipb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                countdownStop.setVisibility(View.GONE);
                countdownButton.setImageResource(R.drawable.pause);
                timeLeftInMilliseconds = 0;
                setProgressBarValues();
                updateTimer();
                if(timerRunning) {
                    countDownTimer.cancel();
                }
            }
        });
        //Pause and Start time
        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                }
            }
        });
        //Reset time
        countdownStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateTimer();
    }

    private void resetTimer() {
        timeLeftInMilliseconds = startTime;
        setProgressBarValues();
        updateTimer();
        pauseTimer();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 500) {
            @Override
            public void onTick(long millsUntilFinished) {
                timeLeftInMilliseconds = millsUntilFinished;
                progressBarCircle.setProgress((int) (millsUntilFinished / 1000));
                updateTimer();
            }
            @Override
            public void onFinish() {
                countdownText.setText("00:00");
                setProgressBarValues();
                timerRunning = false;
                countdownButton.setImageResource(R.drawable.playbutton);
            }
        }.start();
        timerRunning = true;
        countdownButton.setImageResource(R.drawable.pause);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        countdownButton.setImageResource(R.drawable.playbutton);
        timerRunning = false;
    }
    private void updateTimer() {
        int minutes = (int) (timeLeftInMilliseconds / 1000) / 60;
        int seconds = (int) (timeLeftInMilliseconds / 1000) % 60;
        String timeLefText = String.format(Locale.getDefault(), "%02d:%02d", minutes , seconds);
        countdownText.setText(timeLefText);
    }

    private void SetupUIView() {
        transitionsContainer = (ViewGroup) findViewById(R.id.relative);
        play = (Button) findViewById(R.id.play);
        countdownButton = findViewById(R.id.pausebutton);
        pause = (ViewGroup) findViewById(R.id.pause);
        skipb = (Button) findViewById(R.id.skipbutton);
        countdownText = (TextView) findViewById(R.id.time);
        countdownStop = findViewById(R.id.stop);
        detail = (Button) findViewById(R.id.detailtbutton );
        set = (Button) findViewById(R.id.setbutton);
        tasks = (Button) findViewById(R.id.tasksbutton);
        progressBarCircle = (ProgressBar) findViewById(R.id.bg2);

    }
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeLeftInMilliseconds / 1000);
        progressBarCircle.setProgress((int) timeLeftInMilliseconds / 1000);
    }


    @Override
    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
        Status status = snapshot.getValue(Status.class);
        Log.d(TAG, status.getFocus().toString());
    }

    @Override
    public void onCancelled(@NonNull @NotNull DatabaseError error) {

    }
}
