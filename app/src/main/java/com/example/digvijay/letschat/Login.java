package com.example.digvijay.letschat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.example.digvijay.letschat.MyPreferences.*;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isLoggedIn()) {
            setContentView(R.layout.activity_login);
        } else {
            Intent in = new Intent(this, Home.class);
            startActivity(in);
            this.finish();
        }
    }

    private boolean isLoggedIn() {
        return getLoggedIn(this);
    }



}
