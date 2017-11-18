package com.example.hacktum2017;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.hacktum2017.models.Check24;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import static java.lang.Thread.sleep;


public class ShowMapInformation extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    public static final String URL_TO_HIT = "https://api.hotel.check24.de/hackatum/hotels/searches/30908196/results.json";

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private JSONArray results;
    private String finalJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map_information);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        finalJson = intent.getStringExtra("hotels");


        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        new JSONTask().execute(URL_TO_HIT);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        for(int i=0; i<results.length(); i++) {
            JSONObject finalObject;
            String longitude = "";
            String latitude = "";
            String site_name = "";
            try {
                finalObject = results.getJSONObject(i);
                longitude = finalObject.getString("longitude");
                latitude = finalObject.getString("latitude");
                site_name = finalObject.getString("name");
            } catch(JSONException ex) {
                String message = ex.getMessage();
                Log.d("exception in map", message);
            }
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            map.addMarker(new MarkerOptions()
                    .title(site_name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(latLng));
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        }
    }


    public class JSONTask extends AsyncTask<String,String, List<Check24> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Check24> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Log.d("NAME2", finalJson);

                JSONObject parentObject = new JSONObject(finalJson);

                List<Check24> Check24List = new ArrayList<>();

                JSONArray finalObjectbefore = parentObject.getJSONArray("hotels");
                results = finalObjectbefore;
                Gson gson = new Gson();
                for(int i=0; i<finalObjectbefore.length(); i++) {
                    JSONObject finalObject = finalObjectbefore.getJSONObject(i);
                    Check24 check24 = gson.fromJson(finalObject.toString(), Check24.class);
                    Check24List.add(check24);
                }
                return Check24List;

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
    }


}
