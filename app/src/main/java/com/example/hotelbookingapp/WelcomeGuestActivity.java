package com.example.hotelbookingapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeGuestActivity extends AppCompatActivity {

    private String roomType;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guest);
        roomType = getIntent().getStringExtra("roomType");
        userEmail = getIntent().getStringExtra("userEmail");

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeGuestActivity.this, RoomInfoActivity.class);
                intent.putExtra("roomType", roomType);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });
    }
}