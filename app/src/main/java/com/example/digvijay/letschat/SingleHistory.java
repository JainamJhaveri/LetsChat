package com.example.digvijay.letschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SingleHistory extends AppCompatActivity {

    private ChatScreenAdapter chatScreenAdapter;
    ListView lv;
    DatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_history);
        String userID = getIntent().getExtras().getString("userID");
        String userName = getIntent().getExtras().getString("userName");
        setTitle(userName);

        lv = (ListView) findViewById(R.id.singleChatList);
        ArrayList<ChatMessage> list = new ArrayList<>();
        chatScreenAdapter = new ChatScreenAdapter(this, list);

        db = new DatabaseAdapter(this);
        list = db.getMessages(userID);
        for (int i = 0; i < list.size(); i++) {
            chatScreenAdapter.add(list.get(i));
        }

        lv.setAdapter(chatScreenAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
