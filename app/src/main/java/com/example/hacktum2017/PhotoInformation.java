package com.example.hacktum2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PhotoInformation extends AppCompatActivity /*implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener*/ {
    private TextView id_incidencia;
    private TextView version_control;
    private TextView title;
    private TextView comment;
    private TextView lat;
    private TextView lon;
    private TextView date;
    private TextView n_photos;
    private TextView location;
    private ImageView foto1_incidencia;
    private ImageView foto2_incidencia;
    private ImageView foto3_incidencia;
    private ImageView foto4_incidencia;
    private String identificador;

    private SliderLayout mDemoSlider;
    private int numero_photos;
    private String numero_photos1;
    private String imagen_1;
    private String imagen_2;
    private String imagen_3;
    private String imagen_4;

    private String latitu;
    private String longitu;
    private GoogleMap map;
    //ShowMapInformation mapa;  *****************

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_photo);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Showing and Enabling clicks on the Home/Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // setting up text views and stuff
        setUpUIViews();

        // recovering data from MainActivity, sent via intent
       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString("standModel");
            StandModel standModel = new Gson().fromJson(json, StandModel.class);*/

            /*numero_photos1 =standModel.getN_photos();*/
            numero_photos = 1;/*Integer.parseInt(numero_photos1);*/

            if (numero_photos == 1) {
                imagen_1 = "http://www.iconhot.com/icon/png/juicy-fruit/256/settings-39.png"; /*standModel.getFoto1_incidencia();*/
            } /*else if (numero_photos == 2) {
                imagen_1 = standModel.getFoto1_incidencia();
                imagen_2 = standModel.getFoto2_incidencia();
            } else if (numero_photos == 3) {
                imagen_1 = standModel.getFoto1_incidencia();
                imagen_2 = standModel.getFoto2_incidencia();
                imagen_3 = standModel.getFoto3_incidencia();
            } else if (numero_photos == 4) {
                imagen_1 = standModel.getFoto1_incidencia();
                imagen_2 = standModel.getFoto2_incidencia();
                imagen_3 = standModel.getFoto3_incidencia();
                imagen_4 = standModel.getFoto4_incidencia();
            }*/
            title.setText("Pruebe"/*standModel.getTitle()*/);
            comment.setText("Pruebe"/*standModel.getComment()*/);

            /*identificador = standModel.getId_incidencia();
            latitu = standModel.getLat();
            longitu = standModel.getLon();*/
        }
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String, String> url_maps = new HashMap<String, String>();

        if (numero_photos == 1) {
            url_maps.put("1", imagen_1);
        } /*else if (numero_photos == 2) {
            url_maps.put("1", imagen_1);
            url_maps.put("2", imagen_2);
        } else if (numero_photos == 3) {
            url_maps.put("1", imagen_1);
            url_maps.put("2", imagen_2);
            url_maps.put("3", imagen_3);
        } else if (numero_photos == 4) {
            url_maps.put("1", imagen_1);
            url_maps.put("2", imagen_2);
            url_maps.put("3", imagen_3);
            url_maps.put("4", imagen_4);
        }*/

        for (String name : url_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside);
            mDemoSlider.addSlider(textSliderView);

        }

        /* For the map*/
       /* mapa = new ShowMapInformation(latitu, longitu);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapa.Crear(this, mapFragment);*/
    }

    private void setUpUIViews() {
        title = (TextView) findViewById(R.id.title);
        comment = (TextView) findViewById(R.id.comment);
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

}

