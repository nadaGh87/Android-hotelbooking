package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class BookingsListActivity extends AppCompatActivity {

    private ListView lvBookings; // lista l fiha reser
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private String userEmail;
    private ArrayList<HashMap<String, String>> reservationList;
    // Liste l fiha  data complètes mte3 réservations sous forme de HashMaps
    private ArrayList<String> displayList; // list l bsh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_list);

        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        // l user l conn
        userEmail = sharedPreferences.getString("userEmail", "");
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // lista m3a l xml l teb3ha
        lvBookings = findViewById(R.id.lvBookings);

        // nchargi reservation l andi kol
        loadReservations();

        // list item  click listener
        lvBookings.setOnItemClickListener((parent, view, position, id) -> {
            //behi lazmk tjibha ml pos l aaml aleha clic
            HashMap<String, String> reservation = reservationList.get(position);
            // baad njib l id
            String reservationId = reservation.get("id");
            //ken c bon jebt list netaada l page l najm nshuf fiha lista

            Intent intent = new Intent(BookingsListActivity.this, ViewBookingsActivity.class);
            intent.putExtra("reservationId", reservationId);
            startActivity(intent);
        });
    }

    private void loadReservations() {
        reservationList = databaseHelper.getUserReservations(userEmail);
        displayList = new ArrayList<>();

        if (reservationList.isEmpty()) {
            Toast.makeText(this, "No reservations found", Toast.LENGTH_SHORT).show();
            displayList.add("No reservations available");
            lvBookings.setEnabled(false);
        } else {
            //tawa lazm njib réservation lkol
            for (HashMap<String, String> reservation : reservationList) {
                String displayText = "Reservation ID: " + reservation.get("id") +
                        "\n Name: " + reservation.get("name") +
                        "\n Room Type: " + reservation.get("roomType") +
                        "\n Check-In: " + reservation.get("checkIn");
                displayList.add(displayText);
            }
        }
        //  l adapter  bch naaml lien  bin data o  listview

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, displayList); // layout prédéf simple_list_item_1 mte3 android

        lvBookings.setAdapter(adapter); //association ma bin adapter o lista
    }
}