package com.example.hotelbookingapp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
public class EditBookingActivity extends AppCompatActivity {

    private EditText edName, edAddress, edPhone, edNumberOfPeople;
    private Spinner spinnerRoomType;
    private EditText edCheckIn, edCheckOut, edNumberOfRooms;
    private Button btnUpdate, btnCancel;
    private DatabaseHelper databaseHelper;
    private String reservationId;
    private HashMap<String, String> reservationDetails;
    private Calendar myCalendar;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override //l activ bdet
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        // db ; lel oper
        databaseHelper = new DatabaseHelper(this);

        // njib id ml intent
        reservationId = getIntent().getStringExtra("reservationId");
        if (reservationId == null || reservationId.isEmpty()) {
            Toast.makeText(this, "Invalid reservation", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();

        // hathy lel dates
        myCalendar = Calendar.getInstance();

        //  chargi  reservation details
        loadReservationDetails();

        // room type spinner l fih types mte3 rooms
        setupRoomTypeSpinner();

        // date pickers : l fih dates
        setupDatePickers();

        //  click listeners boutons
        setupButtonListeners();
    }

    private void initializeViews() {
        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPhone = findViewById(R.id.edPhone);
        edNumberOfPeople = findViewById(R.id.edNumberOfPeople);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        edCheckIn = findViewById(R.id.edCheckIn);
        edCheckOut = findViewById(R.id.edCheckOut);
        edNumberOfRooms = findViewById(R.id.edNumberOfRooms);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadReservationDetails() {
        reservationDetails = databaseHelper.getReservationDetails(reservationId);

        if (reservationDetails.isEmpty()) {
            Toast.makeText(this, "Reservation not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // values eli fl  fields
        edName.setText(getValueOrDefault(reservationDetails, "name", ""));
        String address = getValueOrDefault(reservationDetails, "address", "");
        if (address.startsWith("somewhere ")) {
            address = address.substring("somewhere ".length());
        }
        edAddress.setText(address);

        edPhone.setText(getValueOrDefault(reservationDetails, "phone", ""));
        edNumberOfPeople.setText(getValueOrDefault(reservationDetails, "numberOfPeople", ""));
        edCheckIn.setText(getValueOrDefault(reservationDetails, "checkIn", ""));
        edCheckOut.setText(getValueOrDefault(reservationDetails, "checkOut", ""));
        edNumberOfRooms.setText(getValueOrDefault(reservationDetails, "numberOfRooms", ""));
    }
     //Debug
    private String getValueOrDefault(HashMap<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        return (value != null) ? value : defaultValue;
    }

    private void setupRoomTypeSpinner() {
        String[] roomTypes = {"Standard", "Deluxe", "Suite", "Executive Suite"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoomType.setAdapter(adapter);

        //  room type selected :
        String roomType = getValueOrDefault(reservationDetails, "roomType", "");
        for (int i = 0; i < roomTypes.length; i++) {
            if (roomTypes[i].equals(roomType)) {
                spinnerRoomType.setSelection(i);
                break;
            }
        }
    }

    private void setupDatePickers() {
        // date picker ceckin ,date l bsh tekhtaru
        DatePickerDialog.OnDateSetListener checkInDate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar, edCheckIn);
        };

        edCheckIn.setOnClickListener(v -> {
            //instance
            DatePickerDialog dialog = new DatePickerDialog(EditBookingActivity.this, checkInDate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            //min date lyuma
            dialog.show();
        });

         // hatha lel checkout , nafs principe

        DatePickerDialog.OnDateSetListener checkOutDate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar, edCheckOut);
        };

        edCheckOut.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(EditBookingActivity.this, checkOutDate,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            if (!edCheckIn.getText().toString().isEmpty()) {
                try {
                    //text en objet converssion
                    Date checkIn = dateFormat.parse(edCheckIn.getText().toString());
                    if (checkIn != null) {
                        dialog.getDatePicker().setMinDate(checkIn.getTime());
                    }
                } catch (Exception e) {
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                }
            } else {
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
            dialog.show();
        });
    }

    private void updateLabel(Calendar calendar, EditText editText) {
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupButtonListeners() {
        btnUpdate.setOnClickListener(v -> {
            if (validateForm()) {
                updateReservation();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private boolean validateForm() {
        //condition bsh nchufhm kol  msh ferghin
        if (edName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edNumberOfPeople.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the number of people", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edCheckIn.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a check-in date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edCheckOut.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a check-out date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edNumberOfRooms.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the number of rooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            int rooms = Integer.parseInt(edNumberOfRooms.getText().toString());
            if (rooms <= 0) {
                Toast.makeText(this, "Number of rooms must be positive", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number of rooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            Date checkIn = dateFormat.parse(edCheckIn.getText().toString());
            Date checkOut = dateFormat.parse(edCheckOut.getText().toString());
            //text vers un objet date
            if (checkOut.before(checkIn) || checkOut.equals(checkIn)) {
                //Checkout msh 9bal checkin
                Toast.makeText(this, "Check-out date must be after check-in date", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateReservation() {
        boolean result = databaseHelper.updateReservation(
                reservationId,
                edName.getText().toString(),
                edAddress.getText().toString(),
                edPhone.getText().toString(),
                edNumberOfPeople.getText().toString(),
                spinnerRoomType.getSelectedItem().toString(),
                edCheckIn.getText().toString(),
                edCheckOut.getText().toString(),
                edNumberOfRooms.getText().toString()
        );

        if (result) {
            Toast.makeText(this, "Reservation updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditBookingActivity.this, ViewBookingsActivity.class);
            intent.putExtra("reservationId", reservationId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
        }
    }
}