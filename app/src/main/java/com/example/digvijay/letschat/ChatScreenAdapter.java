package com.example.digvijay.letschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatScreenAdapter extends ArrayAdapter<ChatMessage> {
    private final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, MY_IMAGE = 2, OTHER_IMAGE = 3;

    public ChatScreenAdapter(Context context, ArrayList<ChatMessage> data) {
        super(context, R.layout.chat_bubble, data);
    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if (item.isMine()) return MY_MESSAGE;
        else if (!item.isMine()) return OTHER_MESSAGE;
        return MY_MESSAGE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_bubble, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());


        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_bubble_other, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).getContent());
        }

        /* else if (viewType == MY_IMAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatrow, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.otherchat, parent, false);
        }*/


        return convertView;
    }
}