package com.example.digvijay.letschat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Digvijay on 29-02-2016.
 */
public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;

    DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }


    public long insertInTable_1(String email, String message, int sentBy, String time) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL_ID, email);
        values.put(DatabaseHelper.MESSAGE_1, message);
        values.put(DatabaseHelper.SENT_BY_1, sentBy);
        values.put(DatabaseHelper.TIME_1, time);
        long success = db.insert(DatabaseHelper.TABLE_NAME_1, null, values);
        return success;
    }

    public ArrayList<ChatMessage> getMessages(String id){
        Cursor c = db.query(DatabaseHelper.TABLE_NAME_1,new String[]{DatabaseHelper.MESSAGE_1,DatabaseHelper.SENT_BY_1,DatabaseHelper.TIME_1}
                ,DatabaseHelper.EMAIL_ID+ " = " + id,null,null,null,null);

        ArrayList<ChatMessage> list = new ArrayList<>(c.getCount());

        while(c.moveToNext()){
            String message = c.getString(c.getColumnIndex(DatabaseHelper.MESSAGE_1));
            int sentBy = c.getInt(c.getColumnIndex(DatabaseHelper.SENT_BY_1));
            if(sentBy == ChatScreen.ME)
                list.add(new ChatMessage(message,true));
            if(sentBy == ChatScreen.OTHER)
                list.add(new ChatMessage(message,false));

        }
        return list;
    }

    public void insertName(String id, String name){
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("name",name);

        long x = db.insert("user_names",null,cv);
        Log.e(" >> ","inserted "+x);
    }

    public String getName(String id){
        Cursor c =db.query("user_names",new String[]{"name"},"id = "+id,null,null,null,null);
        String name = null;
        while (c.moveToNext()){
            name = c.getString(0);
        }
        return name;

    }

    public Cursor getHistory(){
        return db.query("user_names",new String[]{"name","id"},null,null,null,null,null);

    }






    static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "letschat_database";
        private static final String TABLE_NAME_1 = "CHATS";
        private static final int DATABASE_VERSION = 3;
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
                db.execSQL("create table user_names(id text primary key,name text);");
            } catch (Exception e) {
                Log.e("SQL Exception ", "see below the stack trace");
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_1);
            db.execSQL("drop table if exists user_names");
            onCreate(db);
        }
    }

}
