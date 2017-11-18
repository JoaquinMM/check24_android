package com.example.hacktum2017;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ShowMapInfo extends Activity implements OnMapReadyCallback {
    Context contexto;
    private String latitu;
    private String longitu;
    private double latitud;
    private double longitud;
    private static final String TAG = "MApInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG,"aquiiiiii"+ String.valueOf(latitud));
        //Log.d(TAG,"aquiiiiii"+ String.valueOf(longitud));
    }
    public ShowMapInfo(String latitu, String longitu) {
        this.latitu = latitu;
        this.longitu = longitu;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        latitud= Double.parseDouble(latitu);
        longitud= Double.parseDouble(longitu);

        LatLng latLng = new LatLng(latitud, longitud);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        map.addMarker(new MarkerOptions()
                .title("Site")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    SupportMapFragment mapFragment;
    public void Crear(Context contextito, SupportMapFragment mapFragment) {
        //setContentView(R.layout.activity_main);

        contexto = contextito;
        this.mapFragment=mapFragment;
        this.mapFragment.getMapAsync(this);
    }
}



