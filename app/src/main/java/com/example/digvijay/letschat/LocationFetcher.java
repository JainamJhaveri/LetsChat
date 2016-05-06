package com.example.digvijay.letschat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Digvijay on 05-04-2016.
 */
public class LocationFetcher implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient client;
    Context context;
    Location location;

    public LocationFetcher(Context context) {
        this.context = context;
    }

    void onStart() {
        client.connect();
    }

    void onCreate() {

            client = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();

    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the mi ssing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("onConnected","...returned");
            return;
        }
        Log.e("onConnected","...set");
        location = LocationServices.FusedLocationApi.getLastLocation(client);
        if( location==null );
        Log.e("onConnected","setButNull");
    }

    Location getLocation() {
        return location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("onConnectionSuspended","");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("onConnectionFailed","");
    }

    public void onStop() {
        if (client != null)
            client.disconnect();
    }
}
