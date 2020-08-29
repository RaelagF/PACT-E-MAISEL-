package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zoé on 01/02/2018.
 */

//method connected to database : attemptLogin() and addId()

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /** Variables */
    EditText username;
    EditText password;
    Button signin;
    Button newid;
    private ExecutorService mThreadPool;
    ArrayList<ArrayList<String>> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        /** Initialize the LIST */
        ArrayList<String> Number1 = new ArrayList<String>();
        Number1.add("raphael");
        Number1.add("pact21");
        LIST.add(Number1);

        mThreadPool = Executors.newCachedThreadPool();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        newid = (Button) findViewById(R.id.newid);
        newid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addId();
            }
        });
    }

    /** method that tries to connect a user with its username and password */
    public void attemptLogin(){

        /** check if the username/password is in the database */
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                String user = username.getText().toString();
                String pw = password.getText().toString();

                //examine if the username and password are in the right form
                if (user.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez un identifiant.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (user.contains(" ")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Désolé, l'identifiant ne peut pas contenir d'espaces.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (pw.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez un mot de passe.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (pw.contains(" ")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Désolé, le mot de passe ne peut pas contenir d'espaces.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }

                //start a TCP connection
                TCPConnection conn = new TCPConnection();
                conn.send("login\n");
                conn.receive();
                conn.send(user + " " + pw + "\n");
                String res = conn.receive();
                conn.closeConnection();

                switch (res) {
                    case "True":
                        startActivity(new Intent(LoginActivity.this, AccueilActivity.class));
                        LoginActivity.this.finish();
                        break;

                    case "False":
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Mot de passe invalide", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;

                    default:
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                }
            }
        });
    }

    /** method to add a new user to the database/LIST */
    public void addId(){
        /** check if the username is in the database */
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                String user = username.getText().toString();
                String pw = password.getText().toString();

                //examine if the username and password are in the right form
                if (user.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez un identifiant.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (user.contains(" ")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Désolé, l'identifiant ne peut pas contenir d'espaces.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (pw.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez un mot de passe.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (pw.contains(" ")) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Désolé, le mot de passe ne peut pas contenir d'espaces.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }

                //start a TCP connection
                TCPConnection conn = new TCPConnection();
                conn.send("sign up\n");
                conn.receive();
                conn.send(user + " " + pw + "\n");
                String res = conn.receive();
                conn.closeConnection();

                switch (res) {
                    case "True":
                        startActivity(new Intent(LoginActivity.this, AccueilActivity.class));
                        LoginActivity.this.finish();
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Element ajouté !", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;

                    case "False":
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Sorry, some problems happened, please try again.", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;

                    default:
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                }
            }
        });
    }
}
