package com.example.hacktum2017;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoInfo extends AppCompatActivity {

    private String description;
    private double latitude;
    private double longitude;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        imageUrl = intent.getStringExtra("imageUrl");

        showDescription();
        findPicture();

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void findPicture() {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Bitmap img = BitmapFactory.decodeStream(bis);
            ImageView image = (ImageView) findViewById(R.id.image);
            image.setImageBitmap(img);
        } catch(MalformedURLException mex) {
            String message = mex.getMessage();
            Log.d("malformed url", message);
        } catch(IOException ioex) {
            String message = ioex.getMessage();
            Log.d("ioexception", message);
        } catch(Exception ex) {
            String message = ex.getMessage();
            Log.d("exception", message);
        }
    }

    private void showDescription() {
        TextView textView = (TextView) findViewById(R.id.description);
        textView.setText(description);
    }

    public void findHotelsButton(View v) {
        try {
            URL url = new URL(imageUrl);
        } catch (Exception e) {

        }
    }
}

