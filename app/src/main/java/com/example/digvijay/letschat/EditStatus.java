package com.example.digvijay.letschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.digvijay.letschat.MyPreferences.*;

public class EditStatus extends AppCompatActivity {

    String oldstatus, newstatus;
    EditText statusET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        statusET = (EditText) findViewById(R.id.statusEditText);

        oldstatus = getStatus(this);
        statusET.setText(oldstatus);

    }

    public void updateStatus(View view) {

        newstatus = statusET.getText().toString();

        Log.e(">>", "oldstatus: " + oldstatus + " newstatus: " +newstatus );

        if( newstatus.equals(oldstatus)){
            Toast.makeText(EditStatus.this, "Please set a new status", Toast.LENGTH_SHORT).show();
            return;
        }

//        if( isNetworkAvailable() )
        /*
            Code for updating new status of user on the server side ..
        */

        setStatus(this, newstatus);

        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
