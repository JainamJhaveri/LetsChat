package com.example.digvijay.letschat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;

public class Home extends AppCompatActivity {

    TextView tv;

    ListView history;
    String[] str={"Aashka","Anjali","Jainam","Priyanshi","Akash","Preet"};
    int[] img={R.drawable.p1,R.drawable.p2, R.drawable.p4,R.drawable.p3,R.drawable.p5,R.drawable.p6};
    CustomListAdapter adapter;

    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        tv = (TextView) findViewById(R.id.userName);
        if(count==0)
        tv.setText(getIntent().getExtras().getString(LoginFragment.USERNAME));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        history = (ListView)findViewById(R.id.history);
        adapter = new CustomListAdapter(this,str,img,getResources());
        history.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent profileIntent = new Intent(this,Profile.class);
                startActivity(profileIntent);

                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                return true;

        }
        return false;
    }



}
