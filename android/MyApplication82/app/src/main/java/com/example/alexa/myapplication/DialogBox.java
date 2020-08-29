package com.example.alexa.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;

/**
 * Created by Zoé on 04/02/2018.
 */

public class DialogBox extends DialogFragment {
    /*public static int idd;
    public static int reserved;

    *//** create a new instance of DialogBox with the label, the number of the machine
     * and reserved as an argument *//*
    public static DialogBox newInstance(String label, int i, int reservation){
        DialogBox dialogBox = new DialogBox();

        // Supply the arguments
        Bundle args = new Bundle();
        args.putInt("numéro",i);
        args.putString("label",label);
        args.putInt("reservation",reservation);
        dialogBox.setArguments(args);

        return dialogBox;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //variables
        idd = getArguments().getInt("numéro");
        String label = getArguments().getString("label");
        reserved = getArguments().getInt("reservation");

        //general
        AlertDialog.Builder box = new AlertDialog.Builder(getActivity());
        box.setCancelable(true);

        //Box message depending on the label
        switch (label){
            case "empty":
                box.setMessage("Voulez vous réserver cette machine ?");
                box.setTitle("Machine "+(idd+1));
                box.setPositiveButton("Réserver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(reserved==0) {
                            LingerieActivity.makeReservation(idd);
                        }
                        else {
                            Toast.makeText(getActivity(), "Vous avez déjà reservé une machine !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                box.setNegativeButton("Annuler",okButton);
                break;
            case "full":
                box.setMessage("Machine en cours d'utilisation.");
                box.setTitle("Machine "+(idd+1));
                box.setNeutralButton("OK",okButton);
                break;
            case "reserved":
                box.setMessage("Cette machine est déjà réservée, veuillez en sélectionner une autre.");
                box.setTitle("Machine "+(idd+1));
                box.setNeutralButton("OK",okButton);
                break;
            default:
                box.setMessage("error");
                box.setTitle("Machine"+label);
                box.setNeutralButton("OK",okButton);
        }

        return box.create();
    }

    private DialogInterface.OnClickListener okButton = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };*/
}
