package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        TextView txtDate = findViewById(R.id.txt_detail_date);
        TextView txtTemp = findViewById(R.id.txt_detail_temp);
        TextView txtHumidity = findViewById(R.id.txt_detail_humidity);
        ImageView imgIcon = findViewById(R.id.img_detail_icon);

        txtDate.setText(intent.getStringExtra("date"));
        txtTemp.setText(intent.getStringExtra("temperature") + " °C");
        txtHumidity.setText("Humidity: " + intent.getStringExtra("humidity") + "%");
        imgIcon.setImageResource(intent.getIntExtra("icon", R.drawable.pic_01d));
    }
}

