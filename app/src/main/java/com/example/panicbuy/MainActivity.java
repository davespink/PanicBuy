package com.example.panicbuy;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Button;

import android.widget.Toast;

//import com.google.ml-kit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    GmsBarcodeScanner gmsBarcodeScanner;
    private TextView barcodeResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView mListView;
        ArrayAdapter aAdapter;


        ArrayList<String> users = new ArrayList<String>(
                Arrays.asList(
                        "Mr Suresh Dasari", "Rohini Alavalas", "Hamsika Yemineni Cricket",
                        "Mr Suresh Dasari", "Rohini Alavala", "Hamsika Yemineni Cricket"

                ));


        DatabaseHelper helper = new DatabaseHelper(this);
        helper.readAll(this, users);

        mListView = (ListView) findViewById(R.id.userlist);
        aAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        mListView.setAdapter(aAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value = aAdapter.getItem(position).toString();
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void readBarcode() {
        //      Log.d("some TAG", "a message");

        TextView vDisplay = findViewById(R.id.barcodeResultView);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());

        //  vDisplay.setText(getSuccessfulMessage(barcode);
        gmsBarcodeScanner.startScan().addOnSuccessListener((barcode) -> {

                    DatabaseHelper helper;

                    vDisplay.setText(barcode.getDisplayValue());
                    try {
                        helper = new DatabaseHelper(this);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "NOT Inserted", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (helper.insert(vDisplay.getText().toString(), "new tin", "10")) {
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "NOT Inserted", Toast.LENGTH_LONG).show();
                    }


                    if (helper.read(vDisplay.getText().toString(), this)) {
                        Toast.makeText(MainActivity.this, "Read main OK", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "NOT Read main ;-(", Toast.LENGTH_LONG).show();
                    }

                }


        ).addOnCanceledListener(() -> {
        });

    }


    public void actionButton(View v) {

        TextView vQty = findViewById(R.id.textViewQty);
        String currentQty = vQty.getText().toString();
        String viewID = getResources().getResourceName(v.getId());
        int l = viewID.length();
        viewID = viewID.substring(l - 1, l);

        // ViewID is the bit after the "_"
        String thisKey = viewID;

        Toast.makeText(MainActivity.this, "Click" + thisKey, Toast.LENGTH_LONG).show();


        switch (thisKey) {

            case "m":

                break;
            case "p":

                break;
            case "s":
                readBarcode();
                break;
            case "u":

                break;
            case "f":

                break;
            case "d":

                break;
        }


    }
}








