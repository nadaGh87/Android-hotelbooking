package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_profile extends AppCompatActivity {

    private TextView tvProfileName, tvProfileEmail, tvProfilePhone;
    private Button btnViewBookings, btnBookRoom, btnLogout, btnEditProfile;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        btnLogout = findViewById(R.id.btnLogout);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        loadUserProfile();

        setupClickListeners();
    }

    @Override
    protected void onResume() {
        // Called when the activity becomes visible to the user again
        //Reloads user profile data bsh tchuf changement
        super.onResume(); //parent method
        loadUserProfile();
    }

    private void loadUserProfile() {
        //ythabet ml db
        String userEmail = sharedPreferences.getString("userEmail", "");

        if (userEmail.isEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }
                               // instance
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = {"name", "email", "phone"};
        String selection = "email = ?"; //bsh nalka specific user
        String[] selectionArgs = {userEmail}; //ml shered...
          // retreive user data
        Cursor cursor = db.query(
                "hotel_users",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
      //Chek ken yraja3 RES or no o taada leli baadu
        if (cursor != null && cursor.moveToFirst()) {
            // njib  user data from the cursor
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));

            // Display user info ml user interface  tawa :
            tvProfileName.setText("Name: " + name);
            tvProfileEmail.setText("Email: " + email);
            tvProfilePhone.setText("Phone: " + phone);

            cursor.close(); //free the ressources
        } else {
            Toast.makeText(this, "Unable to retrieve user information", Toast.LENGTH_SHORT).show();
            redirectToLogin();
        }

        db.close();
    }

    private void setupClickListeners() {
        //al bouton
        btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profile.this, ViewBookingsActivity.class);
                startActivity(intent);
            }
        });

        btnBookRoom.setOnClickListener(new View.OnClickListener() {
            //kifha bouton lokhra
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // njib sharedpre editor bch naaml modify data
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); //remove stored ones
                editor.apply(); //zid changes

                redirectToLogin();
                Toast.makeText(activity_profile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            //lel edit
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profile.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void redirectToLogin() {

        Intent intent = new Intent(activity_profile.this, LoginActivity.class);
        //  flags  :  user maynjmsh yarj3 bl back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent); // login act
        finish();
    }
}