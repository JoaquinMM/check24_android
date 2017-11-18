package com.example.hacktum2017;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.sql.Time;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

public class Aportar extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonUpload;
    private int contador=1;
    private Button buttonChoose;
    private Button btnCamera;
    private ImageView imageView;

    private ImageView capturedImage;

    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aportar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        btnCamera = (Button) findViewById(R.id.btnCamera);
        capturedImage= (ImageView) findViewById(R.id.imageView);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose.setTypeface(font);
        buttonChoose.setOnClickListener(this);
        imageView  = (ImageView) findViewById(R.id.imageView);
        btnCamera.setTypeface(font);
        btnCamera.setOnClickListener(this);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonUpload.setTypeface(font);
        buttonUpload.setOnClickListener(this);
    }

    private void uploadImage(){

        int hours1 = new Time(System.currentTimeMillis()).getHours();
        int hours2 = new Time(System.currentTimeMillis()).getMinutes();
        int hours3 = new Time(System.currentTimeMillis()).getSeconds();
        String image2 = getStringImage(bitmap);
        Log.d("NAME", "image-" + Integer.toString(hours3) + "-" + Integer.toString(hours2)+ "-" +  Integer.toString(hours1));
        Log.d("PHOTO", image2);

        //Showing the progress dialog
        String msg="Image uploading. So much information...";
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading image",msg,false,false);
        JSONObject body = new JSONObject();
        try {
            JSONObject image = new JSONObject();
            image.put("name", "image-" + Long.valueOf(System.currentTimeMillis()) + ".jpeg");
            image.put("base64", getStringImage(bitmap));
            body.put("image", image);
        } catch(JSONException jex) {
            String message = jex.getMessage();
            Log.d("JSON exception", message);
        }
        final String requestBody = body.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject picData = new JSONObject(s);
                           /* picData = new JSONObject(
                                    "{" +
                                            "\"landmarkAnnotations\": [" +
                                            "{" +
                                            "\"mid\":\"/m/02rg0nb\"," +
                                            "\"description\":\"Marienplatz\"," +
                                            "\"score\":0.9773778," +
                                            "\"locations\": [" +
                                            "{" +
                                            "\"latLng\": {" +
                                            "\"latitude\":48.137225," +
                                            "\"longitude\":11.57554" +
                                            "}" +
                                            "}" +
                                            "]" +
                                            "}" +
                                            "]," +
                                            "\"webDetection\":{" +
                                            "\"webEntities\": [" +
                                            "{" +
                                            "\"entityId\":\"/m/09g3lm\"," +
                                            "\"score\":23.024," +
                                            "\"description\":\"Marienplatz\"" +
                                            "}," +
                                            "{" +
                                            "\"entityId\":\"/m/02rg0nb\"," +
                                            "\"score\":14.3776," +
                                            "\"description\":\"New Town Hall\"" +
                                            "}," +
                                            "{" +
                                            "\"entityId\":\"/m/0cwkr4\"," +
                                            "\"score\":9.6576," +
                                            "\"description\":\"St Peter's Church\"" +
                                            "}" +
                                            "]," +
                                            "\"fullMatchingImages\": [" +
                                            "{" +
                                            "\"url\":" +
                                            "\"http://adrijanaputovanja.hr/wp-content/uploads/2017/11/MUNCHEN.jpg\"" +
                                            "}," +
                                            "{" +
                                            "\"url\":" +
                                            "\"http://www.yugopolis.ru/data/img/cdbb04ac08902ad689d7e0cd19585b33/125977.jpg\"" +
                                            "}" +
                                            "]," +
                                            "}" +
                                            "}"
                            );*/
                            JSONObject response = picData.getJSONObject("response");
                            String description = response.getString("description");
                            JSONObject location = response.getJSONArray("locations").getJSONObject(0).getJSONObject("latLng");
                            double latitude = location.getDouble("latitude");
                            double longitude = location.getDouble("longitude");
                            Intent photoInfo = new Intent(Aportar.this, PhotoInfo.class);
                            photoInfo.putExtra("description", description);
                            photoInfo.putExtra("latitude", latitude);
                            photoInfo.putExtra("longitude", longitude);
                            Aportar.this.startActivity(photoInfo);
                        } catch(JSONException ex) {

                        }


                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Aportar.this, s , Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Aportar.this, "No connection", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                int hours1 = new Time(System.currentTimeMillis()).getHours();
                int hours2 = new Time(System.currentTimeMillis()).getMinutes();
                int hours3 = new Time(System.currentTimeMillis()).getSeconds();

                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                String image = getStringImage(bitmap);
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, "image-" + Integer.toString(hours3) + "-" + Integer.toString(hours2)+ "-" +  Integer.toString(hours1));
                return params;
            }*/

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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                if(contador==1) {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //Setting the Bitmap to ImageView
                    imageView.setImageBitmap(bitmap);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(resultCode == RESULT_OK) {
            if(contador==1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                capturedImage.setImageBitmap(bitmap);
                contador=contador+1;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == buttonChoose){
            showFileChooser();
        }
        if(v == btnCamera){
            openCamera();
        }
        if(v == buttonUpload){
            uploadImage();

        }
    }
}



