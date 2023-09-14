package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    /*
      TODO:
        edit board
        delete board
        move to another board by button press
        create board for users
        upload images and record audio (be able to access from different devices)
        instructions
        demonstration clip
        admin login: for creating additional presets
        if possible - translate to hebrew
     */
    ImageView gifLeft, gifRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);
        runGifs();

        int DELAY_TIME = 2000;
        new Handler().postDelayed(() -> {
            // check if user is logged in. if they are, move to main activity
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                startActivity(new Intent(SplashActivity.this, BoardSelectionActivity.class));
            }
            else{
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
                finish();
        }, DELAY_TIME);
    }

    private void runGifs() {
        gifLeft = findViewById(R.id.gif_left);
        gifRight = findViewById(R.id.gif_right);
        Glide.with(this).load(R.drawable.butterflies_gif).into(gifLeft);
        Glide.with(this).load(R.drawable.butterflies_gif).into(gifRight);
    }
}