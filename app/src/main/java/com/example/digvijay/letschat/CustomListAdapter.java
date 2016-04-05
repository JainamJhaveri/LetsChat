package com.example.digvijay.letschat;


import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titleid;
    private final int[] imgid;
    private double latitudes[],longitudes[];
    private float distances[];
    private final Resources r;


    public CustomListAdapter(Activity context, String[] titleid, int[] imgid,double[] latitudes,double[] longitudes,float[] distances, Resources r) {
        super(context, R.layout.layout_adapter, titleid);
        // TODO Auto-generated constructor stub

        this.r = r;
        this.context=context;
        this.titleid=titleid;
        this.imgid=imgid;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        this.distances = distances;

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

        holder.imageview.setImageBitmap(
                Profile.decodeSampledBitmapFromResource(r, imgid[position], 300, 300));


//        holder.imageview.setImageResource(imgid[position]);
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

