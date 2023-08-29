package com.example.xpressme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText emailEditText;
    AppCompatButton resetBtn, goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        initButtons();
    }

    private void initButtons() {
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailEditText.getText().toString();
                if (!userEmail.isEmpty()){
                    sendPasswordToEmail(userEmail);
                }
            }
        });

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to login screen
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void sendPasswordToEmail(String userEmail) {
        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(ResetPasswordActivity.this, "Password reset request sent to email");

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
        finish();
    }

    private void initViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.email_user_input);
        resetBtn = findViewById(R.id.reset_btn);
        goBackBtn = findViewById(R.id.go_back_btn);
    }
}