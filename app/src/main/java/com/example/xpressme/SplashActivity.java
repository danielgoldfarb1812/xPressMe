package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);

        int DELAY_TIME = 2000;
        new Handler().postDelayed(() -> {
            // check if user is logged in. if they are, move to main activity
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                startActivity(new Intent(SplashActivity.this, CreateBoardActivity.class));
            }
            else{
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
                finish();
        }, DELAY_TIME);
    }
}