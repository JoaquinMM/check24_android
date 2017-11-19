package com.example.hacktum2017;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;


public class ShowMapInformation extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private String finalJson;
    private double centreLongitude;
    private double centreLatitude;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map_information);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        finalJson = intent.getStringExtra("hotels");
        centreLatitude = intent.getDoubleExtra("centreLatitude", 0.0);
        centreLongitude = intent.getDoubleExtra("centreLongitude", 0.0);
        description = intent.getStringExtra("description");

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
        LatLng centreLatLng = new LatLng(Double.valueOf(centreLatitude), Double.valueOf(centreLongitude));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLatLng, 13));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions()
                .title(description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(centreLatLng));
        try {
            JSONObject parentObject = new JSONObject(finalJson);
            JSONArray results = parentObject.getJSONArray("hotels");
            for (int i = 0; i < results.length(); i++) {
                JSONObject finalObject = results.getJSONObject(i);
                String longitude = finalObject.getString("longitude");
                String latitude = finalObject.getString("latitude");
                String site_name = finalObject.getString("name");
                LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                map.addMarker(new MarkerOptions()
                        .title(site_name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .position(latLng));
            }
        } catch (JSONException ex) {
            String message = ex.getMessage();
            Log.d("exception in map", message);
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
