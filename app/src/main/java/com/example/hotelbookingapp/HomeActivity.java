package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.HashMap;


public class HomeActivity extends AppCompatActivity {
    private TextView tvWelcomeUser;
    private CardView cardBookRoom, cardViewBookings, cardProfile; // l card l fihom mte3 lhome 3 boutons
    private SharedPreferences sharedPreferences;// l fihom data email .....
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        cardBookRoom = findViewById(R.id.cardBookRoom);
        cardViewBookings = findViewById(R.id.cardViewBookings);
        cardProfile = findViewById(R.id.cardProfile);

        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        //pour acceder lel data l stocker
        databaseHelper = new DatabaseHelper(this); // instance

        String userEmail = sharedPreferences.getString("userEmail", "");

        if (userEmail.isEmpty()) {
            redirectToLogin();
            // verif  l'email vide (user mch aamel cnct yarj3 lel login )
            return;
        }

        // tawa  recuperer  détails  user ml db
        HashMap<String, String> userDetails = databaseHelper.getUserDetails(userEmail);
        if (userDetails != null) { //msh null yetaada
            // l hash map ta3ml stockage  des données sous forme de paires clé-valeur
            // o taatik acces rapide
            tvWelcomeUser.setText("Welcome Guest , " + userDetails.get("name"));
        }

        cardBookRoom.setOnClickListener(v -> //khedmet l book
                startActivity(new Intent(HomeActivity.this, MainActivity.class)));
        //y3adik lel main act
        cardViewBookings.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, BookingsListActivity.class)));
        // yaadik lel list booking
        cardProfile.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, activity_profile.class)));
        // yaadik l profilek
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class)
                // lel login
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        // notion flag  : ta3ml effacer la pile acttivi  bch user  ne puisse pas
        // revenir lel home   avec le clic sur le bouton retour après s'être déconnecté
        finish(); // wfet l activi , ressource ywaliw liberer l haja okhra
    }
}