package com.example.digvijay.letschat;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;

import java.util.ArrayList;


public class History extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView)findViewById(R.id.historyReal);
        DatabaseAdapter db = new DatabaseAdapter(this);

        Cursor c = db.getHistory();
        String id[] = new String[c.getCount()];
        String names[] = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            id[i] = c.getString(c.getColumnIndex("id"));
            names[i] = c.getString(c.getColumnIndex("name"));
            i++;
        }
        HistoryAdapter adapter = new HistoryAdapter(this,names,id,names);
        lv.setAdapter(adapter);


    }





}
