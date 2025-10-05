package com.example.hotelbookingapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hotelBooking_db";
    private static final String TABLE_USERS = "hotel_users";
    private static final String TABLE_RESERVATIONS = "hotel_reservations";
    private static final String COLUMN_USER_ID = "user_id";// hatha l primary key
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    // reserv esm l columns
    private static final String COLUMN_RESERVATION_ID = "reservation_id"; //hatha primary key
    private static final String COLUMN_RESERVATION_USER_EMAIL = "user_email";
    private static final String COLUMN_RESERVATION_NAME = "name";
    private static final String COLUMN_RESERVATION_ADDRESS = "address";
    private static final String COLUMN_RESERVATION_PHONE = "phone";
    private static final String COLUMN_RESERVATION_NUMBER_OF_PEOPLE = "number_of_people";
    private static final String COLUMN_RESERVATION_ROOM_TYPE = "room_type";
    private static final String COLUMN_RESERVATION_CHECK_IN = "check_in";
    private static final String COLUMN_RESERVATION_CHECK_OUT = "check_out";
    private static final String COLUMN_RESERVATION_NUMBER_OF_ROOMS = "number_of_rooms";

    //hathom  sql query to create the users table with the  columns wel  constraints
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PHONE + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT UNIQUE,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_RESERVATION_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
            + COLUMN_RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RESERVATION_USER_EMAIL + " TEXT,"
            + COLUMN_RESERVATION_NAME + " TEXT,"
            + COLUMN_RESERVATION_ADDRESS + " TEXT,"
            + COLUMN_RESERVATION_PHONE + " TEXT,"
            + COLUMN_RESERVATION_NUMBER_OF_PEOPLE + " TEXT,"
            + COLUMN_RESERVATION_ROOM_TYPE + " TEXT,"
            + COLUMN_RESERVATION_CHECK_IN + " TEXT,"
            + COLUMN_RESERVATION_CHECK_OUT + " TEXT,"
            + COLUMN_RESERVATION_NUMBER_OF_ROOMS + " TEXT,"
            + "FOREIGN KEY(" + COLUMN_RESERVATION_USER_EMAIL + ") REFERENCES "
            + TABLE_USERS + "(" + COLUMN_USER_EMAIL + ")" + ")";

    // drop testa3ml  fl  database upgrades
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USERS;
    private String DROP_RESERVATION_TABLE = "DROP TABLE IF EXISTS " + TABLE_RESERVATIONS;

    // constuctor yet7at to define context l app
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //hatha sqlLiteHelper (initializih)
    }

    @Override // l db tsan3t firs
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE); //excute sql bsh yasn3 table
        db.execSQL(CREATE_RESERVATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_RESERVATION_TABLE); //Ale khatr andu cle etrangere
        db.execSQL(DROP_USER_TABLE);
        onCreate(db); // recreate
    }

    // l methods l teb3in user lkol
    public boolean addUser(String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values); // Insert row, o raja3 id mte3ha
        db.close();
        return result != -1;
    }
    // thabet if user bl email heka mawjud ?
    public boolean checkUser(String email) {
        String[] columns = {COLUMN_USER_ID}; //o hajtna ken bl id
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?"; // l where
        String[] selectionArgs = {email}; // valeur l where
        //query lel user table avec condition
        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount(); //a3tini kadeh lkit ml row matching
        cursor.close();
        db.close();
        return cursorCount > 0; //raja3 true ken lkit ala9al wahda
    }
    //lehna check bl email o passsword :nafs principe
    public boolean checkUser(String email, String password) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    // l hashmap fiha l user details
    public HashMap<String, String> getUserDetails(String userEmail) {
        HashMap<String, String> user = new HashMap<>(); //to store user details (create it)
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_USER_NAME,
                COLUMN_USER_PHONE,
                COLUMN_USER_EMAIL
        };

        String selection = COLUMN_USER_EMAIL + " = ?"; //where ?
        String[] selectionArgs = {userEmail}; // value l where
        // l condition :
        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) { // ken user mawjud khuth data mte3u ml cursor
            user.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.put("phone", cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            user.put("email", cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        }

        cursor.close();
        db.close();
        return user;
    }

    public boolean updateUserProfile(String email, String name, String phone, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // hatha bsh nhoto fih data updated

        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_PHONE, phone);

        //update password if a new one is provided
        if (newPassword != null && !newPassword.isEmpty()) {
            values.put(COLUMN_USER_PASSWORD, newPassword);
        }
        String whereClause = COLUMN_USER_EMAIL + " = ?";
        String[] whereArgs = {email};

        int result = db.update(TABLE_USERS, values, whereClause, whereArgs);
        //update o raja3 rows
        db.close();

        return result > 0; // ken ala9al wahda bark sarlha update yarj3 true
    }

    public boolean addReservation(String userEmail, String name, String address, String phone,
                                  String numberOfPeople, String roomType, String checkIn,
                                  String checkOut, String numberOfRooms) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); //lel data
        values.put(COLUMN_RESERVATION_USER_EMAIL, userEmail);
        values.put(COLUMN_RESERVATION_NAME, name);
        values.put(COLUMN_RESERVATION_ADDRESS, address);
        values.put(COLUMN_RESERVATION_PHONE, phone);
        values.put(COLUMN_RESERVATION_NUMBER_OF_PEOPLE, numberOfPeople);
        values.put(COLUMN_RESERVATION_ROOM_TYPE, roomType);
        values.put(COLUMN_RESERVATION_CHECK_IN, checkIn);
        values.put(COLUMN_RESERVATION_CHECK_OUT, checkOut);
        values.put(COLUMN_RESERVATION_NUMBER_OF_ROOMS, numberOfRooms);

        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return result != -1; //
    }

    public ArrayList<HashMap<String, String>> getUserReservations(String userEmail) {
        ArrayList<HashMap<String, String>> reservationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                COLUMN_RESERVATION_ID,
                COLUMN_RESERVATION_NAME,
                COLUMN_RESERVATION_ADDRESS,      // Add missing fields
                COLUMN_RESERVATION_PHONE,       // Add missing fields
                COLUMN_RESERVATION_NUMBER_OF_PEOPLE, // Add missing fields
                COLUMN_RESERVATION_ROOM_TYPE,
                COLUMN_RESERVATION_CHECK_IN,
                COLUMN_RESERVATION_CHECK_OUT,
                COLUMN_RESERVATION_NUMBER_OF_ROOMS
        };

        String selection = COLUMN_RESERVATION_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = db.query(
                TABLE_RESERVATIONS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_RESERVATION_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> reservation = new HashMap<>();
                reservation.put("id", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_ID)));
                reservation.put("name", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_NAME)));
                reservation.put("address", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_ADDRESS))); // Add
                reservation.put("phone", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_PHONE))); // Add
                reservation.put("numberOfPeople", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_NUMBER_OF_PEOPLE))); // Add
                reservation.put("roomType", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_ROOM_TYPE)));
                reservation.put("checkIn", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_CHECK_IN)));
                reservation.put("checkOut", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_CHECK_OUT)));
                reservation.put("numberOfRooms", cursor.getString(cursor.getColumnIndex(COLUMN_RESERVATION_NUMBER_OF_ROOMS)));

                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reservationList;
    }

    // Fix for DatabaseHelper.java - getReservationDetails method

    public HashMap<String, String> getReservationDetails(String reservationId) {
        HashMap<String, String> reservation = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_RESERVATIONS +
                " WHERE " + COLUMN_RESERVATION_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{reservationId});

        if (cursor.moveToFirst()) {
            // Get column indices safely to handle schema changes
            int idIndex = cursor.getColumnIndex(COLUMN_RESERVATION_ID);
            int userEmailIndex = cursor.getColumnIndex(COLUMN_RESERVATION_USER_EMAIL);
            int nameIndex = cursor.getColumnIndex(COLUMN_RESERVATION_NAME);
            int addressIndex = cursor.getColumnIndex(COLUMN_RESERVATION_ADDRESS);
            int phoneIndex = cursor.getColumnIndex(COLUMN_RESERVATION_PHONE);
            int numberOfPeopleIndex = cursor.getColumnIndex(COLUMN_RESERVATION_NUMBER_OF_PEOPLE);
            int roomTypeIndex = cursor.getColumnIndex(COLUMN_RESERVATION_ROOM_TYPE);
            int checkInIndex = cursor.getColumnIndex(COLUMN_RESERVATION_CHECK_IN);
            int checkOutIndex = cursor.getColumnIndex(COLUMN_RESERVATION_CHECK_OUT);
            int numberOfRoomsIndex = cursor.getColumnIndex(COLUMN_RESERVATION_NUMBER_OF_ROOMS);

            // Add values if the column exists
            if (idIndex >= 0) reservation.put("id", cursor.getString(idIndex));
            if (userEmailIndex >= 0) reservation.put("userEmail", cursor.getString(userEmailIndex));
            if (nameIndex >= 0) reservation.put("name", cursor.getString(nameIndex));
            if (addressIndex >= 0) reservation.put("address", cursor.getString(addressIndex));
            if (phoneIndex >= 0) reservation.put("phone", cursor.getString(phoneIndex));

            // Add these debug logs to verify column data
            if (numberOfPeopleIndex >= 0) {
                String value = cursor.getString(numberOfPeopleIndex);
                android.util.Log.d("DatabaseHelper", "numberOfPeople from DB: " + value);
                reservation.put("numberOfPeople", value);
            }
            if (roomTypeIndex >= 0) {
                String value = cursor.getString(roomTypeIndex);
                android.util.Log.d("DatabaseHelper", "roomType from DB: " + value);
                reservation.put("roomType", value);
            }
            if (checkInIndex >= 0) {
                String value = cursor.getString(checkInIndex);
                android.util.Log.d("DatabaseHelper", "checkIn from DB: " + value);
                reservation.put("checkIn", value);
            }
            if (checkOutIndex >= 0) {
                String value = cursor.getString(checkOutIndex);
                android.util.Log.d("DatabaseHelper", "checkOut from DB: " + value);
                reservation.put("checkOut", value);
            }
            if (numberOfRoomsIndex >= 0) {
                String value = cursor.getString(numberOfRoomsIndex);
                android.util.Log.d("DatabaseHelper", "numberOfRooms from DB: " + value);
                reservation.put("numberOfRooms", value);
            }

            // Debug log all keys and values
            for (String key : reservation.keySet()) {
                android.util.Log.d("ReservationDetails", key + ": " + reservation.get(key));
            }
        }

        cursor.close();
        db.close();
        return reservation;
    }

    public boolean updateReservation(String reservationId, String name, String address, String phone,
                                     String numberOfPeople, String roomType, String checkIn,
                                     String checkOut, String numberOfRooms) {
        SQLiteDatabase db = this.getWritableDatabase();//writable khatr bsh tektb
        ContentValues values = new ContentValues(); // bsh taaml fiha store lel values

        values.put(COLUMN_RESERVATION_NAME, name);
        values.put(COLUMN_RESERVATION_ADDRESS, address);
        values.put(COLUMN_RESERVATION_PHONE, phone);
        values.put(COLUMN_RESERVATION_NUMBER_OF_PEOPLE, numberOfPeople);
        values.put(COLUMN_RESERVATION_ROOM_TYPE, roomType);
        values.put(COLUMN_RESERVATION_CHECK_IN, checkIn);
        values.put(COLUMN_RESERVATION_CHECK_OUT, checkOut);
        values.put(COLUMN_RESERVATION_NUMBER_OF_ROOMS, numberOfRooms);

        String whereClause = COLUMN_RESERVATION_ID + " = ?";
        String[] whereArgs = {reservationId};

        int result = db.update(TABLE_RESERVATIONS, values, whereClause, whereArgs);
        db.close();

        return result > 0;
    }
    //hatha f delete bl id : tfasakh rese :
    public boolean deleteReservation(String reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_RESERVATION_ID + " = ?"; // where
        String[] whereArgs = {reservationId}; //value lel where

        int result = db.delete(TABLE_RESERVATIONS, whereClause, whereArgs);
        db.close();

        return result > 0; //true if ala9al andk res bsh tetfasakh
    }
}