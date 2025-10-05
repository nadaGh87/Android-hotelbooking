package com.example.hotelbookingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final String[] roomTypes = {
            "Luxury Suite",
            "Executive Room",
            "Family Room",
            "Presidential Suite"
    };

    private final double[] roomPrices = {
            349.00, // Luxury Suite
            249.00, // Executive Room
            299.00, // Family Room
            599.00  // Presidential Suite
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("HotelBookingPrefs", MODE_PRIVATE);

        // Setup click listeners for all book buttons
        setupBookButtons();
    }
    //card : comme container of elem
    private void setupBookButtons() { //for all booking buttons
        int[] cardViewIds = {
                R.id.cardView1,
                R.id.cardView2,
                R.id.cardView3,
                R.id.cardView4
        };

        for (int i = 0; i < cardViewIds.length; i++) { //for each one , find each one in the loop :
            CardView cardView = findViewById(cardViewIds[i]);
            if (cardView != null) { //if it's found
                setupButtonInCard(cardView, i); // match with each room index ( esm l room yaa3ni )
            }
        }
    }

    private void setupButtonInCard(CardView cardView, int roomIndex) { //button in the card  (layout)
        ViewGroup linearLayout = (ViewGroup) cardView.getChildAt(0); //first child of card
        for (int j = 0; j < linearLayout.getChildCount(); j++) { // loop for each child
            View child = linearLayout.getChildAt(j); //get the child
            if (child instanceof Button) {
                Button bookButton = (Button) child; //child match to view button
                bookButton.setOnClickListener(v -> handleRoomSelection(roomTypes[roomIndex], roomPrices[roomIndex]));
                return;
            }
        }
    }

    private void handleRoomSelection(String roomType, double roomPrice) { // when a room is selected
        String userEmail = sharedPreferences.getString("userEmail", null); //get user profile from shared.....

        if (userEmail == null) { //user not logged in
            Intent intent = new Intent(this, LoginActivity.class); // back to login
            intent.putExtra("roomType", roomType);
            intent.putExtra("roomPrice", roomPrice);
            startActivity(intent); // im talking about login here
        } else { // cas where user is logged in
            Intent intent = new Intent(this, WelcomeGuestActivity.class);
            intent.putExtra("roomType", roomType); // selected room type as extra data to intent (transfer data for android app)
            intent.putExtra("roomPrice", roomPrice); // also passing the room price
            startActivity(intent); // im talking about the roomInfo here
        }
    }
}