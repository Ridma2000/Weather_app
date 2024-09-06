package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private HttpURLConnection urlConnection;
    private String[] date_list = new String[20];
    private String[] temp_list = new String[20];
    private String[] humidity_list = new String[20];
    private Integer[] icon_list = new Integer[20];
    private String unit = "metric"; // default unit
    private String city = "Colombo"; // default city

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Weather App");

        loadSettings();
        new FetchData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload the data when returning from SettingsActivity
        loadSettings();
        new FetchData().execute();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("WeatherAppPreferences", MODE_PRIVATE);
        unit = sharedPreferences.getString("metric_unit", "metric");
        city = sharedPreferences.getString("city", "Colombo");
    }

    public class FetchData extends AsyncTask<String, Void, String> {
        private BufferedReader reader;

        @Override
        protected void onPostExecute(String forecastJsonStr) {
            try {
                JSONObject weatherObject = new JSONObject(forecastJsonStr);
                JSONArray dataList = weatherObject.getJSONArray("list");

                for (int i = 0; i < 20; i++) {
                    JSONObject valueObject = dataList.getJSONObject(i);
                    date_list[i] = valueObject.getString("dt_txt");
                    JSONObject mainObject = valueObject.getJSONObject("main");
                    temp_list[i] = mainObject.getString("temp");
                    humidity_list[i] = mainObject.getString("humidity");
                    JSONArray weatherArray = valueObject.getJSONArray("weather");
                    JSONObject weatherArrayObject = weatherArray.getJSONObject(0);
                    icon_list[i] = getApplicationContext().getResources().getIdentifier(
                            "pic_" + weatherArrayObject.getString("icon"), "drawable", getApplicationContext().getPackageName());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CustomListAdapter adapter = new CustomListAdapter(MainActivity.this, date_list, temp_list, humidity_list, icon_list);
            ListView list = findViewById(R.id.list_view);
            list.setAdapter(adapter);

            list.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent detailActivity = new Intent(MainActivity.this, DetailActivity.class);
                detailActivity.putExtra("date", date_list[i]);
                detailActivity.putExtra("temperature", temp_list[i]);
                detailActivity.putExtra("humidity", humidity_list[i]);
                detailActivity.putExtra("icon", icon_list[i]);
                startActivity(detailActivity);
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            String forecastJsonStr = null;
            try {
                String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=1bfe6d43bd9035746b6358b48be1ed12&units=" + unit;
                URL url = new URL(BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e("MainActivity", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("MainActivity", "Error closing stream", e);
                    }
                }
            }
            return forecastJsonStr;
        }
    }
}
