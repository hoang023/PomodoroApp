package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.SignIn.SignInActivity;

public class SplashActivity extends AppCompatActivity {

    TextView loadTxt, madeTxt;
    ImageView iconImage, pomodoroImage, loadingCircle;
    Animation topAnm, bottomAnm, rotate;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Ainimation
        bottomAnm = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        topAnm = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        rotate = AnimationUtils.loadAnimation(this, R.anim.loading_rotate);

        //Hooks
        iconImage = findViewById(R.id.icon1);
        pomodoroImage = findViewById(R.id.pomodoro);
        loadTxt = findViewById(R.id.txtloading);
        madeTxt = findViewById(R.id.txtmade);
        loadingCircle = findViewById(R.id.loadingCircle);

        loadingCircle.setAnimation(rotate);
        iconImage.setAnimation(topAnm);
        pomodoroImage.setAnimation(bottomAnm);
        madeTxt.setAnimation(bottomAnm);

        int SPLASH_SCREEN = 3000;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}
