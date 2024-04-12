package com.example.bluesky;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText Search_City;
    ImageView Search, Weather_Img;
    TextView Weather_Description, Degree, Date_Text, City, Pressure, Wind, Humidity;
    String City_Name, Api_Id;
    String BASE_URL;
    String weather_Description, weather, degree, date, city, pressure, wind, humidity;
    LinearLayout main_panel, starter;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Search_City = findViewById(R.id.search_city);
        Search = findViewById(R.id.search);
        Weather_Img = findViewById(R.id.weather_img);
        Weather_Description = findViewById(R.id.weather);
        Degree = findViewById(R.id.degree);
        Date_Text = findViewById(R.id.date);
        City = findViewById(R.id.city);
        Pressure = findViewById(R.id.pressure);
        Wind = findViewById(R.id.wind);
        Humidity = findViewById(R.id.humidity);
        main_panel = findViewById(R.id.main_panel);
        starter = findViewById(R.id.starter);

        Api_Id = "18bee35754bff4e11eadb7e3f50105ea";

        animationView = findViewById(R.id.animation_view);
        animationView.pauseAnimation();

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchWeather();
            }
        });
    }

    public void FetchWeather() {
        if (Search_City.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
        } else {
            animationView.playAnimation();
            starter.setVisibility(View.VISIBLE);
            main_panel.setVisibility(View.GONE);

            City_Name = Search_City.getText().toString();

            BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + City_Name + "&appid=" + Api_Id + "&units=metric";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject jObj = new JSONObject(String.valueOf(response));

                        city = jObj.getString("name");

                        JSONObject main_obj = jObj.getJSONObject("main");
                        degree = main_obj.getString("temp");
                        pressure = main_obj.getString("pressure");
                        humidity = main_obj.getString("humidity");

                        JSONObject wind_obj = jObj.getJSONObject("wind");
                        wind = wind_obj.getString("speed");

                        JSONArray weather_obj = jObj.getJSONArray("weather");
                        JSONObject obj = weather_obj.getJSONObject(0);
                        weather_Description = obj.getString("description");
                        weather = obj.getString("main");

                        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
                        Date date = new Date();

                        String text_date = dateFormat.format(date);

                        Degree.setText(degree + "°C");
                        Pressure.setText(pressure + " Pa");
                        Humidity.setText(humidity + " %");
                        Wind.setText(wind + " m/s");
                        City.setText(city);
                        Date_Text.setText(text_date);
                        Weather_Description.setText(weather_Description);

                        if (weather.equals("Clouds")) {
                            Weather_Img.setImageResource(R.drawable.cloud);
                        } else if (weather.equals("Smoke")) {
                            Weather_Img.setImageResource(R.drawable.smoke);
                        } else if (weather.equals("Fog")) {
                            Weather_Img.setImageResource(R.drawable.fog);
                        } else if (weather.equals("Mist")) {
                            Weather_Img.setImageResource(R.drawable.mist);
                        } else if (weather.equals("Clear")) {
                            Weather_Img.setImageResource(R.drawable.clear);
                        } else if (weather.equals("Snow")) {
                            Weather_Img.setImageResource(R.drawable.snow);
                        } else if (weather.equals("Haze")) {
                            Weather_Img.setImageResource(R.drawable.haze);
                        } else {
                            Weather_Img.setImageResource(R.drawable.cloud);
                        }

                        animationView.pauseAnimation();
                        starter.setVisibility(View.GONE);
                        main_panel.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "City Not Found !!", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }
    }
}