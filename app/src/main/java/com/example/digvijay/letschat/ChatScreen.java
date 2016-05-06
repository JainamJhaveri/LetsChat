package com.example.digvijay.letschat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatScreen extends AppCompatActivity {

    ListView lv;
    EditText editText;
    ChatScreenAdapter chatScreenAdapter;
    String oppUserId;
    String oppUserName;
    View sendChatScreen, disableSend;
    Socket mSocket;
    DatabaseAdapter db;
    static final int ME = 0;
    static final int OTHER = 1;
    Context context;
    Calendar c;
    String lastmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        context = this;
        mSocket = SocketHandler.getSocket();
        mSocket.on("newMessageR", messageReceived);
        mSocket.on("userLeft", disableSending);

        disableSend = findViewById(R.id.disableSend);
        sendChatScreen = findViewById(R.id.sendChatScreen);
        editText = (EditText) findViewById(R.id.message);
        lv = (ListView) findViewById(R.id.chatList);
        ArrayList<ChatMessage> list = new ArrayList<>();
        chatScreenAdapter = new ChatScreenAdapter(this, list);
        lv.setAdapter(chatScreenAdapter);

        oppUserId = getIntent().getExtras().getString("id");
        oppUserName = getIntent().getExtras().getString("name");
        toolbar.setTitle(oppUserName);
        Toast.makeText(this, oppUserName, Toast.LENGTH_LONG).show();

        c = Calendar.getInstance();
        new DisplayMessages().execute();
    }


    public void sendMessage(View v) {
        String message = editText.getText().toString();
        editText.setText("");
        ChatMessage m = new ChatMessage(message, true);
        chatScreenAdapter.add(m);
        JSONObject ob = new JSONObject();
        try {
            ob.put("message", message);
            ob.put("to", oppUserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (db == null)
            db = new DatabaseAdapter(this);
        db.insertInTable_1(oppUserId, message, ME, c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
        mSocket.emit("newMessageS", ob);
        lastmessage = message;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("newMessageR", messageReceived);
        JSONObject ob = new JSONObject();
        try {
            ob.put("to", oppUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("leftChat", ob);
        Log.e("   >>>>>>>>>>    ", "ChatScreenOnDestroy");
        db.insertLastMessage(oppUserId, oppUserName, lastmessage);

    }

    Emitter.Listener messageReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject ob = (JSONObject) args[0];
                    ChatMessage m;
                    String message = null;
                    try {
                        message = ob.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    m = new ChatMessage(message, false);
                    chatScreenAdapter.add(m);
                    db.insertInTable_1(oppUserId, m.content, OTHER, c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                    lastmessage = message;

                }
            });
        }
    };

    Emitter.Listener disableSending = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    disableSend.setVisibility(View.VISIBLE);
                    sendChatScreen.setVisibility(View.INVISIBLE);
                    db.insertLastMessage(oppUserId, oppUserName, lastmessage);
                }
            });
        }
    };

    class DisplayMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            db = new DatabaseAdapter(context);

            db.insertName(oppUserId, oppUserName);
            ArrayList<ChatMessage> list = db.getMessages(oppUserId);

            for (int i = 0; i < list.size(); i++) {
                chatScreenAdapter.add(list.get(i));
            }
            return null;
        }
    }

}
