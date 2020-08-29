package com.example.alexa.myapplication;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zoé on 28/01/2018.
 */

//method connected to database : initialization of LABEL, makeReservation(),

//todo : transformer l'apparence en style ; couleur de notre machine réservée
//database : filling of LABEL

public class LaverieActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //declaration of variables
    //ici seront récupéré depuis le serveur les états des machines
    private String occupation = null;
    private String reservation = null;
    private LaverieButton b1, b2, b3, b4, b5, b6, b7, b8;
    private ArrayList<LaverieButton> b = new ArrayList<LaverieButton>();
    private ImageButton stat;

    // to make sure only one machine is reserved
    private int reserved;

    private ExecutorService mThreadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laverie);

        android.support.v7.app.ActionBar laverieActionBar = getSupportActionBar();
        laverieActionBar.setTitle("Laverie");

        mThreadPool = Executors.newCachedThreadPool();

        // recover the layout buttons
        /* todo  : variable number of button created directly here */
        b1 = (LaverieButton) findViewById(R.id.b1);
        b.add(b1);
        b2 = (LaverieButton) findViewById(R.id.b2);
        b.add(b2);
        b3 = (LaverieButton) findViewById(R.id.b3);
        b.add(b3);
        b4 = (LaverieButton) findViewById(R.id.b4);
        b.add(b4);
        b5 = (LaverieButton) findViewById(R.id.b5);
        b.add(b5);
        b6 = (LaverieButton) findViewById(R.id.b6);
        b.add(b6);
        b7 = (LaverieButton) findViewById(R.id.b7);
        b.add(b7);
        b8 = (LaverieButton) findViewById(R.id.b8);
        b.add(b8);

        setReserved(0);

        stat = findViewById(R.id.statButton);

        // button parameters
        for (int i = 0; i < 8; i++) {
            b.get(i).setOnClickListener(machineButton);
            b.get(i).setIdd(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStates();
    }

    private void updateStates() {

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                TCPConnection conn = new TCPConnection();
                conn.send("get washing machine states\n");
                conn.receive();
                conn.send("ok\n");
                occupation = conn.receive().replace(" ", "");
                Log.d("occupation", occupation);
                reservation = conn.receive().replace(" ", "");

                Log.d("reservation", reservation);

                conn.closeConnection();
                LaverieActivity.this.setColor(b);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basicmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateStates();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(LaverieActivity.this, ParametresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * change all the colors of the buttons
     */
    public void setColor(final ArrayList<LaverieButton> L) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int n = 8;
                for (int i = 0; i < n; i++) {
                    //case empty
                    if ((occupation.charAt(i) == '0') && (reservation.charAt(i) == '0')) {
                        L.get(i).setBackgroundColor(Color.GREEN);
                    }
                    //case reserved
                    if ((occupation.charAt(i) == '0') && (reservation.charAt(i) == '1')) {
                        L.get(i).setBackgroundColor(Color.BLUE);
                    }
                    //case occupied
                    if (occupation.charAt(i) == '1') {
                        L.get(i).setBackgroundColor(Color.RED);
                    }
                }
            }
        });
    }

    /**
     * when a button is clicked
     */
    private View.OnClickListener machineButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //update first
            Log.d("update", "first");
            updateStates();

            LaverieButton bi = (LaverieButton) view;
            final int idd = bi.getIdd();

            //case empty
            if ((occupation.charAt(idd) == '0') && (reservation.charAt(idd) == '0')) {

                //the user has already reserved one machine and it's not cancelled
                if ((reserved >= 0) && (reservation.charAt(reserved) == '1')) {
                    Toast.makeText(LaverieActivity.this, R.string.reservation_limit, Toast.LENGTH_SHORT).show();
                    return;
                }


                DialogInterface.OnClickListener okButton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };

                //the user is allowed to make a reservation
                AlertDialog.Builder builder = new AlertDialog.Builder(LaverieActivity.this);
                builder.setMessage(R.string.reservation)
                        .setTitle("Machine " + (idd + 1))
                        .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mThreadPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        TCPConnection conn = new TCPConnection();
                                        conn.send("reserve washing machine\n");
                                        conn.receive();
                                        conn.send(idd + "");
                                        String res = conn.receive();
                                        conn.closeConnection();
                                        Log.d("res0", res);

                                        updateStates();

                                        //res important!!!
                                        switch (res) {
                                            case "True":
                                                setReserved(idd);
                                                Looper.prepare();
                                                Toast.makeText(LaverieActivity.this, R.string.reservation_succeeded, Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                                break;

                                            case "False":
                                                Looper.prepare();
                                                Toast.makeText(LaverieActivity.this, R.string.problem, Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                                break;

                                            default:
                                                Looper.prepare();
                                                Toast.makeText(LaverieActivity.this, res, Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                        }
                                    }
                                });

                            }
                        })
                        .setNegativeButton(R.string.non, okButton);
                builder.show();

            }
            //case reserved by others
            if ((occupation.charAt(idd) == '0') && (reservation.charAt(idd) == '1') && (reserved != idd)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LaverieActivity.this);
                builder.setMessage(R.string.reserved_msg);
                builder.setTitle("Machine " + (idd + 1));
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }


            //case reserved by the user himself and he wants to cancel the reservation
            if ((occupation.charAt(idd) == '0') && (reservation.charAt(idd) == '1') && (reserved == idd)) {

                DialogInterface.OnClickListener okButton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                };

                //the user is about to cancel his reservation
                AlertDialog.Builder builder = new AlertDialog.Builder(LaverieActivity.this);
                builder.setMessage(R.string.cancel_reservation)
                        .setTitle("Machine " + (idd + 1))
                        .setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                mThreadPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        TCPConnection conn = new TCPConnection();
                                        conn.send("cancel reservation\n");
                                        conn.receive();
                                        conn.send(idd + "");
                                        String res = conn.receive();
                                        conn.closeConnection();
                                        Log.d("res0", res);

                                        setReserved(-1);
                                        updateStates();

                                        Looper.prepare();
                                        Toast.makeText(LaverieActivity.this, R.string.cancel_reservation_succeeded, Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                });

                            }
                        })
                        .setNegativeButton(R.string.non, okButton);
                builder.show();
            }


            //case occupied
            if (occupation.charAt(idd) == '1') {
                AlertDialog.Builder builder = new AlertDialog.Builder(LaverieActivity.this);
                builder.setMessage(R.string.occupied_msg);
                builder.setTitle("Machine " + (idd + 1));
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        }
    };

    /**
     * disconnection
     */
    public void finish(View view) {
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }


    /**
     * Setters and getters
     */
    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getReserved() {
        return reserved;
    }
}
