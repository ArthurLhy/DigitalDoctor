package com.jxstarxxx.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    private ImageView appName, background;
    private LottieAnimationView lottieAnimationView;
    private float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appName = findViewById(R.id.splash_title);
        lottieAnimationView = findViewById(R.id.lottie_splash);
        background = findViewById(R.id.back_ground_splash);

        appName.setAlpha(v);
        appName.animate().alpha(1).setDuration(1000).setStartDelay(400).start();

        appName.animate().translationY(-2000).setDuration(1000).setStartDelay(5000);
        lottieAnimationView.animate().translationY(2000).setDuration(1000).setStartDelay(5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5800);
    }
}