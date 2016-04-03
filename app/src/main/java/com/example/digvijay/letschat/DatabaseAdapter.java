package com.example.digvijay.letschat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Digvijay on 29-02-2016.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;

    DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }


    public long insertInTable_1(String email, String message, int sentBy, String time) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL_ID, email);
        values.put(DatabaseHelper.MESSAGE_1, message);
        values.put(DatabaseHelper.SENT_BY_1, sentBy);
        values.put(DatabaseHelper.TIME_1, time);
        long success = db.insert(DatabaseHelper.TABLE_NAME_1, null, values);
        return success;
    }


    static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "letschat_database";
        private static final String TABLE_NAME_1 = "CHATS";
        private static final int DATABASE_VERSION = 1;
        private static final String ID_1 = "_id";
        private static final String EMAIL_ID = "email_id";
        private static final String MESSAGE_1 = "message";
        private static final String SENT_BY_1 = "sent_by";
        private static final String TIME_1 = "time";
        private static final String CREATE_TABLE_1 = "CREATE TABLE " + TABLE_NAME_1 + " (" + ID_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + EMAIL_ID + " TEXT, " + MESSAGE_1 + " TEXT, " + SENT_BY_1 + " INTEGER, " + TIME_1 + " TEXT);";
        private static final String DROP_TABLE_1 = "DROP TABLE IF EXISTS " + TABLE_NAME_1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override


        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_1);
            } catch (Exception e) {
                Log.e("SQL Exception ", "see below the stack trace");
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_1);
            onCreate(db);
        }
    }

}
