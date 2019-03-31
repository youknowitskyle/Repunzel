package com.example.hackathon1;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String encoded;
    ImageView imgVW;
    String request;
    boolean popBool = false;

    String word_of_the_day;
    String[] pun_list = {"Pun 1", "", ""};

    static String base_url = "https://puns.samueltaylor.org/?word=";

    Button button7;

    String chosen_pun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        imgVW = (ImageView) findViewById(R.id.imageView);
        button7 = (Button) findViewById(R.id.button7);
        String chosenPun;
        TextView txtTemp = (TextView) findViewById(R.id.punText);
        txtTemp.setText("");
        button7.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                //PopupMenu popup = new PopupMenu(MainActivity.this, button7);
                //Inflating the Popup using xml file
               // popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
               // popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    String punString;

                   /* public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this, "You Chose : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        punString = (String) item.getTitle();
                        return true;
                    }


                });
                popup.show();
*/              Toast.makeText(MainActivity.this, "Please generate a pun first.",Toast.LENGTH_SHORT).show();

            }
        });


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tmpView = (TextView)findViewById(R.id.punText);
                tmpView.setText("");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    JSONObject finalObject;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        imgVW.setImageBitmap(bitmap);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        request = "{\"requests\":[{\"image\":{\"content\":\"" + encoded +
                "\"},\"features\":[{\"type\":\"LABEL_DETECTION\",\"maxResults\":1}]}]}";

        submit(request);

        // System.out.println(encoded);


    }

    JsonObjectRequest cucumber;
    JSONArray parentObject;

    public void displayText(String s) {

    }

    private void submit(final String request) {
        String url = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyC1RiDsgTZc01z_6V7Duyys8ALBivD6TO8";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        try {
            requestQueue = Volley.newRequestQueue(this);
            String URL = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyC1RiDsgTZc01z_6V7Duyys8ALBivD6TO8";
            JSONObject jsonBody = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj4 = new JSONObject();
            obj2.put("image", obj4);
            obj4.put("content", encoded);
            JSONObject obj3 = new JSONObject();
            obj3.put("type", "LABEL_DETECTION");
            obj3.put("maxResults", 1);
            obj2.put("features", obj3);
            jsonBody.put("requests", obj2);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    try {
                        JSONObject name1 = new JSONObject(response);
                       /* parentObject = name1.getJSONArray("responses");
                        JSONObject name2 = parentObject.getJSONObject(0);
                        JSONArray label = name2.getJSONArray("description");*/

                        JSONArray obj_JSONArray = name1.getJSONArray("responses");
                        JSONObject obj_JSONObject2 = obj_JSONArray.getJSONObject(0);
                        JSONArray components_array = obj_JSONObject2.getJSONArray("labelAnnotations");
                        JSONObject obj_JSONObject3 = components_array.getJSONObject(0);
                        System.out.println("word identified-" + obj_JSONObject3.getString("description"));
                        word_of_the_day = obj_JSONObject3.getString("description");

                        for (int i = 0; i < word_of_the_day.length() - 1; i++){

                            if (word_of_the_day.substring(i, i + 1).equals(" ")){
                                word_of_the_day = word_of_the_day.substring(0, i + 1);
                                break;
                            }
                        }
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    URL obj = new URL(base_url + word_of_the_day);
                                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                    con.setRequestMethod("GET");
                                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                    StringBuffer response = new StringBuffer();
                                    String inputLine;
                                    while ((inputLine = in.readLine()) != null) {
                                        inputLine = inputLine.trim();
                                        if (inputLine.length() > 9) {
                                            if (inputLine.substring(0, 4).equals("<li>")) {
                                                String line = inputLine.substring(4, inputLine.length() - 5);
                                                response.append(line + "-");
                                            }
                                        }
                                    }
                                    String str_response = response.toString();
                                    //      System.out.print(str_response); -- with '-'
                                    String[] final_puns = str_response.split("-");
                                    List<String> myList = new ArrayList<>();
                                    for (String temp : final_puns) {
                                        myList.add(temp);
                                    }
                                    in.close();

                                    List<Integer> list = new ArrayList<>();
                                    for (int i = 0; i <= 9; i++) {
                                        list.add(i);
                                    }
                                    Collections.shuffle(list);

                                    pun_list[0] = myList.get(list.get(0));
                                    pun_list[1] = myList.get(list.get(1));
                                    pun_list[2] = myList.get(list.get(2));

                                    System.out.println(pun_list[0]);
                                    System.out.println(pun_list[1]);
                                    System.out.println(pun_list[2]);

                                /*    MenuItem item1 = (MenuItem)findViewById(R.id.choice1) ;
                                    MenuItem item2 = (MenuItem)findViewById(R.id.choice2) ;
                                    MenuItem item3 = (MenuItem)findViewById(R.id.choice3) ;
                                    item1.setTitle(pun_list[0]);
                                    item2.setTitle(pun_list[1]);
                                    item3.setTitle(pun_list[2]);*/


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Button punButton = (Button)findViewById(R.id.button7);
                                            punButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    PopupMenu menuOpt = new PopupMenu(MainActivity.this, button7);
                                                    menuOpt.getMenuInflater().inflate(R.menu.popup_menu, menuOpt.getMenu());


                                                    // TextView txtTemp = (TextView)findViewById(R.id.punText) ;
                                                    //txtTemp.setText(pun_list[0]);
                                                    menuOpt.getMenu().getItem(0).setTitle(pun_list[0]);
                                                    menuOpt.getMenu().getItem(1).setTitle(pun_list[1]);
                                                    menuOpt.getMenu().getItem(2).setTitle(pun_list[2]);

                                                    menuOpt.show();

                                                    menuOpt.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                        @Override
                                                        public boolean onMenuItemClick(MenuItem item) {
                                                            chosen_pun = (String)item.getTitle();
                                                            TextView tmpView = (TextView)findViewById(R.id.punText);
                                                            tmpView.setText(item.getTitle());
                                                            return true;
                                                        }
                                                    });
                                                }
                                            });



                                        }
                                    });

                                    //System.out.println("1:" + myList.get(list.get(0)));
                                    //System.out.println("2:" + myList.get(list.get(1)));
                                    //System.out.println("3:" + myList.get(list.get(2)));
                                    popBool = true;
                                    //if (popBool) {

                                    } catch(java.io.IOException e){
                                        System.out.println("cant connect?");
                                    }
                                }

                        });
                        thread.start();



                        //return obj_JSONObject3.getString("description");
                       /* for(int j = 0; j<label.length();j++){
                            System.out.println(label.getString(j));
                        }*/
                    } catch (JSONException e) {
                        System.out.println("failed");
                    }


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
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}




