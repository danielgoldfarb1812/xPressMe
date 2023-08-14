package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, passwordConfirmEditText, phoneEditText;
    TextView loginTextView;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initButtons();
    }

    private void initButtons() {
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = passwordConfirmEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated){
            return;
        }
        createAccountInFirebase(email, password);
    }

    private void createAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,
                task -> {

                    if (task.isSuccessful()){
                        // יצירת החשבון בהצלחה
                        Utility.showToast(RegisterActivity.this, getResources().getString(R.string.account_create_success_verify));
                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                    else{
                        // נכשלה יצירת החשבון
                        Utility.showToast(RegisterActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
                    }
                });
    }

    private boolean validateData(String email, String password, String confirmPassword) {
        // בדיקת תקינות האימייל
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        // בדיקת תקינות אורך הסיסמה
        if (password.length() < 6){
            passwordEditText.setError(getResources().getString(R.string.password_error_length));
            return false;
        }
        // בדיקת התאמה בין הסיסמאות
        if (!password.equals(confirmPassword)){
            passwordConfirmEditText.setError(getResources().getString(R.string.password_error_match));
            return false;
        }
        return true;
    }

    private void initViews() {
        firstNameEditText = findViewById(R.id.first_name_user_input);
        lastNameEditText = findViewById(R.id.last_name_user_input);
        emailEditText = findViewById(R.id.email_user_input);
        passwordEditText = findViewById(R.id.password_user_input);
        passwordConfirmEditText = findViewById(R.id.password_user_input);
        phoneEditText = findViewById(R.id.phone_user_input);
        loginTextView = findViewById(R.id.login_now_textview);
        registerBtn = findViewById(R.id.register_btn);
    }
}