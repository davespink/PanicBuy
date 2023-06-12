package com.example.panicbuy;

import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class BuyDialog extends AlertDialog {
    DatabaseHelper mHelper;

    Context mContext;
    private String mDescription;
    private String mBarcode;
    private String mQty;

    protected BuyDialog(Context context,DatabaseHelper helper,  String description, String barcode, String qty) {
        super(context);
        mContext = context;
        mHelper = helper;
        mDescription = description;
        mBarcode = barcode;
        mQty = qty;
    }


    @Override
    public void show() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_buy, null);

        TextView tvDescription = view.findViewById(R.id.stock_desc);
        tvDescription.setText(mDescription);

        TextView tvQty = view.findViewById(R.id.stock_qty);
        tvQty.setText(mQty);

        ImageButton bEdit = view.findViewById(R.id.button_edit);
        ImageButton bCancel = view.findViewById(R.id.button_cancel);

        bCancel.setOnClickListener((View v) -> {

            super.cancel();
        });

        bEdit.setOnClickListener((View v) -> {
           // int qty = Utils.viewToInt(tvQty);

            mHelper.persistToBuy(mContext,mBarcode,tvQty.getText().toString());
            MainActivity ma = (MainActivity) mContext;
            ma.refreshDataset();
            super.cancel();
        });

        setView(view);

        super.show();
    }



    @Override
    public void cancel() {
        //      TextView textView = findViewById(R.id.tagname);
        //    mText = textView.getText().toString();

        super.cancel();
    }


}
