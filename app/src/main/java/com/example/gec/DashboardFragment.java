package com.example.gec;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class DashboardFragment extends Fragment {

    private ImageView goB;

    final String App_ID = "a5d88d0524031c860af2d7379e30fdda";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Providor = LocationManager.GPS_PROVIDER;

    TextView NameofCity, WeatherState, Temperature,Pressure,Humidity,WindSpeed,WindDirection;
    ImageView mweathericon;

    RelativeLayout mrefresh;
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        WeatherState = root.findViewById(R.id.weathercondition);
        Temperature = root.findViewById(R.id.temperatue);
        mweathericon = root.findViewById(R.id.weathericon);
        NameofCity = root.findViewById(R.id.cityname);


        Pressure= root.findViewById(R.id.pressure);
        Humidity= root.findViewById(R.id.humidity);

        WindSpeed= root.findViewById(R.id.windspeed);
        WindDirection= root.findViewById(R.id.winddirection);

        goB= root.findViewById(R.id.addfield);
        goB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(),MapActivity.class);
                startActivity(in);
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getActivity().getSystemService (Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("appid",App_ID);
                LetsDoSomeNetworking(params);



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
//not able to get location
            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Providor, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(),"locationget Seccecfully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied permission
            }

        }
    }


    private void LetsDoSomeNetworking(RequestParams params)
    {
        AsyncHttpClient client =new AsyncHttpClient();
        client.get(Weather_URL,params,new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(getActivity(),"data Get Success",Toast.LENGTH_SHORT).show();
                weatherData weatherD =weatherData.fromJson(response);
                updateUI(weatherD);


                //super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //
            }
        });

    }

    private void updateUI(weatherData weather) {
        Temperature.setText(weather.getmTemperature());

        Pressure.setText(weather.getmpressure());
        Humidity.setText(weather.getmhumidity());
        WindSpeed.setText(weather.getmWindSpeed());
        WindDirection.setText(weather.getmWindDirection());



        NameofCity.setText(weather.getMcity());
        WeatherState.setText(weather.getmWeatherType());
       int resourceID=getResources().getIdentifier(weather.getNicon(),"drawable", getActivity().getPackageName());
        mweathericon.setImageResource(resourceID);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager!=null)
        {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}