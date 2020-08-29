package com.example.alexa.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;

/**
 * Created by alexa on 28/01/2018.
 */

public class page_2 extends AppCompatActivity{

    private long timeWhenStopped = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_2);
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
