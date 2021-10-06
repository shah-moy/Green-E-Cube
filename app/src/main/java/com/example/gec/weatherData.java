package com.example.gec;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature,nicon,mcity,mWeatherType,mpressure,mhumidity,mWindSpeed,mWindDirection;

    private int mCondition;
    public static weatherData fromJson(JSONObject jsonObject)
    {
        try
        {
            weatherData weatherD =new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.nicon=updateweathericon(weatherD.mCondition);
            double tempResut=jsonObject.getJSONObject("main").getDouble("temp")-273.15;

            double pressureResut=jsonObject.getJSONObject("main").getDouble("pressure");
            double humidityResut=jsonObject.getJSONObject("main").getDouble("humidity");
            double windspeedResut=jsonObject.getJSONObject("wind").getDouble("speed");
            double windirectionResut=jsonObject.getJSONObject("wind").getDouble("deg");



            int roundedValue=(int)Math.rint(tempResut);
            weatherD.mTemperature=Integer.toString(roundedValue);

            int roundedValue2=(int)Math.rint(pressureResut);
            weatherD.mpressure=Integer.toString(roundedValue2);
            int roundedValue3=(int)Math.rint(humidityResut);
            weatherD.mhumidity=Integer.toString(roundedValue3);

            weatherD.mWindSpeed=Double.toString(windspeedResut);
            weatherD.mWindDirection=Double.toString(windirectionResut);


            return weatherD;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }


    }



    private static String updateweathericon(int condition)
    {
        if (condition >= 0 && condition <= 300) {
            return "thunderstrom";
        }
        else if (condition >= 300 && condition <= 500) {
            return "lightrain";
        }
        else if (condition >= 500 && condition <= 600) {
            return "shower";
        }
        else if (condition >= 600 && condition <= 700) {
            return "snow";
        }
        else if (condition >= 700 && condition <= 771) {

            return "fog";
        }
        else if (condition >= 772 && condition <= 800) {
            return "overcast";
        }
        else if (condition == 800) {
            return "sunny";
        }
        else if (condition >= 801 && condition <= 804) {
            return "cloudy";
        }
        else if (condition >= 900 && condition <= 902) {
            return "thunderstrom";
        }
        else if (condition == 903) {
            return "snow";
        }
        else if (condition == 904) {
            return "sunny";
        }
        else if (condition >= 905 && condition <= 1000) {
            return "thunderstrom";
        }
        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }


    public String getmpressure() {
        return mpressure+"hPa";
    }
    public String getmhumidity() {
        return mhumidity+"°";
    }

    public String getmWindSpeed() {
        return mWindSpeed+"m/s";
    }
    public String getmWindDirection() { return mWindDirection+"°"; }


    public String getNicon() {
        return nicon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}




