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

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText emailUserInput, passwordUserInput;
    TextView forgotPasswordTextView, createAccountTextView;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initButtons();

    }


    private void initViews() {
        emailUserInput = findViewById(R.id.email_user_input);
        passwordUserInput = findViewById(R.id.password_user_input);
        forgotPasswordTextView = findViewById(R.id.forget_password_textview);
        createAccountTextView = findViewById(R.id.create_account_textview);
        loginBtn = findViewById(R.id.login_btn);
        fAuth = FirebaseAuth.getInstance();
    }
    private void initButtons() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private void loginUser() {
        // קבלת הקלט מתיבת הטקסט לאימייל ולסיסמה
        String email = emailUserInput.getText().toString();
        String password = passwordUserInput.getText().toString();

        // אימות תקינות הקלט
        boolean isValidated = validateData(email, password);
        if (!isValidated){
            return;
        }
        boolean isAdmin = validateAdmin(email, password);
        if (isAdmin){
            loginWithFirebase(email, password);
            startActivity(new Intent(LoginActivity.this, AdminCreateBoardActivity.class));
            finish();
            return;
        }

        // התחברות עם האימייל והסיסמה לפיירבייס
        loginWithFirebase(email, password);
    }

    private boolean validateAdmin(String email, String password) {
        return email.equals("admin@admin.admin") && password.equals("administrator");
    }

    private void loginWithFirebase(String email, String password) {
        // התחברות לפיירבייס באמצעות אימייל וסיסמה

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // בסיום התהליך, שינוי התצוגה בהתאם לתוצאה
            if (task.isSuccessful()){
                // התחברות הצליחה
                if (Objects.requireNonNull(fAuth.getCurrentUser()).isEmailVerified()){
                    // האימייל מאומת - עבור למסך הראשי
                    startActivity(new Intent(LoginActivity.this, BoardSelectionActivity.class));
                    finish();
                }
                else{
                    // האימייל אינו מאומת - הודעה למשתמש
                    Utility.showToast(LoginActivity.this, getResources().getString(R.string.email_verification_error));
                }
            }
            else{
                // התחברות נכשלה - הודעת שגיאה למשתמש
                Utility.showToast(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });
    }

    private boolean validateData(String email, String password) {
        // אימות הקלט שהוזן על ידי המשתמש

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailUserInput.setError(getResources().getString(R.string.invalid_email));
            return false;
        }
        if (password.length() < 6){
            passwordUserInput.setError(getResources().getString(R.string.password_error_length));
            return false;
        }
        return true;
    }

}