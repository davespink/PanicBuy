package com.example.panicbuy;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

//import com.google.ml-kit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GmsBarcodeScanner gmsBarcodeScanner;

    //
    // see https://guides.codepath.com/android/Populating-a-ListView-with-a-CursorAdapter
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mListView = (ListView) findViewById(R.id.userlist);
        ArrayList<Stock> stockList = new ArrayList<>();
        StockListAdapter adapter = new StockListAdapter(this, R.layout.adapter_view_layout, stockList);
        mListView.setAdapter(adapter);

        DatabaseHelper helper = new DatabaseHelper(this);
        helper.readAll(this, stockList);

        mListView.setOnItemClickListener((adapterView, view, position, l) -> {
            String value = adapter.getItem(position).toString();

            Integer i = position;
            Toast.makeText(getApplicationContext(), String.format("Clicked %s", i.toString()), Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

        });

        //  aAdapter.notifyDataSetChanged();

    }

    public void readBarcode() {
        //      Log.d("some TAG", "a message");

        TextView vDisplay = findViewById(R.id.barcodeResultView);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());
        gmsBarcodeScanner.startScan().addOnSuccessListener((barcode) -> {

                    //       DatabaseHelper helper;

                    vDisplay.setText(barcode.getDisplayValue());

    /*
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
                    } */

                }


        ).addOnCanceledListener(() -> {
            Toast.makeText(MainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show();

        });

    }


    public void actionButton(View v) {

        TextView vQty = findViewById(R.id.textViewQty);
     //   String currentQty = vQty.getText().toString();
        String viewID = getResources().getResourceName(v.getId());
        int l = viewID.length();
        viewID = viewID.substring(l - 1, l);
        String thisKey = viewID;

        DatabaseHelper helper;
        try {
            helper = new DatabaseHelper(this);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to access database", Toast.LENGTH_LONG).show();
            return;
        }

        // Lets construct a stock object
        String sBarcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String sDescription = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        String sQty = ((TextView)(findViewById(R.id.editTextQty))).getText().toString();

        switch (thisKey) {
            case "m":
            case "p":
                int iQty = Integer.parseInt(sQty);
                if(thisKey.equals("m"))
                iQty--;else iQty++;
                TextView eQty = (TextView)(findViewById(R.id.editTextQty));
                eQty.setText(String.valueOf(iQty));
                break;
            case "s":
                readBarcode();
                break;
            case "u":
                Stock stock = new Stock("0",sBarcode,sDescription,sQty);
                helper.update(stock);
                break;
            case "f":
   //             if (helper.read(sBarcode, this)) {
      //              Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_LONG).show();
       //         } else {
         //           Toast.makeText(MainActivity.this, "NOT Inserted", Toast.LENGTH_LONG).show();
                }
              //  break;

            //case "d":
          //      if (helper.delete(sBarcode, this)) {
          //          Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
           //     } else {
            //        Toast.makeText(MainActivity.this, "NOT Deleted", Toast.LENGTH_SHORT).show();
            //    }
             //   break;
        }


    }








