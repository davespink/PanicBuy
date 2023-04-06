package com.example.panicbuy;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

//import com.google.ml-kit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;


/*
TODO
https://stackoverflow.com/questions/10144820/get-the-html-of-the-javascript-rendered-page-after-interacting-with-it

Pasting the following to your browser console ( F12 -> Console ) will automatically save a file called rendered.html to your downloads directory:

let link = document.createElement("a");
link.href = URL.createObjectURL(new Blob([document.getElementsByTagName('html')[0].innerHTML], { type: 'text/html' }));
link.download = "rendered.html";
link.click();
URL.revokeObjectURL(link.href);


Panic Buy:

Bug: Shop button doesn't work right. Shop mode persists when switched off.
Idea: Use color changes to indicate tags. Green or white.

Need to allow for a show to buy only.

Should we change tobuy into a number? Could indicate order qty.




 */


public class MainActivity extends AppCompatActivity {
    GmsBarcodeScanner gmsBarcodeScanner;
    ArrayList<Stock> stockList;
    StockListAdapter adapter;

    DatabaseHelper helper;

    //
    // see https://guides.codepath.com/android/Populating-a-ListView-with-a-CursorAdapter
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout bLine = (LinearLayout) findViewById(R.id.buttonLine);


        String[] tags = {"fish", "meat", "fruit", "veg", "cans"};
        Button b;

        for (int i = 0; i < tags.length; i++) {
            b = new com.google.android.material.chip.Chip(this);
            b.setText(tags[i]);

            bLine.addView(b);
        }

        ListView mListView = findViewById(R.id.userlist);
        stockList = new ArrayList<>();

        helper = new DatabaseHelper(this);
        //   setUp();
        helper.readAll(this, stockList);

        adapter = new StockListAdapter(this, R.layout.adapter_view_layout, stockList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener((adapterView, view, position, l) -> {

/*
            for(int index = 0; index < ((ViewGroup) view ).getChildCount(); index++) {
                View nextChild = ((ViewGroup) view ).getChildAt(index);
                String s = (String) ((TextView)nextChild).getText();
                String t = s;
            }

 */
            View thisChild = ((ViewGroup) view).getChildAt(1);
            String sBarcode = (String) ((TextView) thisChild).getText();
            thisChild = ((ViewGroup) view).getChildAt(2);
            String sDescription = (String) ((TextView) thisChild).getText();
            thisChild = ((ViewGroup) view).getChildAt(3);
            String sStockLevel = (String) ((TextView) thisChild).getText();

            boolean shopping = ((CheckBox) findViewById(R.id.checkBox2)).isChecked();
            //boolean shopping = true;
            if (shopping) {
                boolean bToBuy = helper.toggleToBuy(sBarcode, this);

                if (bToBuy) {
                    thisChild = ((ViewGroup) view).getChildAt(4);
                    ((TextView) thisChild).setText("Y");

                } else {
                    thisChild = ((ViewGroup) view).getChildAt(4);
                    ((TextView) thisChild).setText("N");

                }
                refreshDataset();
            }
            ((TextView) (findViewById(R.id.barcodeResultView))).setText(sBarcode);
            ((TextView) (findViewById(R.id.editTextDescription))).setText(sDescription);
            ((TextView) (findViewById(R.id.textViewStockLevel))).setText(sStockLevel);
            ((TextView) (findViewById(R.id.editTextQty))).setText("0");

        });


        refreshDataset();
    }

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    public void launchSecondActivity(View view) {
        Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, SecondActivity.class);
        TextView v = (TextView) findViewById(R.id.barcodeResultView);
        String barcode = v.getText().toString();
        if (barcode.length() > 0) {
            intent.putExtra("barcode", barcode);
            startActivity(intent);
        }
    }

    public void refreshDataset() {
        helper.readAll(this, stockList);
        adapter.notifyDataSetChanged();
    }

    public void setUp() {
        String list = "Fish, Cauliflower,Tortillas,Chicharones";
        String[] arr = list.split(",");

        int x = 0;
        for (int i = 0; i < arr.length; i++) {
            String d = "Fresh " + arr[i];
            Stock s = new Stock("1", "", d, "0", "N", "0", "","","");
            helper.update(s, this);
            x = 0;
        }


    }

    public void refreshDataset(View v) {
        refreshDataset();
    }

    public void readBarcode() {


        TextView vDisplay = findViewById(R.id.barcodeResultView);
        ImageButton bFind = (ImageButton) findViewById(R.id.button_f);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());

        gmsBarcodeScanner.startScan().addOnSuccessListener(
                (barcode) -> {
                    vDisplay.setText(barcode.getDisplayValue());
                    bFind.performClick();
                    ;
                }).addOnCanceledListener(() -> Toast.makeText(MainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show());

    }


    public void actionButton(View v) {

        String viewID = getResources().getResourceName(v.getId());
        int l = viewID.length();
        viewID = viewID.substring(l - 1, l);
        String thisKey = viewID;


        // Lets construct a stock object
        String sBarcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String sDescription = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        String sQty = ((TextView) (findViewById(R.id.editTextQty))).getText().toString();
        String sStockLevel = ((TextView) (findViewById(R.id.textViewStockLevel))).getText().toString();

        switch (thisKey) {
            case "m":
            case "p":
                int iQty = Integer.parseInt(sQty);
                if (thisKey.equals("m"))
                    iQty--;
                else iQty++;
                @SuppressLint("CutPasteId")
                TextView eQty = findViewById(R.id.editTextQty);
                eQty.setText(String.valueOf(iQty));
                break;

            case "u":
                String sNewStockLevel = String.valueOf(Integer.parseInt(sStockLevel) + Integer.parseInt(sQty));
                ((TextView) findViewById(R.id.textViewStockLevel)).setText(sNewStockLevel);

                Stock stock = new Stock("0", sBarcode, sDescription, sNewStockLevel, "0", "n", "","","");
                helper.update(stock, this);
                helper.readAll(this, stockList);
                refreshDataset();
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_LONG).show();
                break;
            case "s":
                readBarcode();
                break;

            case "f":
                Stock s = helper.findBarcode(sBarcode, this);
                if (s == null) {
                    ((TextView) (findViewById(R.id.editTextDescription))).setText("");
                    ((TextView) (findViewById(R.id.editTextQty))).setText("1");
                    ((TextView) (findViewById(R.id.textViewStockLevel))).setText("0");
                } else {
                    ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());
                    ((TextView) (findViewById(R.id.textViewStockLevel))).setText(s.getStockLevel());
                    ((TextView) (findViewById(R.id.editTextQty))).setText("1");
                }
                break;
            case "d":
                helper.deleteBarcode(sBarcode, this);
                refreshDataset();
                break;

            case "c":

                ((TextView) findViewById(R.id.barcodeResultView)).setText("");
                ((TextView) findViewById(R.id.editTextDescription)).setText("");
                ((TextView) findViewById(R.id.editTextQty)).setText("1");
                ((TextView) findViewById(R.id.textViewStockLevel)).setText("0");


                break;
            default:
                Toast.makeText(this, "Unknown button " + thisKey, Toast.LENGTH_SHORT).show();

        }

    }
}











