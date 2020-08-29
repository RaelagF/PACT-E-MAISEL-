package com.example.alexa.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    public void main(String[] args){
        this.onActivityResult(2000, 1, new Intent());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.etage_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void page1(View view){
        startActivityForResult(new Intent(this,page_2.class),2000);
    }


    public void page_1(View view){
        startActivityForResult(new Intent(this, page_3.class),2000);
    }

    public void finish(View view){
        this.setResult(1);
        //MainActivity.setI(1) ;
        //startActivityForResult(new Intent(this, LoginActivity.class));
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
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
