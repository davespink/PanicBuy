package com.example.panicbuy;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import android.content.Intent;
import android.database.Cursor;

import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;


import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.Toast;


//import com.google.ml-kit.vision.barcode.common.Barcode;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;


import java.util.List;


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

1. Bit more work on toolbar
3. Load database. Change name to .db
4. Help screen.
5. Splash screen.
6. Allow many databases.





 */


public class MainActivity extends AppCompatActivity {
    GmsBarcodeScanner gmsBarcodeScanner;

    StockListAdapter adapter;

    String sFilter;

    private BuyDialog dialog;
//    private TagDialog dialog;
    DatabaseHelper helper;
    Cursor cursor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }

        setContentView(R.layout.activity_main);


        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, id) ->
                refreshDataset());


        Chip chip;

        sFilter = "";

        helper = new DatabaseHelper(this);

        String str = "All," + helper.getMeta("tags");
        String[] tags = str.split(",");

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setSingleLine(true);
        for (String tag : tags) {
            chip = new Chip(this);
            chip.setText(tag);
            chip.setChipEndPadding((float) .5);
            chip.setChipStartPadding((float) 1.5);

            chip.setCheckable(true);

            chipGroup.addView(chip);
        }

        // setOnCheckedChangeListener
        chipGroup.setOnCheckedStateChangeListener((ChipGroup group, List<Integer> checkedId) -> {

            Log.d(LOG_TAG, "Chip clicked now!");

            int index = 0;
            this.sFilter = "";
            while (index < group.getChildCount()) {
                Chip nextChip = (Chip) group.getChildAt(index);

                if (nextChip.isChecked()) {
                    sFilter = sFilter + nextChip.getText() + ",";
                }
                index++;
            }
            if (sFilter.length() > 0) sFilter = sFilter.substring(0, sFilter.length() - 1);

            refreshDataset();

        });


        cursor = helper.readAll(this, null);

        adapter = new StockListAdapter(this, cursor);
        ListView mListView = findViewById(R.id.stocklist);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener((adapterView, view, position, l) -> {

            View thisChild = ((ViewGroup) view).getChildAt(1);
            String sBarcode = (String) ((TextView) thisChild).getText();

            setCurrentItem(sBarcode,null);

            refreshDataset(); //ToDo needed????
        });

        (findViewById(R.id.button_c)).setOnLongClickListener((l) -> {

            Toast.makeText(this, "Long Press Detected! buttons", Toast.LENGTH_SHORT).show();


            return true;

        });



    }

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public void doBuyDialog(String description) {
        dialog = new BuyDialog(this, description);
        dialog.show();
    }


    public void dialogButton(View v) {

        dialog.cancel();



    }


    public void setCurrentItem(String barcode,String toBuy) {
        Stock s = helper.findBarcode(barcode, this);
        ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());
        ((TextView) (findViewById(R.id.barcodeResultView))).setText(barcode);
        ((TextView) (findViewById(R.id.editTextDescription))).setText(s.getDescription());
        ((TextView) (findViewById(R.id.textViewStockLevel))).setText(s.getStockLevel());
        ((TextView) (findViewById(R.id.editTextQty))).setText("0");
        if(toBuy!=null){
            helper.persistToBuy(barcode,this,toBuy);
            refreshDataset();
        }
    }

    public void finishProcess(View v) {

        finishAffinity();

    }

    public void launchSecondActivity(View view) {
        //  Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, SecondActivity.class);
        TextView v = findViewById(R.id.barcodeResultView);
        String barcode = v.getText().toString();
        if (barcode.length() > 0) {
            intent.putExtra("barcode", barcode);
            startActivity(intent);
        }
    }

    public void launchActivity(View view) {
        //  Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, ThirdActivity.class);

        startActivity(intent);

    }

    public void refreshDataset() {
        cursor = helper.readAll(this, sFilter);
        adapter.changeCursor(cursor);
    }

    public void setUp() {
        String list = "Fish, Cauliflower,Tortillas,Chicharones";
        String[] arr = list.split(",");


        for (String value : arr) {
            String d = "Fresh " + value;
            Stock s = new Stock("1", "", d, "0", "N", "0", "", "", "");
            helper.update(s, this);

        }


    }

    public void readBarcode() {
// unused
        TextView vDisplay = findViewById(R.id.barcodeResultView);
        //  ImageButton bFind =   findViewById(R.id.button_list);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());

        gmsBarcodeScanner.startScan().addOnSuccessListener((barcode) -> {
            vDisplay.setText(barcode.getDisplayValue());


        }).addOnCanceledListener(() -> Toast.makeText(MainActivity.this,
                "Operation cancelled", Toast.LENGTH_LONG).show()).addOnFailureListener((e) -> Toast.makeText(MainActivity.this,
                "Operation failed" + e, Toast.LENGTH_LONG).show());

    }


    public void actionButton(View v) {

        String viewID = getResources().getResourceName(v.getId());
        String[] str = viewID.split("_");
        String thisKey = str[1];

        // Lets construct a stock object
        String sBarcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String sDescription = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        if (sDescription.length() == 0) sDescription = "New";
        String sQty = ((TextView) (findViewById(R.id.editTextQty))).getText().toString();
        String sStockLevel = ((TextView) (findViewById(R.id.textViewStockLevel))).getText().toString();

        switch (thisKey) {
            case "l": {
                // here we only need to deal with shopping cart


                break;
            }
            case "m":
            case "p":
                int iQty = Integer.parseInt(sQty);
                if (thisKey.equals("m")) iQty--;
                else iQty++;
                @SuppressLint("CutPasteId") TextView eQty = findViewById(R.id.editTextQty);
                eQty.setText(String.valueOf(iQty));
                break;

            case "u":
                String sNewStockLevel = String.valueOf(Integer.parseInt(sStockLevel) + Integer.parseInt(sQty));
                ((TextView) findViewById(R.id.textViewStockLevel)).setText(sNewStockLevel);

                Stock stock = new Stock("0", sBarcode, sDescription, sNewStockLevel, "n", "0", "", "", "");
                helper.update(stock, this);

                refreshDataset();
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_LONG).show();

                // In case its a new one....
                //      String st = stock.getBarcode();
                ((TextView) findViewById(R.id.barcodeResultView)).setText(stock.getBarcode());
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
                findViewById(R.id.button_c).performClick();
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















