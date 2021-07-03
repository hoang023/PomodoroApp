package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.TransitionManager;
import android.content.Intent;
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
import com.example.pomodoro.TasksFunctions.TasksActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long startTime = 1*60000; //1p
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupUIView();

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
}
