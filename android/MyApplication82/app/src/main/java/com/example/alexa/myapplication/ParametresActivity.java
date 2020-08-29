package com.example.alexa.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zoé on 28/01/2018.
 */

public class ParametresActivity extends AppCompatActivity{
    EditText username;
    EditText previousPassword;
    EditText newPassword;
    EditText newPasswordBis;
    Button passwordChange;

    private ExecutorService mThreadPool;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        android.support.v7.app.ActionBar ActionBar = getSupportActionBar();
        ActionBar.setTitle("Paramètres");

        username = (EditText) findViewById(R.id.idEditText);
        previousPassword = (EditText) findViewById(R.id.previousPasswordEditText);
        newPassword = (EditText) findViewById(R.id.newPasswordEditText);
        newPasswordBis = (EditText) findViewById(R.id.newPasswordEditTextBis);

        mThreadPool = Executors.newCachedThreadPool();

        passwordChange = (Button) findViewById(R.id.changeButton);
        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {

                String user = username.getText().toString();
                String previous = previousPassword.getText().toString();
                String newpw = newPassword.getText().toString();
                String newpwbis = newPasswordBis.getText().toString();

                //examine if the username and password are in the right form
                if (user.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez votre identifiant.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (previous.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez votre ancien mot de passe.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (newpw.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez un nouveau mot de passe.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (newpwbis.isEmpty()) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Entrez une deuxième fois le nouveau mot de passe.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }
                if (!(newpw.equals(newpwbis))) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Les deux nouveaux mots de passe ne sont pas les mêmes.", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return;
                }

                //start a TCP connection to check that the previous password is valid
                TCPConnection conn = new TCPConnection();
                conn.send("change password\n");
                conn.receive();
                conn.send(user + " " + previous + " " + newpw + "\n");
                String res = conn.receive();
                conn.closeConnection();

                switch (res) {
                    case "True":
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Mot de passe modifié.", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;

                    case "False":
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Désolé nous avons un problème...", Toast.LENGTH_SHORT).show();
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

    public void finish(View view){
        this.setResult(1);
        //CuisineActivity.setI(1) ;
        //startActivityForResult(new Intent(this, LoginActivity.class));
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
