package com.example.panicbuy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
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



        //     DatabaseHelper helper = new DatabaseHelper(this);
        //    helper.readAll(this, products);

        //Create the Stock objects
        Stock pen = new Stock("1","1111111","Nice Pen Black","10");
        Stock beans = new Stock("2","22222","Nice Heinz beans","20");
        Stock tuna = new Stock("3","33333","Fishy fish","30");
        Stock soap = new Stock("4","4444","Soft soap","40");


        //Add the Stock objects to an ArrayList
        ArrayList<Stock> stockList = new ArrayList<>();
        stockList.add(pen);
        stockList.add(beans);
        stockList.add(tuna);
        stockList.add(soap);

        ListView mListView = (ListView) findViewById(R.id.userlist);


        StockListAdapter adapter = new StockListAdapter(this, R.layout.adapter_view_layout, stockList);
        mListView.setAdapter(adapter);


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
        String currentQty = vQty.getText().toString();
        String viewID = getResources().getResourceName(v.getId());
        int l = viewID.length();
        viewID = viewID.substring(l - 1, l);

        // ViewID is the bit after the "_"
        String thisKey = viewID;

        //    Toast.makeText(MainActivity.this, "Click" + thisKey, Toast.LENGTH_LONG).show();

        DatabaseHelper helper;
        try {
            helper = new DatabaseHelper(this);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to access database", Toast.LENGTH_LONG).show();
            return;
        }

        String sBarcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String sDescription = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        //String sBarcode = tv.getText().toString();

        //String sBarcode = ((TextView)findViewById(R.id.barcodeResultView).

        //String sDescription = findViewById(R.id.editTextDescription).toString();

        switch (thisKey) {

            case "m":

                break;
            case "p":

                break;
            case "s":
                readBarcode();
                break;
            case "u":
                helper.update(sBarcode, sDescription, "10", this);
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








