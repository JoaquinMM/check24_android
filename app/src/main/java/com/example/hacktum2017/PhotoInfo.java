package com.example.hacktum2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class PhotoInfo extends AppCompatActivity implements View.OnClickListener {

    private String description;
    private double latitude;
    private double longitude;

    private String arrivalDate;
    private String departureDate;
    private String bitmap;
    private Button findHotelsButton;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        bitmap = intent.getStringExtra("bitmap_image");
       // Bitmap image_bitmap = getImageString(bitmap);
        Bitmap image_bitmap = StringToBitMap(bitmap);

        imageView  = (ImageView) findViewById(R.id.imageView3);
        imageView.setImageBitmap(image_bitmap);

        showDescription();
        //findPicture();

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        findHotelsButton = (Button) findViewById(R.id.button5);
        findHotelsButton.setTypeface(font);
        findHotelsButton.setOnClickListener(this);

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void arrivalButton(View v) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        arrivalDate = Integer.valueOf(year).toString() + "-" + Integer.valueOf(month).toString() + "-" + Integer.valueOf(day).toString();
        Toast.makeText(PhotoInfo.this, "Arrival date set to " + arrivalDate, Toast.LENGTH_LONG).show();
    }

    public void departureButton(View v) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        departureDate = Integer.valueOf(year).toString() + "-" + Integer.valueOf(month).toString() + "-" + Integer.valueOf(day).toString();
        Toast.makeText(PhotoInfo.this, "Departure date set to " + departureDate, Toast.LENGTH_LONG).show();
    }

    private void showDescription() {
        TextView textView = (TextView) findViewById(R.id.description);
        textView.setText(description);
    }

    public void findHotelsButton(/*View v*/) {
        //Showing the progress dialog
        String msg="Displaying available hotels";
        final ProgressDialog loading = ProgressDialog.show(this,"Showing map",msg,false,false);
        JSONObject body = new JSONObject();
        try {
            body.put("latitude", latitude);
            body.put("longitude", longitude);
            if(arrivalDate == null) {
                Calendar calendar = Calendar.getInstance();
                arrivalDate = Integer.toString(calendar.get(Calendar.YEAR)) + "-" + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "-" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            }
            body.put("arrivalDate", arrivalDate);
            if(departureDate == null) {
                Calendar calendar = Calendar.getInstance();
                departureDate = Integer.toString(calendar.get(Calendar.YEAR)) + "-" + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "-" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH) + 1);
            }
            body.put("departureDate", departureDate);
        } catch(JSONException jex) {
            String message = jex.getMessage();
            Log.d("JSON exception", message);
        }

        final String requestBody = body.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.Hotels_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject picData = new JSONObject(s);
                            Intent mapInfo = new Intent(PhotoInfo.this, ShowMapInformation.class);
                            mapInfo.putExtra("hotels", picData.toString());
                            mapInfo.putExtra("centreLongitude", longitude);
                            mapInfo.putExtra("centreLatitude", latitude);
                            mapInfo.putExtra("description", description);
                            PhotoInfo.this.startActivity(mapInfo);

                        } catch(JSONException ex) {

                        }
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(PhotoInfo.this, "Success" , Toast.LENGTH_LONG).show();
                        //finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(PhotoInfo.this, "No connection", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){


            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    Log.d("volley ex", uee.getMessage());
                    return null;
                } catch(Exception ex) {
                    Log.d("another volley ex", ex.getMessage());
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String data = "";
                if(response != null) {
                    try {
                        data = new JSONObject(new String(response.data)).toString();
                    } catch (JSONException ex) {
                        String message = ex.getMessage();
                        Log.d("response parsing error", message);
                    }
                }
                return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if(v == findHotelsButton){
            findHotelsButton();
        }
    }








}

