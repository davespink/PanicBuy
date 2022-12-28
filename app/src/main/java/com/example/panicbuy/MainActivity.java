package com.example.panicbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import android.widget.Toast;


import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;


import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    GmsBarcodeScanner gmsBarcodeScanner;
    private TextView barcodeResultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();
    }


    public void readBarcode(View v) {
        //      Log.d("some TAG", "a message");

        TextView vDisplay = findViewById(R.id.barcodeResultView);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());


        gmsBarcodeScanner
                .startScan()
                .addOnSuccessListener(barcode
                        -> {
                    vDisplay.setText(getSuccessfulMessage(barcode));
                    final DatabaseHelper helper = new DatabaseHelper(this);

                    helper.read(vDisplay.getText().toString());

                    if (helper.insert("1234567", "big tin", "10")) {
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "NOT Inserted", Toast.LENGTH_LONG).show();
                    }

                })
                .addOnCanceledListener(
                        () -> {
                        });

    }

    public void actionButton(View v) {

        TextView vQty = findViewById(R.id.qty);
        String currentQty = vQty.getText().toString();
        String viewID = getResources().getResourceName(v.getId());
        int l = viewID.length();
        viewID = viewID.substring(l - 1, l);

        // ViewID is the bit after the "_"
        String thisKey = viewID;
        String newQty = "";

        String erase = "e";
        String correct = "c";
        String scan = "s";
        String update = "u";

        if (thisKey.equals(scan)) {
            readBarcode(findViewById(R.id.barcodeResultView));

        }
        if (thisKey.equals(update)) {

        } else if (thisKey.equals(erase))
            newQty = "";
        else if (thisKey.equals(correct))
            if (currentQty.length() > 0)
                newQty = currentQty.substring(0, currentQty.length() - 1);
            else {
            }
        else {
            newQty = currentQty + thisKey;
        }

        vQty.setText(newQty);

    }

/*
    public OnSuccessListener<? super Barcode> someSub() {
        Context context = getApplicationContext();

     //   barcodeResultView;
        CharSequence text = "more toast please";

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        return null;

    }*/


    // https://stackoverflow.com/questions/1910892/what-is-the-difference-between-super-and-extends-in-java-generi
    // https://www.codejava.net/java-core/collections/generics-with-extends-and-super-wildcards-and-the-get-and-put-principle

    //  Understand the extends wildcards in Java Generics


    private String getSuccessfulMessage(Barcode barcode) {
        String barcodeValue =
                String.format(
                        Locale.US,
                        "My Display Value: %s\nRaw Value: %s\nFormat: %s\nValue Type: %s",
                        barcode.getDisplayValue(),
                        barcode.getRawValue(),
                        barcode.getFormat(),
                        barcode.getValueType());

        return barcode.getDisplayValue();

        // return  barcodeValue;
    }


}




