package com.example.digvijay.letschat;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfo extends DialogFragment {

String userId;
    String userStatus;
    String userName;



    public UserInfo() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);
        TextView name = (TextView)v.findViewById(R.id.dialogUserName);
        TextView status = (TextView)v.findViewById(R.id.dialogStatus);
        ImageView image = (ImageView)v.findViewById(R.id.dialogImage);
        String url = "https://graph.facebook.com/" + userId + "/picture?type=large";
        Glide.with(getActivity()).load(url).crossFade().centerCrop().into(image);
        name.setText(userName);

        status.setText(userStatus);
        //getDialog().setTitle(userName);
        
        return v;


    }

}
