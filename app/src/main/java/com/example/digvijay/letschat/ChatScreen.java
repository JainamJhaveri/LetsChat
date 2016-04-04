package com.example.digvijay.letschat;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatScreen extends AppCompatActivity {

    ListView lv;
    EditText editText;
    ChatScreenAdapter chatScreenAdapter;
    String oppUserId;
    String oppUserName;
    View sendChatScreen,disableSend;
    com.github.nkzawa.socketio.client.Socket mSocket;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSocket = SocketHandler.getSocket();
        mSocket.on("newMessageR", messageReceived);
        mSocket.on("leftChat",disableSending);

        disableSend = (View)findViewById(R.id.disableSend);
        sendChatScreen = (View)findViewById(R.id.sendChatScreen);
        editText = (EditText)findViewById(R.id.message);
        lv = (ListView)findViewById(R.id.chatList);
        ArrayList<ChatMessage> list = new ArrayList<>();
        chatScreenAdapter = new ChatScreenAdapter(this,list);
        lv.setAdapter(chatScreenAdapter);

        oppUserId = getIntent().getExtras().getString("id");
        oppUserName = getIntent().getExtras().getString("name");
        toolbar.setTitle(oppUserName);
        Toast.makeText(this,oppUserName,Toast.LENGTH_LONG).show();
    }

    public void sendMessage(View v){
        String message = editText.getText().toString();
        editText.setText("");
        ChatMessage m = new ChatMessage(message,true);
        chatScreenAdapter.add(m);
        JSONObject ob = new JSONObject();
        try {
            ob.put("message",message);
            ob.put("to",oppUserId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit("newMessageS",ob);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("newMessageR", messageReceived);
        JSONObject ob = new JSONObject();
        try {
            ob.put("to",oppUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("leftChat",ob);
        Log.e("   >>>>>>>>>>    ", "ChatScreenOnDestroy");

    }

    Emitter.Listener messageReceived = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject ob =(JSONObject)args[0];
                    ChatMessage m = null;
                    try {
                        m = new ChatMessage(ob.getString("message"),false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    chatScreenAdapter.add(m);

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
                }
            });
        }
    };
}
