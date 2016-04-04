package com.example.digvijay.letschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.digvijay.letschat.MyPreferences.*;


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
    String[] str = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6"};
    int[] img = {R.drawable.p4, R.drawable.p4, R.drawable.p4, R.drawable.p4, R.drawable.p4, R.drawable.p4};
    CustomListAdapter adapter;
    FloatingActionButton fab;

    String id, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        username = getUsername(this);
        id = getId(this);

        tv = (TextView) findViewById(R.id.userName);
        tv.setText(username);


        waiting_overlay = findViewById(R.id.waiting_overlay);
        refresh = (Button) findViewById(R.id.refresh);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    waiting_overlay.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.INVISIBLE);
                    JSONObject ob = new JSONObject();
                    ob.put("id", id);
                    mSocket.emit("ready", ob);
                    mSocket.on("newChat", newChatListener);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Exception thrown", Toast.LENGTH_LONG).show();
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

            case R.id.logout:
                Toast.makeText(this.getApplicationContext(), "Logout clicked !", Toast.LENGTH_SHORT).show();
                if (!isNetworkAvailable(this)) {
                    Toast.makeText(Home.this, "No internet connected", Toast.LENGTH_SHORT).show();
                    return true;
                }
                clearPreferences(this);
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();            // for the fb button to show "log in with fb" after logout
                this.finish();
                return true;
        }
        return false;
    }

    void establishConnection() {

        if (isNetworkAvailable(this)) {
            try {
                mSocket = IO.socket("http://letschatserver.herokuapp.com/");
//                mSocket = IO.socket("http://letschatmodulusserver-61179.onmodulus.net/");

                System.out.println("here: " + mSocket.toString());
                SocketHandler.setSocket(mSocket);
                mSocket.connect();
                mSocket.on("getUsers", displayOnlineUsers);
                JSONObject user = new JSONObject();
                user.put("name", username);
                user.put("id", id);
                mSocket.emit("register", user);

            } catch (Exception e) {
                Log.i("fdf", e.toString());

            }
        } else {
            Toast.makeText(Home.this, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }

    private Emitter.Listener displayOnlineUsers = new Emitter.Listener() {

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
                    JSONObject ob = (JSONObject) args[0];
                    Intent i = new Intent(context, ChatScreen.class);
                    String name = "";
                    String id = "";
                    try {
                        id = ob.getString("id");
                        name = ob.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("id", id);
                    i.putExtra("name", name);

                    startActivity(i);
                }
            });
        }
    };


    public void refresh(View v) {
        if (isNetworkAvailable(this)) {
            mSocket.emit("display");
        } else {
            Toast.makeText(Home.this, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject user = new JSONObject();
        try {
            user.put("name", username);
            user.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isNetworkAvailable(this)) {
            mSocket.emit("unregister", user);           // exception generated when no internet
            mSocket.disconnect();
            mSocket.off("newChat", newChatListener);
            mSocket.off("getUsers", displayOnlineUsers);
        }
        else{
            Log.e(">>>>>", "No Internet connection: OnDestroy() called");
        }
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Exception thrown", Toast.LENGTH_LONG).show();
        }

    }




    @Override
    public void onBackPressed() {
        Log.e(" >>>> Home: ", " Back Pressed");
        this.finish();
    }

}
