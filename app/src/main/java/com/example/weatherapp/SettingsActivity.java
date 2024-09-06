package com.example.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerMetricUnit;
    private EditText editTextCity;
    private Button buttonSaveSettings;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerMetricUnit = findViewById(R.id.spinner_metric_unit);
        editTextCity = findViewById(R.id.edit_text_city);
        buttonSaveSettings = findViewById(R.id.button_save_settings);

        sharedPreferences = getSharedPreferences("WeatherAppPreferences", MODE_PRIVATE);

        setupMetricUnitSpinner();
        loadSettings();

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void setupMetricUnitSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.metric_units_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetricUnit.setAdapter(adapter);
    }

    private void loadSettings() {
        String metricUnit = sharedPreferences.getString("metric_unit", "metric");
        String city = sharedPreferences.getString("city", "");

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerMetricUnit.getAdapter();
        if (metricUnit != null) {
            int position = adapter.getPosition(metricUnit.equals("metric") ? "Celsius" : "Fahrenheit");
            spinnerMetricUnit.setSelection(position);
        }
        editTextCity.setText(city);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("metric_unit", spinnerMetricUnit.getSelectedItem().toString().equals("Celsius") ? "metric" : "imperial");
        editor.putString("city", editTextCity.getText().toString());
        editor.apply();
    }
}
