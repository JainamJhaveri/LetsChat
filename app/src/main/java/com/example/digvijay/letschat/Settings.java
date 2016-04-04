package com.example.digvijay.letschat;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private GPSLocation mylocation;
    SeekBar seek_dist, seek_age;
    int distprogress = 0, ageprogress = 0;
    TextView tv_distance, tv_age, tv_address;
    private int min_d = 20,  min_a = 19 ; // used for seekbars
    private Location anotherlocation;       // a sample test location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getViewReferences();
        manipulateSeekBars();
        setupARandomLocation();

    }

    private void setupARandomLocation() {// setting any random coordinates
        anotherlocation = new Location("");
        anotherlocation.setLatitude(23.0062181);
        anotherlocation.setLongitude(72.529375);
    }



    public void showMyLocation(View view) {

        mylocation = new GPSLocation(this);
        if (mylocation.canGetLocation()) {

            double latitude = mylocation.getLatitude();
            double longitude = mylocation.getLongitude();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            int d_from_another  = mylocation.distancefrom(anotherlocation);
            System.out.println("Distance: " + d_from_another);
            Toast.makeText(Settings.this, d_from_another + " meters" , Toast.LENGTH_SHORT).show();

        } else {
            mylocation.showSettingsAlert();
        }
    }

    private void manipulateSeekBars() {

        seek_dist.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distprogress = min_d + progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_distance.setText(distprogress + " meters");
            }
        });

        seek_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ageprogress = min_a + progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_age.setText("18 - " +ageprogress);
            }
        });

    }

    private void getViewReferences() {
        seek_dist = (SeekBar) findViewById(R.id.seek_dist);
        seek_age = (SeekBar) findViewById(R.id.seek_age);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_age = (TextView) findViewById(R.id.tv_age);
    }

}