package com.example.xpressme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText emailUserInput, passwordUserInput;
    TextView forgotPasswordTextView, createAccountTextView;
    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initButtons();
        addPasswordListener();
    }

    //this function fixes the right to left password hint
    private void addPasswordListener(){
        passwordUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0){
                    passwordUserInput.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                            passwordUserInput.setSelection(editable.length());
                }
                else{
                    passwordUserInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }
            }
        });
    }
    private void initViews() {
        emailUserInput = findViewById(R.id.email_user_input);
        passwordUserInput = findViewById(R.id.password_user_input);
        forgotPasswordTextView = findViewById(R.id.forget_password_textview);
        createAccountTextView = findViewById(R.id.create_account_textview);
        loginBtn = findViewById(R.id.login_btn);
    }
    private void initButtons() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showToast(LoginActivity.this, "no login method yet");
                return;
            }
        });
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                return;
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.showToast(LoginActivity.this, "no forgot password method yet");
                return;
            }
        });
    }

}