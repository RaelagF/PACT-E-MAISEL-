package com.example.alexa.myapplication;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zoé on 28/01/2018.
 */
//Note : transformer l'apparence en style ; couleur de notre machine réservée

public class LingerieActivity extends AppCompatActivity {

    //ici seront récupéré depuis le serveur les états des machines ; hashmap ?
    private static ArrayList<String> LABEL = new ArrayList<>();
    //pour l'instant on crée un nb défini de boutons -> à changer pour que cela puisse être implémenté autre part qu'à la Maisel
    private LaverieButton b1,b2,b3,b4,b5,b6,b7,b8;
    private static ArrayList<LaverieButton> b = new ArrayList<LaverieButton>();

    //booleen pour ne pouvoir reserver qu'une seule machine
    private static boolean reserved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lingerie);
        setSupportActionBar((Toolbar)findViewById(R.id.laverie_toolbar));

        //récupération des boutons (plus tard : création d'un nb défini de boutons)
        b1 = (LaverieButton) findViewById(R.id.b1);
        b.add(b1);
        LABEL.add("empty");
        b2 = (LaverieButton) findViewById(R.id.b2);
        b.add(b2);
        LABEL.add("empty");
        b3 = (LaverieButton) findViewById(R.id.b3);
        b.add(b3);
        LABEL.add("full");
        b4 = (LaverieButton) findViewById(R.id.b4);
        b.add(b4);
        LABEL.add("full");
        b5 = (LaverieButton) findViewById(R.id.b5);
        b.add(b5);
        LABEL.add("full");
        b6 = (LaverieButton) findViewById(R.id.b6);
        b.add(b6);
        LABEL.add("empty");
        b7 = (LaverieButton) findViewById(R.id.b7);
        b.add(b7);
        LABEL.add("empty");
        b8 = (LaverieButton) findViewById(R.id.b8);
        b.add(b8);
        LABEL.add("empty");

        setReserved(false);

        //paramètres des boutons
        setColor(b);
        for (int i=0; i<8; i++){
            b.get(i).setOnClickListener(machineButton);
            b.get(i).setLabel(LABEL.get(i));
            b.get(i).setIdd(i);
        }
    }


    //change all the colors of the buttons
    public static void setColor(ArrayList<LaverieButton> L){
        int n = LABEL.size();
        for (int i=0; i<n; i++){
            switch (LABEL.get(i)){
                case "empty":
                    L.get(i).setBackgroundColor(Color.GREEN);
                    break;
                case "full":
                    L.get(i).setBackgroundColor(Color.RED);
                    break;
                case "reserved":
                    L.get(i).setBackgroundColor(Color.GRAY);
                    break;
            }
        }
    }

    //when a button is clicked
    private View.OnClickListener machineButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LaverieButton bi = (LaverieButton) view;
            String label = bi.getLabel();
            int idd = bi.getIdd();
            boolean reservation = getReserved();
            DialogFragment dialog = DialogBox.newInstance(label,idd, reservation);
            dialog.show(getFragmentManager(), "DialogBox");
        }
    };

    //to reserve a machine
    public static void makeReservation(int i){
        setChanged(i,"reserved");
        setReserved(true);
    }

    //to change the label of a button
    public static void setChanged(int i, String s){
        //pb: ne change pas la valeur de LABELi
        LABEL.set(i,s);
        b.get(i).setLabel(s);
        //On pourra plus tard changer seulement la couleur du boutton de la machine selectionnée.
        setColor(b);
    }

    public void finish(View view) {
        this.setResult(1);
        this.finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public static void setReserved(boolean reserved) {
        LingerieActivity.reserved = reserved;
    }

    public static boolean getReserved() {
        return reserved;
    }
}
