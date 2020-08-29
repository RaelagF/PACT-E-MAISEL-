package com.example.alexa.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.HashMap;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by zoé on 28/01/2018.
 */

public class NotificationsActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //Variable declaration
    private TextView letter;
    private ExpandableListView listMail;
    ArrayList subjects = null;
    ArrayList texts = null;
    private String[] mail = new String[]{"Ménage prévu ce mercredi 30","Coupure d'eau vendredi 2 entre 9h et 12h"};
    ExpandableListAdapter adapter = null;
    List<String> mailObject;
    HashMap<String, List<String>> mailContent;
    private ExecutorService mThreadPool;
    private Handler mHandler = new Handler();

    //todo : getResult pour récupérer le username depuis loginActivity et donc le roomNumber
    private int roomNumber = 718;

    //Variable that indicates if a letter is waiting or not
    //private boolean waitLetter = false;
    //ask server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        android.support.v7.app.ActionBar laverieActionBar = getSupportActionBar();
        laverieActionBar.setTitle("Notifications");

        mThreadPool = Executors.newCachedThreadPool();

        letter = (TextView) findViewById(R.id.letter);
        colorLetter();

        listMail = (ExpandableListView) findViewById(R.id.list);

        mailObject = new ArrayList<String>();
        mailContent = new HashMap<String, List<String>>();
        mailObject.add("Contrôle de ménage 16 mai");
        List<String> menage1 = new ArrayList<String>();
        menage1.add("Controle de ménage prévu dans les étages 5 et 6");
        mailContent.put(mailObject.get(0),menage1);
        adapter = new ExpandableListAdapter(this, mailObject, mailContent);
        listMail.setAdapter(adapter);

        startMailService();
        //while (subjects  == null){}
        System.out.println(subjects);
        System.out.println(texts);
    }

    public void finish(View view) {
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void colorLetter(){
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                TCPConnection conn = new TCPConnection();
                conn.send("get letter state\n");
                conn.receive();
                conn.send(roomNumber + "\n");
                String res = conn.receive();
                conn.closeConnection();

                Log.d("res", res);
                if (res.equals("1")) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            letter.setText("Vous avez du courrier !");
                            letter.setBackgroundColor(ContextCompat.getColor(NotificationsActivity.this,R.color.colorAccent));
                        }
                    });
                }
                else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            letter.setText("Votre casier est vide.");
                            letter.setBackgroundColor(ContextCompat.getColor(NotificationsActivity.this,R.color.white));
                        }
                    });
                }
            }
        });

    }

    // Launching the service
    public void startMailService() {
        Intent intent = new Intent(this, MailService.class);
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on MAIL string
        IntentFilter filter = new IntentFilter(MailService.MAIL);
        registerReceiver(mailReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        unregisterReceiver(mailReceiver);
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver mailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            subjects = (ArrayList<String>) intent.getStringArrayListExtra("subject");
            texts = intent.getStringArrayListExtra("text");
            for (int i = 0; i<subjects.size(); i++){
                mailObject.add((String) subjects.get(i));
                List<String> temp = new ArrayList<String>();
                temp.add((String) texts.get(i));
                mailContent.put(mailObject.get(i+1),temp);
            }
            adapter.notifyDataSetChanged();
            onResume();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.basicmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                colorLetter();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(NotificationsActivity.this, ParametresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
