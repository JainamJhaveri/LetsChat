package com.example.digvijay.letschat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.AdapterView.*;


public class History extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener {

    ListView lv;
    String ids[], names[], lastmessage[];
    DatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv = (ListView)findViewById(R.id.historyReal);
        db = new DatabaseAdapter(this);
        addEntriestoHistoryList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addEntriestoHistoryList();
    }

    private void addEntriestoHistoryList() {
        Cursor c = db.getHistory();
        ids = new String[c.getCount()];
        names = new String[c.getCount()];
        lastmessage = new String [c.getCount()];

        int i = 0;
        while(c.moveToNext()){
            ids[i] = c.getString(c.getColumnIndex("ID"));
            names[i] = c.getString(c.getColumnIndex("NAME"));
            lastmessage[i] = c.getString(c.getColumnIndex("LASTMESSAGE"));
            i++;
        }
        c.close();

        HistoryAdapter adapter = new HistoryAdapter(this,names,ids,lastmessage);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(History.this, names[position], Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, SingleHistory.class);
        i.putExtra("userID", ids[position]);
        i.putExtra("userName", names[position]);
        startActivity(i);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete");
        dialog.setMessage("Delete all chats with " +names[position] + " ?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteChatMessages(ids[position]);
                onResume();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        dialog.create();
        dialog.show();

        return true;
    }
}
