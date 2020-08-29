package com.example.alexa.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by alexa on 28/01/2018.
 */

public class page_3 extends AppCompatActivity {

/*    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_3);

        Spinner spinner = (Spinner) findViewById(R.id.stat_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.etage_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spin = (Spinner) findViewById(R.id.day_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,
                R.array.day_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spin.setAdapter(adapt);

        barChart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(44f,1));
        barEntries.add(new BarEntry(44f,2));
        barEntries.add(new BarEntry(44f,3));
        barEntries.add(new BarEntry(44f,4));
        barEntries.add(new BarEntry(44f,5));
        barEntries.add(new BarEntry(44f,6));
        barEntries.add(new BarEntry(44f,7));
        barEntries.add(new BarEntry(44f,8));
        barEntries.add(new BarEntry(44f,9));
        barEntries.add(new BarEntry(44f,10));
        barEntries.add(new BarEntry(44f,11));
        barEntries.add(new BarEntry(44f,12));
        barEntries.add(new BarEntry(44f,13));
        barEntries.add(new BarEntry(44f,14));
        barEntries.add(new BarEntry(44f,15));
        barEntries.add(new BarEntry(44f,16));
        barEntries.add(new BarEntry(44f,17));
        barEntries.add(new BarEntry(44f,18));
        barEntries.add(new BarEntry(44f,19));
        barEntries.add(new BarEntry(44f,20));
        barEntries.add(new BarEntry(44f,21));
        barEntries.add(new BarEntry(44f,22));
        barEntries.add(new BarEntry(44f,23));
        BarDataSet barDataSet = new BarDataSet(barEntries, "heures");

        ArrayList<String> heures = new ArrayList<>();
        heures.add("00h-01h");
        heures.add("01h-02h");
        heures.add("02h-03h");
        heures.add("03h-04h");
        heures.add("04h-05h");
        heures.add("05h-06h");
        heures.add("06h-07h");
        heures.add("07h-08h");
        heures.add("08h-09h");
        heures.add("09h-10h");
        heures.add("10h-11h");
        heures.add("11h-12h");
        heures.add("12h-13h");
        heures.add("13h-14h");
        heures.add("14h-15h");
        heures.add("15h-16h");
        heures.add("16h-17h");
        heures.add("17h-18h");
        heures.add("18h-19h");
        heures.add("19h-20h");
        heures.add("20h-21h");
        heures.add("21h-22h");
        heures.add("22h-23h");
        heures.add("23h-00h");

        BarData theData = new BarData(/*heures,*//*barDataSet);
        barChart.setData(theData);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

    }

    /**
     * Created by alexa on 28/01/2018.
     */

    /*public static class NotificationsActivity {
    }*/
}