package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;

/**
 * Created by alexa on 28/01/2018.
 */

public class ChronoActivity extends AppCompatActivity{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private long timeWhenStopped = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);

        android.support.v7.app.ActionBar ActionBar = getSupportActionBar();
        ActionBar.setTitle("Minuteur");
    }

    public void finish(View view) {
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void startChronometer(View view) {
        ((Chronometer) findViewById(R.id.chronometre)).setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        ((Chronometer) findViewById(R.id.chronometre)).start();
    }

    public void stopChronometer(View view) {
        timeWhenStopped = ((Chronometer) findViewById(R.id.chronometre)).getBase() - SystemClock.elapsedRealtime();
        ((Chronometer) findViewById(R.id.chronometre)).stop();
    }




}
