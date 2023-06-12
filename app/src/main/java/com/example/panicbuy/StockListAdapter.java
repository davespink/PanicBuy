package com.example.panicbuy;

//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;


//import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by User on 3/14/2017.
 */

public class StockListAdapter extends CursorAdapter {

    Context m_context;

    private BuyDialog dialog;

    public StockListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

        m_context = context;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.adapter_view_layout, parent, false);


    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template

        // Find fields to populate in inflated template
        TextView tvBarcode = (TextView) view.findViewById(R.id.textView_barcode);
        TextView tvDescription = (TextView) view.findViewById(R.id.textView_description);
        TextView tvStockLevel = (TextView) view.findViewById(R.id.textView_stocklevel);
        TextView tvToBuy = (TextView) view.findViewById(R.id.textView_toBuy);
        TextView tvShop = (TextView) view.findViewById(R.id.textView_shop);


        // Extract properties from cursor
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

        String stockLevel = cursor.getString(cursor.getColumnIndexOrThrow("stocklevel"));
        String toBuy = cursor.getString(cursor.getColumnIndexOrThrow("tobuy"));

        // Populate fields with extracted properties
        tvBarcode.setText(barcode);
        tvDescription.setText(description + "  BUY " + toBuy);

        tvStockLevel.setText(stockLevel);
        tvToBuy.setText(toBuy);

        if (toBuy.equals("0"))
            view.setBackgroundColor(0xFFFFFFFF);
        else
            view.setBackgroundColor(0x5F00FF00);

        tvShop.setOnLongClickListener((l) -> {
            Toast.makeText(m_context, "Long Press Detected sla!", Toast.LENGTH_SHORT).show();

            MainActivity ma = (MainActivity) m_context;
            ma.doBuyDialog(description,barcode,toBuy);

            //   dialog = new BuyDialog(m_context, "123");
            //   dialog.show();

            //  String s = dialog.getText();

            return true;
        });


        // tvToBuy.setOnTouchListener((View v, MotionEvent e) -> {
        tvShop.setOnClickListener((View v) -> {
            View vP = (View) v.getParent();

            //   View thisChild = ((ViewGroup) vP).getChildAt(1);
            //   String sBarcode = (String) ((TextView) thisChild).getText();

            TextView vToBuy = (TextView) ((ViewGroup) vP).getChildAt(4);
            ;

            MainActivity ma = (MainActivity) m_context;


            //        if (vToBuy.getText().toString().equals("0")) {
            if (Utils.viewToInt(vToBuy) == 0) {
                vP.setBackgroundColor(0x5F00FF00);
                vToBuy.setText("1");
                ma.setCurrentItem(barcode, "1");
            } else {
                vP.setBackgroundColor(0xFFFFFFFF);
                vToBuy.setText("0");
                ma.setCurrentItem(barcode, "0");
            }

        });


    }


    public void dialogButton(View v) {


        dialog.cancel();


    }


}

























