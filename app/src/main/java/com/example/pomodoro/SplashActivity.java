package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {

    TextView loadTxt, madeTxt;
    ImageView iconImage, pomodoroImage, loadingCircle;
    Animation topAnm, bottomAnm, rotate;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    //firebaseUser = firebaseAuth.getCurrentUser();


    //mFirebaseAuth = FirebaseAuth.getInstance();


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
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    int SPLASH_SCREEN = 3000;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();

                        }
                    }, SPLASH_SCREEN);
                }
                else {
                    int SPLASH_SCREEN = 3000;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                            finish();
                        }
                    }, SPLASH_SCREEN);
                }
            }
        };
    }
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
