package com.example.digvijay.letschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class HistoryAdapter extends BaseAdapter {

    String[] names;
    String[] ids;
    String[] lastMessage;
    Context context;

    public HistoryAdapter(Context context, String[] names, String[] ids, String[] lastMessage) {
        this.names = names;
        this.ids = ids;
        this.lastMessage = lastMessage;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_layout, parent, false);
        }

        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView lastMessageTv = (TextView) rowView.findViewById(R.id.lastMessage);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        name.setText(names[position]);
        lastMessageTv.setText(lastMessage[position]);
        String url = "https://graph.facebook.com/" + ids[position] + "/picture?type=large";
        Glide.with(context).load(url).crossFade().centerCrop().into(imageView);

        return rowView;


    }
}