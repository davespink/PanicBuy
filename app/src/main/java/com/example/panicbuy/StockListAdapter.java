package com.example.panicbuy;

//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;


//import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * Created by User on 3/14/2017.
 */

public class StockListAdapter extends CursorAdapter {



    public StockListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.adapter_view_layout, parent, false);


    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template

        // Find fields to populate in inflated template
        TextView tvBarcode = (TextView) view.findViewById(R.id.textView_barcode);
        TextView tvDescription = (TextView) view.findViewById(R.id.textView_description);
        TextView tvStockLevel = (TextView) view.findViewById(R.id.textView_stocklevel);
        TextView tvToBuy = (TextView) view.findViewById(R.id.textView_toBuy);
        // Extract properties from cursor
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        String stockLevel = cursor.getString(cursor.getColumnIndexOrThrow("stocklevel"));
        String toBuy = cursor.getString(cursor.getColumnIndexOrThrow("tobuy"));

        // Populate fields with extracted properties
        tvBarcode.setText(barcode);
        tvDescription.setText(description);
        tvStockLevel.setText(stockLevel);
        tvToBuy.setText(toBuy);
/*
        Activity activity = (Activity) context;
        boolean shopping = true;
        Button b = (Button) activity.findViewById(R.id.button_def);

        if (b.getVisibility() == View.VISIBLE)
            shopping = false;
        View v = (View) tvBarcode.getParent();
        if (shopping) {
            if (toBuy.equals("Y"))
                v.setBackgroundColor(0x5F00FF00);
            else
                v.setBackgroundColor(0xFFFFFFFF);
        } else v.setBackgroundColor(0xFFFFFFFF);
*/

        View v = (View) tvBarcode.getParent();
        if (toBuy.equals("Y"))
            v.setBackgroundColor(0x5F00FF00);
        else
            v.setBackgroundColor(0xFFFFFFFF);
    }

}

























