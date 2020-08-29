package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuisineActivity extends AppCompatActivity {

    private ExecutorService mThreadPool;
    private int etageSelected = 0;
    private TextView personnes;
    private TextView plaques;
    private String [] affluence;
    private String [] stoves;
    private Handler mHandler = new Handler();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void main(String[] args){
        this.onActivityResult(2000, 1, new Intent());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);

        android.support.v7.app.ActionBar laverieActionBar = getSupportActionBar();
        laverieActionBar.setTitle("Cuisine");

        mThreadPool = Executors.newCachedThreadPool();
        personnes = (TextView) findViewById(R.id.personnes);
        plaques = (TextView) findViewById(R.id.plaques);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.etage_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String res = (String) adapterView.getItemAtPosition(i);
                setEtageSelected(Integer.parseInt(res));
                Log.d("etage", etageSelected + "");

                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        //get kitchen states
                        TCPConnection conn = new TCPConnection();
                        conn.send("get kitchen affluence\n");
                        conn.receive();
                        conn.send("ok\n");
                        affluence = conn.receive().split(" ");
                        conn.closeConnection();

                        conn = new TCPConnection();
                        conn.send("get stove states\n");
                        conn.receive();
                        conn.send("ok\n");
                        stoves = conn.receive().split(" ");
                        conn.closeConnection();

                        //update UI
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String Text = affluence[etageSelected - 2];
                                personnes.setText(Text);
                            }
                        });
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String Text = stoves[etageSelected - 2];
                                plaques.setText(Text);
                            }
                        });

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setEtageSelected(int etageSelected) {
        this.etageSelected = etageSelected;
    }

    public void page1(View view){
        startActivityForResult(new Intent(this,ChronoActivity.class),2000);
    }


    public void page_1(View view){
        startActivityForResult(new Intent(this, StatActivity.class),2000);
    }

    public void finish(View view){
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode==2000){
            if(resultCode==1){
                // on met le r®¶sultat qui sera r®¶cup®¶rer par l'activit®¶ A si l'on souhaite r®¶cup®¶rer ce r®¶sultat (ici facultative)
                this.setResult(1);
                finish(); // si le code r®¶sultat = 1 on ferme l'activit®¶
            }

        }
        super.onActivityResult (requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubis, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(CuisineActivity.this, ParametresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
