package com.example.digvijay.letschat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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

import com.facebook.AccessToken;
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
    TextView tv, waitingText;
    Context context;
    Activity activity;
    ListView history;
    Button refresh, cancelWaiting;
    View waiting_overlay;
    String ids[], names[];
    double latitudes[], longitudes[];
    float distances[];

    CustomListAdapter adapter;
    FloatingActionButton fab;
    LocationFetcher locationFetcher;
    Location location;
    String id, username;
    int status;

    private final int NO_CONNECTION = 1;
    private final int ACTIVE_CONNECTION = 2;

    @Override
    protected void onStart() {

        locationFetcher.onStart();
        super.onStart();
    }

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


        locationFetcher = new LocationFetcher(this);
        locationFetcher.onCreate();
        if (locationFetcher.client == null)
            Log.e("clent", "....null");
        else
            Log.e("client", "....okay");

        history = (ListView) findViewById(R.id.history);
        waitingText = (TextView) findViewById(R.id.waitingText);
        cancelWaiting = (Button) findViewById(R.id.cancelWaiting);
        waiting_overlay = findViewById(R.id.waiting_overlay);
        refresh = (Button) findViewById(R.id.refresh);
        fab = (FloatingActionButton) findViewById(R.id.fab);

//        Log.e(" >> ",AccessToken.getCurrentAccessToken().toString());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    waiting_overlay.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                    refresh.setVisibility(View.INVISIBLE);
                    cancelWaiting.setText("Cancel");
                    waitingText.setText("Waiting for a user...");
                    JSONObject ob = new JSONObject();
                    ob.put("id", id);
                    mSocket.emit("ready", ob);
                    mSocket.on("newChat", newChatListener);
                    Toast.makeText(context, locationFetcher.getLocation().getLatitude() + "", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Exception thrown", Toast.LENGTH_LONG).show();
                }
            }
        });


        //adapter = new CustomListAdapter(this, str, img,latitudes,longitudes,distances, getResources());
        //history.setAdapter(adapter);

        location = locationFetcher.getLocation();
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

            case R.id.historyMenu:
                startActivity(new Intent(this, History.class));
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

        if (isNetworkAvailable(this) && location != null) {
            try {
                status = ACTIVE_CONNECTION;
                //mSocket = IO.socket("http://letschatserver.herokuapp.com/");
                mSocket = IO.socket("http://chat10101.herokuapp.com/");

                //mSocket = IO.socket("http://10.0.0.13:3000");

                location = locationFetcher.getLocation();
                System.out.println("here: " + mSocket.toString());
                SocketHandler.setSocket(mSocket);
                mSocket.connect();
                mSocket.on("getUsers", displayOnlineUsers);
                JSONObject user = new JSONObject();
                user.put("name", username);
                user.put("id", id);
                user.put("latitude", location.getLatitude());
                user.put("longitude", location.getLongitude());

                MyPreferences preferences = new MyPreferences();
                String status = preferences.getStatus(context);
                Log.e(" >>>> status: ",status);
                user.put("status",status);

                mSocket.emit("register", user);


            } catch (Exception e) {
                Toast.makeText(context, "Exception thrown", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        } else {
            Toast.makeText(Home.this, "Please check your connectivity", Toast.LENGTH_SHORT).show();
            status = NO_CONNECTION;
            waiting_overlay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
            refresh.setVisibility(View.INVISIBLE);
            cancelWaiting.setText("Retry");
            if (isNetworkAvailable(this))
                waitingText.setText("Turn Location On ");
            else
                waitingText.setText("No Connection :(");
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
                        JSONArray tempLatitudes = data.getJSONArray("latitudes");
                        JSONArray tempLongitudes = data.getJSONArray("longitudes");
                        int len = tempIds.length();
                        names = new String[len];
                        ids = new String[len];
                        longitudes = new double[len];
                        latitudes = new double[len];
                        distances = new float[len];
                        double newDistance[] = new double[len];
                        float results[] = new float[4];
                        for (int i = 0; i < tempIds.length(); i++) {
                            names[i] = tempNames.getString(i);
                            ids[i] = tempIds.getString(i);
                            latitudes[i] = tempLatitudes.getDouble(i);
                            longitudes[i] = tempLongitudes.getDouble(i);
                            Location.distanceBetween(location.getLatitude(), location.getLongitude(), latitudes[i], longitudes[i], results);
                            distances[i] = results[0];
                            newDistance[i] = distanceTo(location.getLatitude(), location.getLongitude(), latitudes[i], longitudes[i]);

                        }
                        adapter = new CustomListAdapter(activity, names, latitudes, longitudes, distances, getResources(), ids);
                        history.setAdapter(adapter);
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
                    String status = "";

                    try {
                        id = ob.getString("id");
                        name = ob.getString("name");
                        status = ob.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    i.putExtra("id", id);
                    i.putExtra("name", name);
                    i.putExtra("status",status);

                    startActivity(i);
                    waiting_overlay.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    refresh.setVisibility(View.VISIBLE);
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
    protected void onStop() {
        super.onStop();
        locationFetcher.onStop();
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
            if (mSocket != null) {
                mSocket.emit("unregister", user);           // exception generated when no internet
                mSocket.disconnect();
                mSocket.off("newChat", newChatListener);
                mSocket.off("getUsers", displayOnlineUsers);
            }
        } else {
            Log.e(">>>>>", "No Internet connection: OnDestroy() called");
        }
    }

    public void cancelWaiting(View view) {
        if (status == ACTIVE_CONNECTION) {
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
        } else {
            waiting_overlay.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.VISIBLE);
            //locationFetcher.onCreate();
            locationFetcher.onStart();
            if (locationFetcher.client == null)
                Log.e("clent", "....null");
            else
                Log.e("client", "....okay");

            location = locationFetcher.getLocation();
            establishConnection();
        }

    }


    @Override
    public void onBackPressed() {
        Log.e(" >>>> Home: ", " Back Pressed");
        this.finish();
    }

    double distanceTo(double lat1, double lon1, double lat2, double lon2) {
        double radlat1 = Math.PI * lat1 / 180;
        double radlat2 = Math.PI * lat2 / 180;
        double theta = lon1 - lon2;
        double radtheta = Math.PI * theta / 180;
        double dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;
        return dist;
    }
}
