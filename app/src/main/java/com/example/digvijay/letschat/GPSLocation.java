package com.example.digvijay.letschat;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

public class GPSLocation extends Service implements LocationListener {

    private final Context mContext;
    private static final long MIN_D_CHANGE = 10; // 10 meters
    private static final long AFTER_T = 1000 * 8 ;  // 8 seconds
    protected LocationManager locationManager;
    boolean isGPSEnabled, isNWEnabled, isPassiveEnabled, cangetLocation;
    Location loc;
    private double latitude, longitude;

    public GPSLocation(Context context) {
        isGPSEnabled = isNWEnabled = isPassiveEnabled = cangetLocation = false;
        mContext = context;
        getgpslocation();
    }

    public Location getgpslocation() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNWEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isPassiveEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        if (!isGPSEnabled && !isNWEnabled) {
            cangetLocation = false;
            System.out.println(" None of the services is enabled ");
            Log.d("gps_location: ", "gps and nw not enabled");
            return null;
        }

        cangetLocation = true;
        if (isNWEnabled) {
            getLocationFrom(LocationManager.NETWORK_PROVIDER);
        } else if (isGPSEnabled) {
            getLocationFrom(LocationManager.GPS_PROVIDER);
        } else {
            getLocationFrom(LocationManager.PASSIVE_PROVIDER);
        }

        return loc;
    }

    private void getLocationFrom(String PROVIDER) {
        Log.d("Provider: ", PROVIDER);

        try{
            locationManager.requestLocationUpdates(PROVIDER, MIN_D_CHANGE, AFTER_T, this);
            if (locationManager != null) {
                Log.d("lm: ", "not null");
                loc = locationManager.getLastKnownLocation(PROVIDER);
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                System.out.println("lat: "+latitude + " long: "+longitude);
            }
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
    }

    protected boolean canGetLocation() {
        return this.cangetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("Geo_Location: ", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public double getLatitude() {
        if( loc != null )
            latitude = loc.getLatitude();
        return latitude;
    }

    public double getLongitude() {
        if( loc != null )
            longitude = loc.getLongitude();
        return longitude;
    }


    public int distancefrom( Location anotherloc ){
        return (int) loc.distanceTo( anotherloc );
    }


}
