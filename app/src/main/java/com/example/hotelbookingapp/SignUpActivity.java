package com.example.hotelbookingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName, edPhone, edEmail, edPassword, edConfirmPassword;
    private Button btnSignUp;
    private TextView tvSignIn;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseHelper = new DatabaseHelper(this);

        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // check if all inputs are valid :
                if (validateForm()) {
                    String name = edName.getText().toString().trim();
                    String phone = edPhone.getText().toString().trim();
                    String email = edEmail.getText().toString().trim();
                    String password = edPassword.getText().toString().trim();

                    if (databaseHelper.checkUser(email)) {
                        Toast.makeText(SignUpActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    boolean success = databaseHelper.addUser(name, phone, email, password);

                    if (success) {
                        Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent); //start login
                        finish(); //close current activity
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = edName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) { //check if it's empty (the field)
            edName.setError("Name is required");
            valid = false;
        } else {
            edName.setError(null);
        }

        String phone = edPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Phone number is required");
            valid = false;
        } else if (phone.length() < 10) {
            edPhone.setError("Please enter a valid phone number");
            valid = false;
        } else {
            edPhone.setError(null);
        }

        String email = edEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Please enter a valid email address");
            valid = false;
        } else {
            edEmail.setError(null);
        }

        String password = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            edPassword.setError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            edPassword.setError("Password must be at least 6 characters");
            valid = false;
        } else {
            edPassword.setError(null);
        }

        String confirmPassword = edConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPassword)) {
            edConfirmPassword.setError("Please confirm your password");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            edConfirmPassword.setError("Passwords do not match");
            valid = false;
        } else {
            edConfirmPassword.setError(null);
        }

        return valid;
    }
}