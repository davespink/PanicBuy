package com.example.panicbuy;


import static android.app.PendingIntent.getActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.internal.FlowLayout;
import com.google.android.material.snackbar.Snackbar;


public class SecondActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            SecondActivity.class.getSimpleName();

    private Stock stock;
    private DatabaseHelper helper;

    public void onClick(View v) {
        TextView t = (TextView) v;

    }

    public Bundle m_Bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        m_Bundle = savedInstanceState;
        Intent intent = getIntent();
        String sBarcode = intent.getStringExtra("barcode");
        helper = new DatabaseHelper(this);
        stock = helper.findBarcode(sBarcode, this);
        setContentView(R.layout.activity_second);

        if (stock == null) {
            ((TextView) findViewById(R.id.text_header)).setText("Not found");
            return;
        }

        ((TextView) findViewById(R.id.text_header)).setText(stock.getDescription());
        ((EditText) findViewById(R.id.notes)).setText(stock.getNotes());
        String sStockTags = stock.getTags();

        //     String[] tags = {"fish", "meat", "fruit", "veg", "cans", "diary", "beer", "frozen", "soap"};
        String sTags = helper.getMeta("tags");
        String[] tags = sTags.split(",");
        Chip chip;

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        for (int i = 0; i < tags.length; i++) {
            chip = new Chip(this);
            chip.setText(tags[i]);
            chip.setChipEndPadding((float) .5);
            chip.setChipStartPadding((float) 1.5);
            //    b.setOnClickListener(SecondActivity::onClick); //wtf??
            chip.setOnClickListener(this::onClick); //wtf??
            chip.setCheckable(true);
            if (sStockTags.contains(tags[i]))
                chip.setChecked(true);

            chip.setOnLongClickListener((l) -> {
                Toast.makeText(this, "Long Press Detected!", Toast.LENGTH_SHORT).show();
                return true;
            });

            chipGroup.addView(chip);
        }
    }

    public void actionButton(View v) {
        Chip chip = new Chip(this);
        chip.setText("New");
        chip.setChipEndPadding((float) .5);
        chip.setChipStartPadding((float) 1.5);

        chip.setOnClickListener(this::onClick); //wtf??
        chip.setCheckable(true);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.addView(chip);

        chip.setOnLongClickListener((l) -> {
            Toast.makeText(this, "Long Press Detected!", Toast.LENGTH_SHORT).show();
            return true;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setView(R.layout.dialog_tags);
        // Add action buttons

        AlertDialog dialog = builder.create();
        dialog.show();
        WindowManager.LayoutParams mLayoutParams = dialog.getWindow().getAttributes();
        mLayoutParams.width = 500;
        dialog.getWindow().setAttributes(mLayoutParams);
    }

    protected void onDestroy() {
        String sNotes = ((TextView) findViewById(R.id.notes)).getText().toString();
        stock.setNotes(sNotes);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        String str = "";

        int index = 0;
        while (index < ((ViewGroup) chipGroup).getChildCount()) {
            Chip nextChip = (Chip) ((ViewGroup) chipGroup).getChildAt(index);

            if (nextChip.isChecked()) {
                str = str + (String) ((TextView) nextChip).getText() + ",";
            }
            index++;
        }

        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);

        stock.setTags(str);

        helper.update2(stock, this);
        super.onDestroy();
    }

}



