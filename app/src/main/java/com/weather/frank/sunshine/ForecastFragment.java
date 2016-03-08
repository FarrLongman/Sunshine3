package com.weather.frank.sunshine;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Object;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            ForecastFragment.FetchWeatherTask refreshWeather = new ForecastFragment.FetchWeatherTask();
            refreshWeather.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //create fake data in the list
        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 65/56",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHER STATION - 60/51",
                "Sun - Sunny - 80/68"
        };
        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray)
        );
        //construct the adapter with proper parameters you want to populate
        ArrayAdapter<String> mForecastAdapter = new  ArrayAdapter<String>(

                //get the current content (the parent of the current activity)
                getActivity(),

                //the layout list_item_forecast
                R.layout.list_item_forecast,

                //the id of the textview to populate
                R.id.list_item_forecast_textview,

                //the data to fill in
                weekForecast       );

        //to set the adapter for the listView
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }




    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {
        public FetchWeatherTask(){

        }

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {
            //set the connection and reader and input to be downloaded to be null to kick start
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            //try to connect to the URL
            try{
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?id=1809858&mode=json&units=metric&cnt=7");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    return null;
                }

                forecastJsonStr = buffer.toString();
            }
            catch (IOException e){
                Log.e(LOG_TAG,"Error",e);
                return null;
            }
            finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try {
                        reader.close();
                    }
                    catch (final IOException e){
                        Log.e(LOG_TAG,"Error Closing Stream", e);
                    }
                }
            }
            return null;
        }
    }





}
