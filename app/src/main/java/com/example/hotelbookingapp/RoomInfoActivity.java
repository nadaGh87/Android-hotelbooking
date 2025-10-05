package com.example.hotelbookingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RoomInfoActivity extends AppCompatActivity {
    private Calendar myCalendar;
    private EditText edCheckin, edCheckout, edNum;
    private String name, email, phone, address, numberOfPerson;
    private Spinner spinnerType; // for the chambre type
    private String roomType;
    private Button btnPreview;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
// format mte3 date !
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);

        Intent i = getIntent(); //intent that start the activity
        name = i.getStringExtra("name"); //name of user ml intent yjib  lbe9i nafsu
        email = i.getStringExtra("email");
        phone = i.getStringExtra("phone");
        address = i.getStringExtra("address");
        numberOfPerson = i.getStringExtra("numberofperson");


        myCalendar = Calendar.getInstance(); //date actuel
        edCheckin = findViewById(R.id.edCheckin); // yjib m layout :)
        edCheckout = findViewById(R.id.edCheckout);
        spinnerType = findViewById(R.id.spinnerType);
        btnPreview = findViewById(R.id.btnPreview);
        edNum = findViewById(R.id.edNum);

        setupRoomTypeSpinner();//method config spinner

        setupDatePickers(); // method config date

        btnPreview.setOnClickListener(v -> {
            if (validateForm()) {
                roomType = spinnerType.getSelectedItem().toString();
                Intent intent = new Intent(RoomInfoActivity.this, PersonnelInfoActivity.class);
                intent.putExtra("name", name); //yzid lel intent
                intent.putExtra("address", address);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("numberOfPersonne", numberOfPerson);
                intent.putExtra("roomType", roomType);
                intent.putExtra("checkIn", edCheckin.getText().toString());
                intent.putExtra("checkOut", edCheckout.getText().toString());
                intent.putExtra("num", edNum.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setupRoomTypeSpinner() {
        String[] roomTypes = {"Select Room Type", "Standard", "Deluxe", "Suite", "Executive Suite"}; //list of types
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //tasn3 adapter
        spinnerType.setAdapter(adapter); //associer adapter lel spinner
    }

    private void setupDatePickers() { // aka l method l declaritha kbila mte3 date
        DatePickerDialog.OnDateSetListener checkInDate = (view, year, monthOfYear, dayOfMonth) -> { // ecouteur date entree  bsh yhotha l user
            myCalendar.set(Calendar.YEAR, year); // l3am
            myCalendar.set(Calendar.MONTH, monthOfYear);// chhar
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth); // enhar
            updateLabel(myCalendar, edCheckin); // mba3d ma tekhtrhom , 3malt update l check in check out
        };

        edCheckin.setOnClickListener(v -> { // hatha lel clic
            DatePickerDialog dialog = new DatePickerDialog(RoomInfoActivity.this, checkInDate, //ecouteur hatha l khdmtu kbila
                    myCalendar.get(Calendar.YEAR),//3am sne , lbeki kif kif
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // min data hua lyuma :)
            dialog.show();
        });

        DatePickerDialog.OnDateSetListener checkOutDate = (view, year, monthOfYear, dayOfMonth) -> { //date sortie l bch tokhrj
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar, edCheckout);
        };

        edCheckout.setOnClickListener(v -> { //lel clic l date l bsh tokhrj selection
            DatePickerDialog dialog = new DatePickerDialog(RoomInfoActivity.this, checkOutDate,
                    myCalendar.get(Calendar.YEAR),// sne
                    myCalendar.get(Calendar.MONTH),//chhar hatha
                    myCalendar.get(Calendar.DAY_OF_MONTH) // nhayer hatha
            );
            if (!edCheckin.getText().toString().isEmpty()) { //lazm tekhtar date ma lazmsh tkun empty
                try {
                    Date checkIn = dateFormat.parse(edCheckin.getText().toString()); //conversion string l oobjet bnsba lel dat
                    if (checkIn != null) { //verifi
                        dialog.getDatePicker().setMinDate(checkIn.getTime()); //date min heya ddate l dakhlet entréé
                    }
                } catch (Exception e) { // ken fama error
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
            } else {
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //esta3ml date lyum ka minimum
            }
            dialog.show(); //Affichage
        });
    }

    private void updateLabel(Calendar calendar, EditText editText) {
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    private boolean validateForm() { //formatee
        if (edCheckin.getText().toString().isEmpty()) { //vide wale le
            Toast.makeText(this, "Please enter a check-in date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edCheckout.getText().toString().isEmpty()) { //same
            Toast.makeText(this, "Please enter a check-out date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edNum.getText().toString().isEmpty()) { //same
            Toast.makeText(this, "Please enter the number of rooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            int rooms = Integer.parseInt(edNum.getText().toString()); // converti string l number
            if (rooms <= 0) { // numro lbyut lazm yku akbr ml sfer ofc
                Toast.makeText(this, "Number of rooms must be positive", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) { //msg error
            Toast.makeText(this, "Please enter a valid number of rooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spinnerType.getSelectedItem().toString().equals("Select Room Type")) {
            Toast.makeText(this, "Please select a room type", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Date checkIn = dateFormat.parse(edCheckin.getText().toString()); // Convertit la chaîne de date d'entrée en objet Date
            Date checkOut = dateFormat.parse(edCheckout.getText().toString()); // o hathy sortie lazm tkun objet date
            if (checkOut.before(checkIn) || checkOut.equals(checkIn)) { //lehna nverifi f checkout tatl3chi date kbal chekin
                Toast.makeText(this, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
                return false; // error formulaire ghaletttt mayet3desssh
            }
        } catch (Exception e) { //errors mte3 date l ghalta
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return false; // form ghalet
        }
        return true;// sinn shih :) nshlh ykhdm :)
    }
}