package com.example.digvijay.letschat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import static com.example.digvijay.letschat.MyPreferences.getId;
import static com.example.digvijay.letschat.MyPreferences.getStatus;

public class Profile extends AppCompatActivity {

    private ImageView imageViewRound;
    TextView statusTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageViewRound = (ImageView) findViewById(R.id.imageView_round);
//        imageViewRound.setImageBitmap( decodeSampledBitmapFromResource(getResources(), R.drawable.profile_pic, 300, 300));

        String id = getId(this);

        statusTv = (TextView) findViewById(R.id.statusTV);
        statusTv.setText(getStatus(this));
        imageViewRound.setImageBitmap(getFacebookProfilePicture(this, id  ));

    }

    private Bitmap getFacebookProfilePicture(Context context, String userID) {

        String url = "https://graph.facebook.com/" + userID + "/picture?type=large";
        Glide.with(context).load(url).crossFade().into(imageViewRound);

        return null;

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void editStatusClicked(View view) {
        Intent i = new Intent(this, EditStatus.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusTv.setText(getStatus(this));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,      // This method makes it easy to load a bitmap of arbitrarily large size into an ImageView that displays a 300x300 pixel thumbnail
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


}