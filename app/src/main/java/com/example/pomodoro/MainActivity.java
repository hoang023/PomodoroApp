package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button play;
    private Button play_b;
    private Button pause_b;
    private ViewGroup pause;
    private Button stopb;
    private Button skipb;
    private ViewGroup transitionsContainer;
    private boolean visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("onCreate","Executed");
        SetupUIView();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("aaa","Executed");
                TransitionManager.beginDelayedTransition(transitionsContainer);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                stopb.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.righttoleft);
                pause.startAnimation(animation);
                Animation animation1 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.lefttoright);
                stopb.startAnimation(animation1);
            }
        });
        skipb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer);
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                stopb.setVisibility(View.GONE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(transitionsContainer);
                Log.d("Debug pause", "Executed");
                visible =!visible;
                play_b.setVisibility(visible? View.VISIBLE : View.GONE);
                pause_b.setVisibility(visible? View.GONE : View.VISIBLE);
            }
        });
    }
    private void SetupUIView() {
        transitionsContainer = (ViewGroup) findViewById(R.id.relative);
        play = (Button) findViewById(R.id.play);
        play_b = (Button) findViewById(R.id.playbutton);
        pause_b = (Button) findViewById(R.id.pausebutton);
        stopb = (Button) findViewById(R.id.stop);
        pause = (ViewGroup) findViewById(R.id.pause);
        skipb = (Button) findViewById(R.id.skipbutton);
    }
}