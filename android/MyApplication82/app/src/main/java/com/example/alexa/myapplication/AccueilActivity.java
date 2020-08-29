package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by alexa on 28/01/2018.
 */

public class AccueilActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    ImageButton laverieButton;
    ImageButton notificationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        android.support.v7.app.ActionBar ActionBar = getSupportActionBar();
        ActionBar.setTitle("Accueil");
    }

    public void startLaverie(View view){
        startActivityForResult(new Intent(this, LaverieActivity.class),2000);}

    public void startCuisine(View view){
        startActivity(new Intent(this, CuisineActivity.class));}

    public void startNotifications(View view){
        startActivityForResult(new Intent(this, NotificationsActivity.class),2000);}

    public void startParamètres(View view){
        startActivity(new Intent(this, ParametresActivity.class));}

    public void main(String[] args){
        this.onActivityResult(2000, 1, new Intent());
    }

    public void finish(View view){
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void startInfo(View view){
        startActivity(new Intent(this,InfoActivity.class));
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==2000){
            if(resultCode==1){
                // on met le résultat qui sera récupérer par l'activité A si l'on souhaite récupérer ce résultat (ici facultative)
                this.setResult(1);
                finish(); // si le code résultat = 1 on ferme l'activité
            }

        }
        super.onActivityResult (requestCode, resultCode, data);
    }


}
