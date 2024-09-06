package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] item_date;
    private final String[] item_temp;
    private final Integer[] icons;
    private final String[] item_humidity;

    public CustomListAdapter(Activity context, String[] item_date, String[] item_temp, String[] item_humidity, Integer[] icons) {
        super(context, R.layout.my_list, item_date);
        this.context = context;
        this.item_date = item_date;
        this.item_temp = item_temp;
        this.item_humidity = item_humidity;
        this.icons = icons;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list, null, true);
        TextView txtDate = rowView.findViewById(R.id.txt_date);
        TextView txtTemp = rowView.findViewById(R.id.txt_temp);
        TextView txtHumidity = rowView.findViewById(R.id.txt_humidity);
        ImageView imgIcon = rowView.findViewById(R.id.icon);

        txtDate.setText(item_date[position]);
        txtTemp.setText(item_temp[position] + " °C");
        txtHumidity.setText("Humidity: " + item_humidity[position] + "%");
        imgIcon.setImageResource(icons[position]);

        return rowView;
    }
}
