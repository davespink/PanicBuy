package com.example.panicbuy;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

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
        ((TextView) (findViewById(R.id.text_header))).setText(s.getDescription());

        Log.d(LOG_TAG, s.getDescription());
    }
}