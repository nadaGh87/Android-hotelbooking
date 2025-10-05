package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone, tvAddress, tvNumberOfPeople;
    private TextView tvRoomType, tvCheckIn, tvCheckOut, tvNumberOfRooms;
    private Button btnConfirm, btnCancel;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    private String name, email, phone, address, numberOfPeople;
    private String roomType, checkIn, checkOut, numberOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);


        databaseHelper = new DatabaseHelper(this);

        // njib  user data from session l mahlula
        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);

        initializeViews();
        getIntentData();
        setDataToViews();
        setupClickListeners();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvNumberOfPeople = findViewById(R.id.tvNumberOfPeople);
        // room
        tvRoomType = findViewById(R.id.tvRoomType);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        tvNumberOfRooms = findViewById(R.id.tvNumberOfRooms);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void getIntentData() {
        Intent intent = getIntent();

        // get pers info ml intent
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        numberOfPeople = intent.getStringExtra("numberOfPersonne");

        // get room info kifha
        roomType = intent.getStringExtra("roomType");
        checkIn = intent.getStringExtra("checkIn");
        checkOut = intent.getStringExtra("checkOut");
        numberOfRooms = intent.getStringExtra("num");
    }

    private void setDataToViews() {
        //  text without prefixes :sart moshkla mte3 duplic ma bin l ui  o code java so :
        tvName.setText(name);
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvAddress.setText(address);
        tvNumberOfPeople.setText(numberOfPeople);

        // text without prefixes
        tvRoomType.setText(roomType);
        tvCheckIn.setText(checkIn);
        tvCheckOut.setText(checkOut);
        tvNumberOfRooms.setText(numberOfRooms);
    }

    private void setupClickListeners() {
        //al save confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReservation();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back to home activ
                Intent intent = new Intent(FinalActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveReservation() {
        // email user l 7alel
        String userEmail = sharedPreferences.getString("userEmail", email);
        String cleanAddress = address;
        if (address != null && address.startsWith("somewhere ")) {
            cleanAddress = address.substring("somewhere ".length());
        }

        // save res fl db
        boolean isSuccess = databaseHelper.addReservation(
                userEmail,
                name,
                cleanAddress,
                phone,
                numberOfPeople,
                roomType,
                checkIn,
                checkOut,
                numberOfRooms
        );

        if (isSuccess) {
            Toast.makeText(this, "Reservation saved successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FinalActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //l flag :
            //Si l'activité spécifiée existe déjà  toutes les activités l fu9ha  ytfaskhu
            //  o l activity   relance
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save reservation. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}