package com.example.alexa.myapplication;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by berenger on 17/02/18.
 */

/**new class that extends AppCompactButton to add attributes to our buttons*/
public class LaverieButton extends AppCompatButton {
    //new attributes
    private String label;
    private int idd;

    //constructor
    public LaverieButton (Context context, AttributeSet attrs) {
        super(context,attrs);

    }

    //getters and setters
    public String getLabel() {
        return label;
    }

    public int getIdd() {
        return idd;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }
}
