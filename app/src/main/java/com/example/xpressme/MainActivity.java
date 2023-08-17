package com.example.xpressme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DarkAlertDialog);
        builder.setMessage("Do you want to logout or exit the app?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Logout
                        // Your logout code here
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Exit
                        finishAffinity(); // Closes all activities
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}