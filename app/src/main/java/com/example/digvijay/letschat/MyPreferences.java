package com.example.digvijay.letschat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jaina_000 on 4/4/2016.
 */
public class MyPreferences {

    private static final String DATA = "data";

    public MyPreferences() {}

    public static void setDistanceRange(Context context, int distancerange) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("distancerange", distancerange);
        prefEditor.commit();
    }

    public static void setStatus(Context context, String status){
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("status", status);
        prefEditor.commit();
    }

    public static String getStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString("status", "Enter your status here");
    }

    public static void setAgeRange(Context context, int agerange) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("agerange", agerange);
        prefEditor.commit();
    }

    public static int getDistanceRange(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPref.getInt("distancerange", 20);
    }

    public static int getAgeRange(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPref.getInt("agerange", 19);
    }

    public static void setId(Context context, String id) {
        SharedPreferences sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("id", id);
        prefEditor.commit();
    }

    public static void setLoggedIn(Context context, boolean b) {
        SharedPreferences sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putBoolean("isLogged", b);
        prefEditor.commit();
    }

    public static void setUsername(Context context, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("name", name);
        prefEditor.commit();
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPref.getString("name", "No Name found");
    }

    public static String getId(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPref.getString("id", "No Id found");
    }

    public static boolean getLoggedIn(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isLogged", false);
    }


    public static void clearPreferences(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();

        prefEditor.clear();
        prefEditor.commit();

        Log.e(">>>>>>>>>", "Preferences cleared");

    }

    // checks whether wifi is on or data is enabled. but it doesn't check if the data actually comes from the connected network or not.
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
