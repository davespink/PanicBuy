package com.example.panicbuy;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import java.util.ArrayList;
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

Bug: Shop button doesn't work right. Shop mode persists when switched off.
Idea: Use color changes to indicate tags. Green or white.

Need to allow for a show to buy only.

Should we change tobuy into a number? Could indicate order qty.


Idea: Create a meta table in the database. Use to store possible tags.
key + data pair.


Some code to get all view maybe debugger addon
  ContentFrameLayout x = (ContentFrameLayout) findViewById(android.R.id.content);
        LinearLayoutCompat y = (LinearLayoutCompat) x.getChildAt(0);
        LinearLayout layout = (LinearLayout) y.getChildAt(0);

        //  LinearLayout layout = findViewById(R.id.myLayout);

        int count = layout.getChildCount();
        int j = 0;
        View v = null;
        String idString;

        for (int i = 0; i < count; i++) {
            v = layout.getChildAt(i);

            j = ((ViewGroup) v).getChildCount();
            if (j > 1) {

                for (int k = 0; k < j; k++) {
                    View c = ((ViewGroup) v).getChildAt(k);
                    int id = v.getId();
                    if (id > 0)
                        idString = v.getResources().getResourceEntryName(id);
                    int m = ((ViewGroup) c).getChildCount();


                    }

                }

            }





 */


public class MainActivity extends AppCompatActivity {
    GmsBarcodeScanner gmsBarcodeScanner;
    ArrayList<Stock> stockList;
    StockListAdapter adapter;

    String sFilter;

    String listState = "DEF";

    DatabaseHelper helper;
    Cursor cursor;

    //
    // see https://guides.codepath.com/android/Populating-a-ListView-with-a-CursorAdapter
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_main);


        Chip chip;

        sFilter = new String("");

        helper = new DatabaseHelper(this);

        String str = "All," + helper.getMeta("tags");
        String[] tags = str.split(",");

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.setSingleLine(true);
        for (int i = 0; i < tags.length; i++) {
            chip = new Chip(this);
            chip.setText(tags[i]);
            chip.setChipEndPadding((float) .5);
            chip.setChipStartPadding((float) 1.5);

            chip.setCheckable(true);

            //    b.setOnClickListener(SecondActivity::onClick); //wtf??
            //     chip.setOnClickListener(this::onClick); //wtf??
            //    chip.setCheckable(true);
            //   if (sTags.contains(tags[i]))
            //       chip.setChecked(true);
            chipGroup.addView(chip);
        }

        // setOnCheckedChangeListener
        chipGroup.setOnCheckedStateChangeListener((ChipGroup group, List<Integer> checkedId) -> {

            Log.d(LOG_TAG, "Chip clicked now!");

            int index = 0;
            this.sFilter = "";
            while (index < ((ViewGroup) group).getChildCount()) {
                Chip nextChip = (Chip) ((ViewGroup) group).getChildAt(index);

                if (nextChip.isChecked()) {
                    sFilter = sFilter + (String) ((TextView) nextChip).getText() + ",";
                }
                index++;
            }
            if (sFilter.length() > 0) sFilter = sFilter.substring(0, sFilter.length() - 1);

            refreshDataset();

        });

        //   setUp();

        cursor = helper.readAll(this, null);

        adapter = new StockListAdapter(this, cursor);
        ListView mListView = findViewById(R.id.stocklist);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener((adapterView, view, position, l) -> {

            View thisChild = ((ViewGroup) view).getChildAt(1);
            String sBarcode = (String) ((TextView) thisChild).getText();
            thisChild = ((ViewGroup) view).getChildAt(2);
            String sDescription = (String) ((TextView) thisChild).getText();
            thisChild = ((ViewGroup) view).getChildAt(3);
            String sStockLevel = (String) ((TextView) thisChild).getText();

            //  boolean shopping = ((CheckBox) findViewById(R.id.checkBox2)).isChecked();
            //    boolean shopping = false;

            if (listState.equals("SHOP") || listState.equals("LIST")) {
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


            refreshDataset();
        });

        (findViewById(R.id.button_c)).setOnLongClickListener((l) -> {

            Toast.makeText(this, "Long Press Detected!", Toast.LENGTH_SHORT).show();
            return true;

        });

    }

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    public void finishProcess(View v) {

        finishAffinity();

    }

    public void launchSecondActivity(View view) {
        //  Log.d(LOG_TAG, "Button clicked!");

        Intent intent = new Intent(this, SecondActivity.class);
        TextView v = (TextView) findViewById(R.id.barcodeResultView);
        String barcode = v.getText().toString();
        if (barcode.length() > 0) {
            intent.putExtra("barcode", barcode);
            startActivity(intent);
        }
    }

    public void launchThirdActivity(View view) {
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

        int x = 0;
        for (int i = 0; i < arr.length; i++) {
            String d = "Fresh " + arr[i];
            Stock s = new Stock("1", "", d, "0", "N", "0", "", "", "");
            helper.update(s, this);
            x = 0;
        }


    }


    public void readBarcode() {


        TextView vDisplay = findViewById(R.id.barcodeResultView);
        ImageButton bFind = (ImageButton) findViewById(R.id.button_f);

        GmsBarcodeScannerOptions.Builder optionsBuilder = new GmsBarcodeScannerOptions.Builder();

        optionsBuilder.allowManualInput();

        gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build());

        gmsBarcodeScanner.startScan().addOnSuccessListener((barcode) -> {
            vDisplay.setText(barcode.getDisplayValue());
            bFind.performClick();
            ;
        }).addOnCanceledListener(() -> Toast.makeText(MainActivity.this, "Operation cancelled", Toast.LENGTH_LONG).show());

    }



    public void actionButton(View v) {

        String viewID = getResources().getResourceName(v.getId());
        String[] str = viewID.split("_");
        String thisKey = str[1];

        // Lets construct a stock object
        String sBarcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String sDescription = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        String sQty = ((TextView) (findViewById(R.id.editTextQty))).getText().toString();
        String sStockLevel = ((TextView) (findViewById(R.id.textViewStockLevel))).getText().toString();

        switch (thisKey) {
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
                String st = stock.getBarcode();
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
                ((Button) (findViewById(R.id.button_c))).performClick();
                break;

            case "c":

                ((TextView) findViewById(R.id.barcodeResultView)).setText("");
                ((TextView) findViewById(R.id.editTextDescription)).setText("");
                ((TextView) findViewById(R.id.editTextQty)).setText("1");
                ((TextView) findViewById(R.id.textViewStockLevel)).setText("0");
                break;


            case "def":
            case "list":
            case "shop":

                triState(thisKey);

                break;


            default:
                Toast.makeText(this, "Unknown button " + thisKey, Toast.LENGTH_SHORT).show();

        }

    }


    private void triState(String bString) {
        Button bDef = (Button) findViewById(R.id.button_def);
        Button bList = (Button) findViewById(R.id.button_list);
        Button bShop = (Button) findViewById(R.id.button_shop);

        bDef.setVisibility(View.GONE);
        bList.setVisibility(View.GONE);
        bShop.setVisibility(View.GONE);

        if (bString.equals("def")) {
            bList.setVisibility(View.VISIBLE);
            listState = "LIST";
        }
        if (bString.equals("list")) {
            bShop.setVisibility(View.VISIBLE);
            listState = "SHOP";
        }
        if (bString.equals("shop")) {
            bDef.setVisibility(View.VISIBLE);
            listState = "DEF";
        }


        refreshDataset();

    }
}











