package com.example.digvijay.letschat;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titleid;
    private double latitudes[],longitudes[];
    private float distances[];
    private final Resources r;
    String ids[];


    public CustomListAdapter(Activity context, String[] titleid, double[] latitudes,double[] longitudes,float[] distances, Resources r,String[] ids) {
        super(context, R.layout.layout_adapter, titleid);
        // TODO Auto-generated constructor stub

        this.r = r;
        this.context=context;
        this.titleid=titleid;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.distances = distances;
        this.ids = ids;

    }

    protected class RowViewHolder {
        public TextView name,lat,lon,distance;
        public ImageView imageview;

    }

    public View getView(int position,View view,ViewGroup parent) {
        View rowView=view;


        RowViewHolder holder;

        if(rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_adapter, parent, false);

            holder = new RowViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.name);
            holder.imageview = (ImageView) rowView.findViewById(R.id.imageView);
            holder.lat = (TextView) rowView.findViewById(R.id.lat);
            holder.lon = (TextView) rowView.findViewById(R.id.lon);
            holder.distance = (TextView) rowView.findViewById(R.id.distance);

            rowView.setTag(holder);
        }
        else {
            holder = (RowViewHolder) rowView.getTag();
        }


        holder.name.setText(titleid[position]);
        holder.lat.setText(latitudes[position]+"");
        holder.lon.setText(longitudes[position]+"");
        holder.distance.setText(distances[position]+"");



        String url = "https://graph.facebook.com/" + ids[position] +"/picture?type=large";
        Glide.with(context).load(url).crossFade().centerCrop().into(holder.imageview);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = ((TextView)view).getText().toString();
                Toast.makeText(getContext(),item,Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;

    };
}

