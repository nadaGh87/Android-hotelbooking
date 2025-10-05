package com.example.hotelbookingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PersonnelInfoActivity extends AppCompatActivity {

    private EditText edName, edAddress, edPhone, edEmail, edNumberOfPerson;
    private Button btnNext;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper; //lel operation database

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //activity tsan3t
        super.onCreate(savedInstanceState);//meth classe parent onCreate kima 9rineha 
        setContentView(R.layout.activity_personnelinfo);

     //sharedPref l khdmtu hua mte3 stockage data 
        sharedPreferences = getSharedPreferences("HotelBookingPrefs", Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);  //instance ml bd


        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);
        edNumberOfPerson = findViewById(R.id.edNumberOfPerson);
        btnNext = findViewById(R.id.btnNext);

        String userEmail = sharedPreferences.getString("userEmail", ""); // lehna njib fl email ml db
        if (!TextUtils.isEmpty(userEmail)) { // nthabet in case yatl3 fergh
            edEmail.setText(userEmail);  // l valeur l tal3t tet7at lehna
        }

        btnNext.setOnClickListener(new View.OnClickListener() { // ecouteur lel  clic al bouton next
            @Override
            public void onClick(View v) {
                if (validateForm()) { // lkolhom l valeu mte3 text Fields (l ktebhom user  )
                    String name = edName.getText().toString().trim();
                    String address = edAddress.getText().toString().trim();
                    String phone = edPhone.getText().toString().trim();
                    String email = edEmail.getText().toString().trim();
                    String numberOfPerson = edNumberOfPerson.getText().toString().trim();
                    Intent intent = new Intent(PersonnelInfoActivity.this, FinalActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("numberofperson", numberOfPerson);
                    //lkolhom ya3mlu add  lel  info lel  intention pour  transmettre lel activity l ba3dha
                    startActivity(intent); //  demarre l activity l baadha
                }
            }
        });
    }

    private boolean validateForm() { // pour valider les champs l t7atou kol
        boolean valid = true; //default true

        // valid lel esm
        String name = edName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            edName.setError("Name is required");
            valid = false;
        } else {
            edName.setError(null); // error
        }

        // address
        String address = edAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            edAddress.setError("Address is required");
            valid = false;
        } else {
            edAddress.setError(null);
        }

        // num
        String phone = edPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Phone number is required");
            valid = false;
        } else if (phone.length() < 10) {// el condition
            edPhone.setError("Please enter a valid phone number");
            valid = false;
        } else {
            edPhone.setError(null);
        }

        //   lel email
        String email = edEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //l format  valid ou nn bel  @
            edEmail.setError("Please enter a valid email address");
            valid = false;
        } else {
            edEmail.setError(null);
        }

        // num pers
        String numberOfPerson = edNumberOfPerson.getText().toString().trim();
        if (TextUtils.isEmpty(numberOfPerson)) {
            edNumberOfPerson.setError("Number of persons is required");
            valid = false;
        } else {
            try {
                int num = Integer.parseInt(numberOfPerson);
                if (num <= 0) { // malazmsh negative
                    edNumberOfPerson.setError("Number of persons must be greater than 0");
                    valid = false;
                } else {
                    edNumberOfPerson.setError(null);
                }
            } catch (NumberFormatException e) {
                edNumberOfPerson.setError("Please enter a valid number");
                valid = false;
            }
        }

        return valid; // valid formulaire
    }
}