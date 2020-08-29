package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by alexa on 18/03/2018.
 */

public class InfoActivity extends AppCompatActivity{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        android.support.v7.app.ActionBar ActionBar = getSupportActionBar();
        ActionBar.setTitle("Informations");
    }

    public void finish(View view) {
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, AccueilActivity.class));
    }

}
