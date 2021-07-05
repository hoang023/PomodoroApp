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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity1";
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
    private TextView stage;
    private TextView tx_status;

    private ImageView detail;
    private Button set;
    private Button tasks;

    private ProgressBar progressBarCircle;
    int focusTime;
    int breakTime;
    int stageNumber;
    int index=0;
    FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
    String UId = currentU.getUid();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref = database.getReference().child("User").child(UId);
    DatabaseReference datamref = mref.child("SetTime");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupUIView();
        setProgressBarValues();
        datamref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
               //Log.d(TAG,snapshot.getValue().toString());
                Status status = snapshot.getValue(Status.class);
                focusTime = Integer.parseInt(status.getFocus().toString());
                breakTime = Integer.parseInt(status.getBreak().toString());
                stageNumber = Integer.parseInt(status.getStage().toString());
              // Log.d(TAG,status.getFocus().toString()+" / "+ status.getBreak().toString()+ " / "+ status.getStage().toString());
                timeLeftInMilliseconds = focusTime * 60000;
                if(stageNumber!=0)
                {
                    stage.setText(index+1  +"/"+stageNumber);
                }
                else
                {
                    stage.setText(0+"/"+stageNumber);
                }

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
                        Status zero = new Status("0","0","0");
                        mref.setValue(zero);
                        play.setVisibility(View.VISIBLE);
                        pause.setVisibility(View.GONE);
                        countdownStop.setVisibility(View.GONE);
                        countdownButton.setImageResource(R.drawable.pause);
                        timeLeftInMilliseconds = 0;
                        setProgressBarValues();
                        stage.setText("0/0");
                        tx_status.setText("Foucus");
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
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void resetTimer() {
        if(tx_status.getText().toString().equals("Focus"))
        {
            timeLeftInMilliseconds = focusTime * 60000;
            setProgressBarValues();
            updateTimer();
            pauseTimer();
        }
        else
        {
            timeLeftInMilliseconds = breakTime*60000;
            setProgressBarValues();
            updateTimer();
            pauseTimer();
        }

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
                TimeRun();
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
        detail = (ImageView) findViewById(R.id.detailtbutton );
        set = (Button) findViewById(R.id.setbutton);
        tasks = (Button) findViewById(R.id.tasksbutton);
        progressBarCircle = (ProgressBar) findViewById(R.id.bg2);
        stage = (TextView) findViewById(R.id.stage);
        tx_status =(TextView) findViewById(R.id.status);

    }
    private void setProgressBarValues() {
        if(timeLeftInMilliseconds!=0)
        {
            progressBarCircle.setMax((int) timeLeftInMilliseconds / 1000);
            progressBarCircle.setProgress((int) timeLeftInMilliseconds / 1000);
        }
        else
        {
            progressBarCircle.setMax((int) 1000 / 1000);
            progressBarCircle.setProgress((int) 1000 / 1000);
        }


    }
    private  void TimeRun(){
        Log.d(TAG,"long 3 "+focusTime+" / "+ breakTime+ " / "+ stageNumber);
        if(tx_status.getText().toString().equals("Focus")) {
            tx_status.setText("Break");
            timeLeftInMilliseconds =breakTime *60000;
            updateTimer();
            setProgressBarValues();
            }
        else {
           if(index < stageNumber-1){
               index++;
               stage.setText(index+1 +"/"+stageNumber);
               tx_status.setText("Focus");
               timeLeftInMilliseconds =focusTime *60000;
               updateTimer();
               setProgressBarValues();
           }
           else {
               index=0;
               stage.setText(index +"/0");
               tx_status.setText("Focus");
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
               Status status0 = new Status("0","0","0");
               mref.setValue(status0);
           }
        }
    }
}
