package com.example.hackathon2;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String encoded;
    ImageView imgVW;
    String request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = (Button)findViewById(R.id.btnCamera);
         imgVW = (ImageView)findViewById(R.id.imageView);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0 );
            }
        });
    }
    JSONObject finalObject;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");

         imgVW.setImageBitmap(bitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        request = "{\"requests\":[{\"image\":{\"content\":\"" + encoded+
                "\"},\"features\":[{\"type\":\"LABEL_DETECTION\",\"maxResults\":1}]}]}";

        submit(request);

       // System.out.println(encoded);


    }
    JsonObjectRequest cucumber;
    private void submit(final String request) {
        String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyC1RiDsgTZc01z_6V7Duyys8ALBivD6TO8";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        /*try {

            JSONObject jsonObj = new JSONObject(request);

            cucumber = new JsonObjectRequest(
                    Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("response");

                    finalObject = response;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("rip");
                }
            }
            );
            requestQueue.add(cucumber);


        } catch (Throwable e) {
            System.out.println("rip2");
        }*/





        try {
            requestQueue = Volley.newRequestQueue(this);
            String URL = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyC1RiDsgTZc01z_6V7Duyys8ALBivD6TO8";
            JSONObject jsonBody = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj4 = new JSONObject();
            obj2.put("image",obj4);
            obj4.put("content", encoded);
            JSONObject obj3 = new JSONObject();
            obj3.put("type","LABEL_DETECTION");
            obj3.put("maxResults",1);
            obj2.put("features",obj3);
            jsonBody.put("requests", obj2);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    System.out.println(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());

                }
            }) {
                @Override
                public String getBodyContentType() {

                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {

                        responseString = String.valueOf(response.statusCode);
                        System.out.println(responseString);

                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TextView txtview = (TextView)findViewById(R.id.textView);
        // txtview.setText(finalObject.toString());
/*

        requestQueue = Volley.newRequestQueue(this);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                Map<String, String> params1 = new HashMap<String, String>();
                Map<String, String> params2 = new HashMap<String, String>();

                params1.put("image", encoded);
                params2.put("type", "LABEL_DETECTION");
                params2.put("maxResults", 1);

                return params;
            }
        };

        requestQueue.add(strRequest);
*/
    }

}

