package com.example.panicbuy;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;

//import com.google.ml-kit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;


/*

https://stackoverflow.com/questions/10144820/get-the-html-of-the-javascript-rendered-page-after-interacting-with-it


Pasting the following to your browser console ( F12 -> Console ) will automatically save a file called rendered.html to your downloads directory:

let link = document.createElement("a");
link.href = URL.createObjectURL(new Blob([document.getElementsByTagName('html')[0].innerHTML], { type: 'text/html' }));
link.download = "rendered.html";
link.click();
URL.revokeObjectURL(link.href);

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

        ListView mListView = findViewById(R.id.userlist);
        stockList = new ArrayList<>();

        helper = new DatabaseHelper(this);
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
            String sQty = (String) ((TextView) thisChild).getText();


            ((TextView) (findViewById(R.id.barcodeResultView))).setText(sBarcode);
            ((TextView) (findViewById(R.id.editTextDescription))).setText(sDescription);
            ((TextView) (findViewById(R.id.textViewStockLevel))).setText(sQty);


        });

        /*
        Stock s = new Stock("1", "1", "one", "1");
        helper.update(s, this);

        s.setBarcode("2");
        s.setDescription("two");
        s.setQty("2");
        helper.update(s, this);

        s.setBarcode("3");
        s.setDescription("three");
        s.setQty("3");
        helper.update(s, this);

        s.setBarcode("4");
        s.setDescription("four");
        s.setQty("4");
        helper.update(s, this);

        s.setBarcode("5");
        s.setDescription("five");
        s.setQty("5");
        helper.update(s, this);
*/


        refreshDataset();
    }

    public void refreshDataset() {
        helper.readAll(this, stockList);
        adapter.notifyDataSetChanged();
    }

    public void readBarcode() {
        //      Log.d("some TAG", "a message");

        TextView vDisplay = findViewById(R.id.barcodeResultView);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());
        gmsBarcodeScanner.startScan().addOnSuccessListener((barcode) -> vDisplay.setText(barcode.getDisplayValue())
        ).addOnCanceledListener(() -> Toast.makeText(MainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show());

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
                Stock stock = new Stock("0", sBarcode, sDescription, sQty);
                helper.update(stock, this);
                helper.readAll(this, stockList);
                refreshDataset();
                break;
            case "s":
                readBarcode();
                break;

            case "f":
                Stock s = helper.findBarcode(sBarcode, this);
                if (s == null) {
                    ((TextView) (findViewById(R.id.editTextDescription))).setText("");
                    ((TextView) (findViewById(R.id.editTextQty))).setText("1");
                } else {
                    ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());
                    ((TextView) (findViewById(R.id.editTextQty))).setText(s.getQty());
                }
                break;
            case "d":
                helper.deleteBarcode(sBarcode, this);
                refreshDataset();
                break;


        }


    }
}











