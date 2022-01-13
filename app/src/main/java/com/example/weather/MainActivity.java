package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {


    private TextView day,time,location,temperature,day_name;
    private ImageView current_icon,searchIV;
    private RecyclerView recyclerView;

    private ConstraintLayout clayout;
    private ArrayList<Forecast_model>list;

    private AppCompatButton search,setDefault;

    private EditText search_city;

    private String prev_location;

    private int state_icon;

    private ProgressBar progressBar;

    private  String condition;



    private
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        day=findViewById(R.id.day);
        time=findViewById(R.id.time);
        location=findViewById(R.id.location);
        day_name=findViewById(R.id.day_name);
        temperature=findViewById(R.id.temperature);
        current_icon=findViewById(R.id.imageView);
        searchIV=findViewById(R.id.search_IV);
        progressBar=findViewById(R.id.progressbar);
        clayout=findViewById(R.id.clayout);

        recyclerView=findViewById(R.id.recyclerview);

        if(!check_internet())
        {

            Dialog dialog1=new Dialog(MainActivity.this);
            dialog1.setContentView(R.layout.internet_unavailable_bg);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog1.show();
        }

        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.search_dialog);
        search=dialog.findViewById(R.id.Search);


        search_city=dialog.findViewById(R.id.search_city);
        condition="";
        getTime(new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date())
        ,new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()));

get_forecast("bhopal");
getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        list=new ArrayList<>();



       get_forecast("Bhopal");


       searchIV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
               dialog.show();
           }
       });

       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

             if (check_internet())
             {
               prev_location=location.getText().toString();

               if(!search_city.getText().toString().isEmpty()) {
                   get_forecast(search_city.getText().toString());
                   location.setText(search_city.getText().toString().substring(0, 1).toUpperCase() + search_city.getText().toString().substring(1).toLowerCase());
                   dialog.dismiss();
               }
           }
             else
             {

                 Dialog dialog1=new Dialog(MainActivity.this);
                 dialog1.setContentView(R.layout.internet_unavailable_bg);
                 dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                 dialog1.show();
             }
             }
       });


       current_icon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           Toast.makeText(MainActivity.this,condition,Toast.LENGTH_SHORT).show();


           }
       });
    }



    private void  get_forecast(String cityName)
    {

        Singleton singleton=Singleton.getInstance(this);
        RequestQueue requestQueue=singleton.getRequestQueue();
        String url="http://api.weatherapi.com/v1/forecast.json?key=e40c7dd6ec3c4e58baa114640210612&q="+cityName+"&days=1&aqi=yes&alerts=yes";


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                progressBar.setVisibility(View.GONE);
                clayout.setVisibility(View.VISIBLE);
                try {
                    list.clear();

                    temperature.setText(response.getJSONObject("current").getString("temp_c")+"°");
                    Picasso.get().load("http:".concat(response.getJSONObject("current").getJSONObject("condition").getString("icon"))).into(current_icon);
                     condition=response.getJSONObject("current").getJSONObject("condition").getString("text");


                    JSONArray hourArray=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour");


                    for (int i = 0; i < hourArray.length(); i++) {

                        JSONObject hourobj=hourArray.getJSONObject(i);

                        list.add(new Forecast_model(hourobj.getString("time"),hourobj.getJSONObject("condition").getString("icon")
                                ,hourobj.getString("temp_c"),hourobj.getJSONObject("condition").getString("text")));



                    }
                    getTime(response.getJSONObject("location").getString("localtime").substring(8,10),response.getJSONObject("location").getString("localtime").substring(11));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                getBackground(condition);

                myAdapter=new MyAdapter(MainActivity.this,list);

                recyclerView.setAdapter(myAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
if(check_internet())
{
                    location.setText(prev_location);
                    Toast.makeText(MainActivity.this, "Enter a valid city name!", Toast.LENGTH_SHORT).show();
                    get_forecast(prev_location);}

            }

        }
        );

        singleton.addToRequestQue(jsonObjectRequest);

    }

    private  void getTime(String date,String currtime)
    {

       String dayname= LocalDate.now().getDayOfWeek().name();

       String monthname=LocalDate.now().getMonth().name();


       time.setText(currtime);
       day.setText(dayname.substring(0,1).toUpperCase()+dayname.substring(1).toLowerCase()+", " +
               ""+date+" th"+" "+monthname.substring(0,1)+monthname.substring(1).toLowerCase());
       day_name.setText(dayname);
    }

    private  void getBackground(String weatherType)
    {

     if(weatherType.contains("lear")||weatherType.contains("unny")) {

         clayout.setBackground(getResources().getDrawable(R.drawable.pink_bg));
         return ;

     }
     else if (weatherType.contains("loud"))
     {

         clayout.setBackground(getResources().getDrawable(R.drawable.light_blue));
         return ;
     }
     else if(weatherType.contains("snow"))
     {
         clayout.setBackground(getResources().getDrawable(R.drawable.bg1));
         return ;
     }
        clayout.setBackground(getResources().getDrawable(R.drawable.rain_bg2));

    }

    private boolean check_internet()
    {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return  connected;
    }






}