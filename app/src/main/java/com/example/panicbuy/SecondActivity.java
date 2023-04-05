package com.example.panicbuy;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.internal.FlowLayout;

public class SecondActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String sBarcode = intent.getStringExtra("barcode");

        DatabaseHelper helper = new DatabaseHelper(this);
        Stock s = helper.findBarcode(sBarcode, this);
        ((TextView)findViewById(R.id.text_header)).setText(s.getDescription());

        Log.d(LOG_TAG, s.getDescription());

        LinearLayout bLine = (LinearLayout) findViewById(R.id.buttonLine);
        LinearLayout bLine2 = (LinearLayout) findViewById(R.id.buttonLine2);
        LinearLayout cLine= (LinearLayout) findViewById(R.id.currentTags);

        String[] tags = {"fish", "meat", "fruit", "veg", "cans"};
        Button b;

        for (int i = 0; i < tags.length; i++) {
            b = new com.google.android.material.chip.Chip(this);
            b.setText(tags[i]);

            bLine.addView(b);
        }

      String[] tags2 = {"diary", "frozen", "soap"};


        for (int i = 0; i < tags2.length; i++) {
            b = new com.google.android.material.chip.Chip(this);
            b.setText(tags2[i]);
            bLine2.addView(b);
        }

        for (int i = 0; i < tags2.length; i++) {
            b = new com.google.android.material.chip.Chip(this);
            b.setText(tags2[i]);

            b.setOnClickListener(  (v)->{
                TextView t = (TextView)v;
                String str = t.getText().toString();

            } );


            cLine.addView(b);
        }
    }
}


