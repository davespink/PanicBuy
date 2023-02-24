package com.example.panicbuy;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

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
    DatabaseHelper helper;
    //
    // see https://guides.codepath.com/android/Populating-a-ListView-with-a-CursorAdapter
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView mListView = findViewById(R.id.userlist);
        ArrayList<Stock> stockList = new ArrayList<>();
        StockListAdapter adapter = new StockListAdapter(this, R.layout.adapter_view_layout, stockList);
        mListView.setAdapter(adapter);

        try {
             helper = new DatabaseHelper(this);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening database", Toast.LENGTH_SHORT).show();
        }

        helper.readAll(this, stockList);

        mListView.setOnItemClickListener((adapterView, view, position, l) -> {

            Stock s = helper.findStock(position + 1, this);
            ((TextView) (findViewById(R.id.barcodeResultView))).setText(s.getBarcode());
            ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());
        });

        //  aAdapter.notifyDataSetChanged();

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
            case "s":
                readBarcode();
                break;
            case "u":
                Stock stock = new Stock("0", sBarcode, sDescription, sQty);
                helper.update(stock, this);
                break;
            case "f":
                Stock s = helper.findBarcode(sBarcode, this);
                ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());

                break;
            case "d":
                helper.deleteBarcode(sBarcode, this);

                break;
        }
    }
}











