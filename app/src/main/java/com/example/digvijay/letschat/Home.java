package com.example.digvijay.letschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Socket;
import com.github.nkzawa.engineio.*;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;


public class Home extends AppCompatActivity {
    com.github.nkzawa.socketio.client.Socket mSocket;
    TextView tv;
    Context context;
    Activity activity;
    ListView history;
    Button refresh;
    String names[];
    View waiting_overlay;
    String ids[];
    String[] str = {"Aashka", "Anjali", "Jainam", "Priyanshi", "Akash"};
    int[] img = {R.drawable.p1, R.drawable.p2, R.drawable.p4, R.drawable.p3, R.drawable.p5, R.drawable.p6};
    CustomListAdapter adapter;
    View waitingPanel;
    FloatingActionButton fab;

    String id,username;

    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        //tv = (TextView) findViewById(R.id.userName);
        //if (count == 0) {
          //  tv.setText(getIntent().getExtras().getString(LoginFragment.USERNAME));
        SharedPreferences sharedPref = getSharedPreferences("data", MODE_PRIVATE);

            id = sharedPref.getString("id","No Id Found");
            username = sharedPref.getString("name","No Name Found");

        waiting_overlay = (View)findViewById(R.id.waiting_overlay);
        refresh = (Button)findViewById(R.id.refresh);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //waitingPanel = ((ViewStub) findViewById(R.id.stub_import)).inflate();
                    waiting_overlay.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.INVISIBLE);
                    JSONObject ob = new JSONObject();
                    ob.put("id", id);
                    mSocket.emit("ready", ob);
                    mSocket.on("newChat",newChatListener);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context,"Exception thrown",Toast.LENGTH_LONG).show();
                }
            }
        });

        history = (ListView) findViewById(R.id.history);
        adapter = new CustomListAdapter(this, str, img, getResources());
        history.setAdapter(adapter);

        establishConnection();




        Log.e("   >>>>>>>>>>    ", "HomeOnCreate");
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
                Intent profileIntent = new Intent(this, Profile.class);
                startActivity(profileIntent);

                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                return true;

        }
        return false;
    }

    void establishConnection() {
        try {
            mSocket = IO.socket("http://10.0.0.8:3000");
            SocketHandler.setSocket(mSocket);
            mSocket.connect();
            mSocket.on("getUsers", displayOnlineUsers);
            JSONObject user = new JSONObject();
            user.put("name",username);
            user.put("id",id);
            mSocket.emit("register",user);

        } catch (Exception e) {
            Log.i("fdf", e.toString());

        }
    }

    private Emitter.Listener displayOnlineUsers = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONArray tempNames = data.getJSONArray("names");
                        JSONArray tempIds = data.getJSONArray("id");
                        names = new String[tempIds.length()];
                        ids = new String[tempIds.length()];
                        for (int i = 0; i < tempIds.length(); i++) {
                            names[i] = tempNames.getString(i);
                            ids[i] = tempIds.getString(i);
                            adapter = new CustomListAdapter(activity, names, img, getResources());
                            history.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };


    private Emitter.Listener newChatListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject ob = (JSONObject)args[0];
                    Intent i = new Intent(context,ChatScreen.class);
                    String name="";
                    String id="";
                    try {
                        id = ob.getString("id");
                        name =ob.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("id",id);
                    i.putExtra("name",name);

                    startActivity(i);
                }
            });
        }
    };



    public void refresh(View v){
        mSocket.emit("display");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject user = new JSONObject();
        try {
            user.put("name",username);
            user.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       mSocket.emit("unregister", user);
        mSocket.disconnect();
        mSocket.off("newChat", newChatListener);
        mSocket.off("getUsers", displayOnlineUsers);
    }

    public void cancelWaiting(View view) {
        waiting_overlay.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
        try {
            JSONObject ob = new JSONObject();
            ob.put("id", id);
            mSocket.emit("notReady", ob);
            mSocket.off("newChat", newChatListener);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"Exception thrown",Toast.LENGTH_LONG).show();
        }

    }
}
