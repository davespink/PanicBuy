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
    private MyAlertDialog dialog;

    private ChipGroup chipGroup;

    public void dialogButton(View v) {

        String oldTag = dialog.getText();
        Chip thisChip = null;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            String str = chip.getText().toString();
            if (oldTag.equals(str)) {
                thisChip = chip;
                break;
            }
        }

        dialog.cancel();
        String newTag = dialog.getText();

        String viewID = getResources().getResourceName(v.getId());
        String[] str = viewID.split("_");
        String action = str[1];
        if (action.equals("edit")) {
            thisChip.setText(newTag);

        } else if ((action.equals("delete"))) {
            chipGroup.removeView(thisChip);
        }


    }

    public Bundle m_Bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_Bundle = savedInstanceState;

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }


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

        String sTags = helper.getMeta("tags");
        String[] tags = sTags.split(",");
        Chip chip;

        chipGroup = findViewById(R.id.chipGroup);

        chipGroup.setChipSpacingVerticalResource(R.dimen.space);

        for (int i = 0; i < tags.length; i++) {
            chip = new Chip(this);
            chip.setText(tags[i]);
            chip.setChipEndPadding((float) 1.0);
            chip.setChipStartPadding((float) 1.0);
            //    b.setOnClickListener(SecondActivity::onClick); //wtf??
            //  chip.setOnClickListener(this::onClick); //wtf??
            chip.setCheckable(true);
            if (sStockTags.contains(tags[i]))
                chip.setChecked(true);

            chip.setOnLongClickListener((l) -> {
                TextView v = (TextView) l;
                String sTag = v.getText().toString();

                //    chipGroup.removeView(l);


                dialog = new MyAlertDialog(this, sTag);
                dialog.show();

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

       //     chip.setOnClickListener(this::onClick); //wtf??


        chip.setCheckable(true);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);
        chipGroup.addView(chip);

        chip.setOnLongClickListener((l) -> {
            Toast.makeText(this, "Long Press Detected!", Toast.LENGTH_SHORT).show();
            return true;
        });


    }


    public void launchActivity(View view) {


        Intent intent = new Intent(this, ThirdActivity.class);

        startActivity(intent);

    }

    protected void onDestroy() {
        String sNotes = ((TextView) findViewById(R.id.notes)).getText().toString();
        stock.setNotes(sNotes);

        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        String str = "";
        String strMeta = "";

        int index = 0;
        while (index < ((ViewGroup) chipGroup).getChildCount()) {
            Chip nextChip = (Chip) ((ViewGroup) chipGroup).getChildAt(index);

            if (nextChip.isChecked()) {
                str = str + (String) ((TextView) nextChip).getText() + ",";
            }
            strMeta = strMeta + (String) ((TextView) nextChip).getText() + ",";
            // Remove trailing ,
            index++;
        }

        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        if (strMeta.length() > 0)
            strMeta = strMeta.substring(0, strMeta.length() - 1);

        stock.setTags(str);
        helper.update2(stock, this);

        // persist possible tags
        helper.setMeta(this, "tags", strMeta);

        super.onDestroy();
    }

}



