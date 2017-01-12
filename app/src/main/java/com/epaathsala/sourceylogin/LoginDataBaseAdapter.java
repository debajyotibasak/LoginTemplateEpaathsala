package com.epaathsala.sourceylogin;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static android.R.attr.id;
import static android.R.attr.version;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.N;

/**
 * Created by DROID on 12-01-2017.
 */

public class LoginDataBaseAdapter {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    public LoginDataBaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public LoginDataBaseAdapter open() throws SQLException {
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertData(String name, String specialization, String email, String password) {
        db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.SPECIALIZATION, specialization);
        contentValues.put(DatabaseHelper.EMAIL, email);
        contentValues.put(DatabaseHelper.PASSWORD, password);

        db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public int deleteData(String email) {
        db = databaseHelper.getWritableDatabase();
        db = databaseHelper.getWritableDatabase();
        int numberOFEntriesDeleted = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.EMAIL + " =?",
                new String[]{email});
        return numberOFEntriesDeleted;
    }

    public void updateEntry(String email, String password) {
        db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.EMAIL, email);
        contentValues.put(DatabaseHelper.PASSWORD, password);

        String where = "USERNAME = ?";
        db.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.EMAIL + " =?",
                new String[]{email});
    }

    public String getSingleEntry(String email) {
        db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, DatabaseHelper.EMAIL + " =?",
                new String[]{email}, null, null, null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
        cursor.close();
        return password;
    }


    static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "login.db";
        private static final String TABLE_NAME = "loginTable";
        private static final int DATABASE_VERSION = 4;
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final String SPECIALIZATION = "Specialization";
        private static final String EMAIL = "Email";
        private static final String PASSWORD = "password";
        //look out for syntax errors
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + NAME +
                " VARCHAR(255)," + SPECIALIZATION + " VARCHAR(255)," + EMAIL +
                " VARCHAR(255)," + PASSWORD + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
            }
        }


    }
}