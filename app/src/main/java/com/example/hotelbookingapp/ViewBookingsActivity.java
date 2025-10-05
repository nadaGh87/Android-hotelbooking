package com.example.hotelbookingapp;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class ViewBookingsActivity extends AppCompatActivity {

    private TextView tvName, tvAddress, tvPhone, tvNumberOfPeople, tvRoomType;
    private TextView tvCheckIn, tvCheckOut, tvNumberOfRooms;
    private Button btnEdit, btnDelete, btnBack;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private String userEmail;
    private String reservationId;
    private HashMap<String, String> reservationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        // njib data ml session (mte3 l user l connect )
        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        userEmail = sharedPreferences.getString("userEmail", "");
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ml intent njib fl user id
        reservationId = getIntent().getStringExtra("reservationId");
        if (reservationId == null || reservationId.isEmpty()) {
            Toast.makeText(this, "Invalid reservation", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewBookingsActivity.this, BookingsListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // view
        initializeViews();

        // chargiiii details reserv
        loadReservationDetails();

        // button click listeners
        setUpButtonListeners();
    }

    private void initializeViews() {
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvNumberOfPeople = findViewById(R.id.tvNumberOfPeople);
        tvRoomType = findViewById(R.id.tvRoomType);
        tvCheckIn = findViewById(R.id.tvCheckIn);
        tvCheckOut = findViewById(R.id.tvCheckOut);
        tvNumberOfRooms = findViewById(R.id.tvNumberOfRooms);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
    }



    private void loadReservationDetails() {
        reservationDetails = databaseHelper.getReservationDetails(reservationId);

        if (reservationDetails.isEmpty()) {
            Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewBookingsActivity.this, BookingsListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // nchcuf fl user andu acces wale le ,reservation mte3u ou nn
        String resUserEmail = reservationDetails.get("userEmail");
        if (!userEmail.equals(resUserEmail)) {
            Toast.makeText(this, "Unauthorized access", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewBookingsActivity.this, BookingsListActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // debug
        for (String key : reservationDetails.keySet()) {
            android.util.Log.d("ViewBooking", key + ": " + reservationDetails.get(key));
        }

        //reservation details
        tvName.setText(getValueOrDefault(reservationDetails, "name", "N/A"));
        String address = getValueOrDefault(reservationDetails, "address", "N/A");
        if (address.startsWith("somewhere ")) {
            address = address.substring("somewhere ".length());
        }
        tvAddress.setText(address);

        tvPhone.setText(getValueOrDefault(reservationDetails, "phone", "N/A"));

        // debug
        android.util.Log.d("ViewBooking", "Number of People: " + reservationDetails.get("numberOfPeople"));
        android.util.Log.d("ViewBooking", "Room Type: " + reservationDetails.get("roomType"));
        android.util.Log.d("ViewBooking", "Check In: " + reservationDetails.get("checkIn"));
        android.util.Log.d("ViewBooking", "Check Out: " + reservationDetails.get("checkOut"));
        android.util.Log.d("ViewBooking", "Number of Rooms: " + reservationDetails.get("numberOfRooms"));

        // db
        tvNumberOfPeople.setText(getValueOrDefault(reservationDetails, "numberOfPeople", "N/A"));
        tvRoomType.setText(getValueOrDefault(reservationDetails, "roomType", "N/A"));
        tvCheckIn.setText(getValueOrDefault(reservationDetails, "checkIn", "N/A"));
        tvCheckOut.setText(getValueOrDefault(reservationDetails, "checkOut", "N/A"));
        tvNumberOfRooms.setText(getValueOrDefault(reservationDetails, "numberOfRooms", "N/A"));
    }

    // debuggg
    private String getValueOrDefault(HashMap<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        android.util.Log.d("ViewBooking", "Getting value for key: " + key + ", value: " + value);

        //  handle null values and empty strings
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }

        //click listeners lel boutons :
    private void setUpButtonListeners() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ViewBookingsActivity.this, EditBookingActivity.class);
            intent.putExtra("reservationId", reservationId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(ViewBookingsActivity.this)
                    .setTitle("Delete Reservation")
                    .setMessage("Are you sure you want to delete ur  reservation?")
                    .setPositiveButton("Delete", (dialog, which) -> deleteReservation())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ViewBookingsActivity.this, BookingsListActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void deleteReservation() {
        boolean result = databaseHelper.deleteReservation(reservationId);

        if (result) {
            Toast.makeText(this, "Reservation deleted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewBookingsActivity.this, BookingsListActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to delete reservation", Toast.LENGTH_SHORT).show();
        }
    }

    // ken saru edit , ysir refrech bl override  :
    @Override
    protected void onResume() {
        super.onResume();
        if (reservationId != null && !reservationId.isEmpty()) {
            loadReservationDetails();
        }
    }
}