package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// my explication : LoginActivity class inherits from AppCompatActivity for Android UI compatibility
public class LoginActivity extends AppCompatActivity {
    private Button btnSubmit;
    private EditText edEmail, edPassword;
    private TextView tvSignUp;
    private DatabaseHelper databaseHelper;
    //explication its the Helper class for the database operations
    private SharedPreferences sharedPreferences;
    //explication i use  sharedPreferences to store user data locally
//instance to handle database operations
    @Override
    protected void onCreate(Bundle savedInstanceState) { //activity started
        super.onCreate(savedInstanceState); //superclass's implementation are being calles
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE); //private means accesible only for this app

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSignUp = findViewById(R.id.tvSignUp);

        // User is already logged in?
        if (sharedPreferences.getString("userEmail", null) != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();  //user can't go back to login with the back button
        }

        btnSubmit.setOnClickListener(v -> { // bouton login
            if (validateForm()) { //to check if input is valid (method)
                String email = edEmail.getText().toString().trim(); // removes spaces from both ends of a string
                String password = edPassword.getText().toString().trim();

                if (databaseHelper.checkUser(email, password)) {
                    sharedPreferences.edit().putString("userEmail", email)
                            //Stores user email in sharedpref
                            .apply();

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private boolean validateForm() { //for user input
        boolean valid = true; //default true
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) { //email filed should not be empty
            edEmail.setError("Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Enter valid email");
            valid = false; //it cant be empty
        }

        if (TextUtils.isEmpty(password)) { //same here
            edPassword.setError("Password required");
            valid = false;
        } else if (password.length() < 6) {
            edPassword.setError("Password too short"); //u should put a strong password
            valid = false;
        }

        return valid;
    }
}