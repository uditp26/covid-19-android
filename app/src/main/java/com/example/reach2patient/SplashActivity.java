package com.example.reach2patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private ProgressBar splashProgress;
    private int oldProgress = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashProgress = findViewById(R.id.splashProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        splashProgress.setProgress(0);
        Thread bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(splashProgress.getProgress() < 100){
                    updateProgress();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bgThread.start();
    }

    private void updateProgress() {
        oldProgress += 17;
        splashProgress.setProgress(oldProgress);
        Log.d(TAG, "Progress: " + splashProgress.getProgress());
    }
}