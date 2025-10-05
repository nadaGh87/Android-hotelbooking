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
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextView tvEmail;
    private EditText edName, edPhone, edCurrentPassword, edNewPassword, edConfirmPassword;
    private Button btnUpdateProfile;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        tvEmail = findViewById(R.id.tvEmail);
        edName = findViewById(R.id.edName);
        edPhone = findViewById(R.id.edPhone);
        edCurrentPassword = findViewById(R.id.edCurrentPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        // user l connec
        userEmail = sharedPreferences.getString("userEmail", "");
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        // Load user data
        loadUserData();
        btnUpdateProfile.setOnClickListener(v -> {
            if (validateInputs()) {
                updateUserProfile();
            }
        });
    }

    private void loadUserData() {
        //njib  user details from databasebl  helper method
        HashMap<String, String> userDetails = databaseHelper.getUserDetails(userEmail);

        if (!userDetails.isEmpty()) {
            tvEmail.setText(userDetails.get("email")); // mayetbdlsh
            edName.setText(userDetails.get("name")); // l current
            edPhone.setText(userDetails.get("phone"));
        } else {
            Toast.makeText(this, "Unable to retrieve user information", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validation lihom lkol
        String name = edName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            edName.setError("Name is required");
            isValid = false;
        } else {
            edName.setError(null);
        }
        String phone = edPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Phone is required");
            isValid = false;
        } else if (phone.length() < 10) {
            edPhone.setError("Please enter a valid phone number");
            isValid = false;
        } else {
            edPhone.setError(null);
        }
        // l password : optional
        String currentPassword = edCurrentPassword.getText().toString().trim();
        String newPassword = edNewPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();
        //Check if any password field has data ken ey , lpassword fields  lkol must be properly filled

        if (!TextUtils.isEmpty(currentPassword) || !TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(confirmPassword)) {
            if (TextUtils.isEmpty(currentPassword)) {
                edCurrentPassword.setError("Current password is required");
                isValid = false;
            } else {
                edCurrentPassword.setError(null);
            }

            if (TextUtils.isEmpty(newPassword)) {
                edNewPassword.setError("New password is required");
                isValid = false;
            } else if (newPassword.length() < 6) {
                edNewPassword.setError("Password must be at least 6 characters");
                isValid = false;
            } else {
                edNewPassword.setError(null);
            }

            //matching
            if (TextUtils.isEmpty(confirmPassword)) {
                edConfirmPassword.setError("Confirm password is required");
                isValid = false;
            } else if (!confirmPassword.equals(newPassword)) {
                edConfirmPassword.setError("Passwords do not match");
                isValid = false;
            } else {
                edConfirmPassword.setError(null);
            }
        }

        return isValid;
    }

    private void updateUserProfile() {
        // jib  values from inputs
        String name = edName.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String currentPassword = edCurrentPassword.getText().toString().trim();
        String newPassword = edNewPassword.getText().toString().trim();

        String passwordToUpdate = null; // ken required matbadlsh
        boolean passwordUpdateSuccess = true; // ken tbadl

        if (!TextUtils.isEmpty(currentPassword) && !TextUtils.isEmpty(newPassword)) {
            if (databaseHelper.checkUser(userEmail, currentPassword)) {
                passwordToUpdate = newPassword;
            } else {
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                passwordUpdateSuccess = false;
            }
        }

        boolean profileUpdateSuccess = false; //
        if (passwordUpdateSuccess) {
            // update user profile fl bd
            profileUpdateSuccess = databaseHelper.updateUserProfile(userEmail, name, phone, passwordToUpdate);
        }

        if (profileUpdateSuccess) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateProfileActivity.this, activity_profile.class);
            startActivity(intent);
            finish();
        } else if (!passwordUpdateSuccess) {
            Toast.makeText(this, "Profile update failed: Incorrect current password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(UpdateProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}