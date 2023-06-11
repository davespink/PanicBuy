package com.example.panicbuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class BuyDialog extends AlertDialog {
    private String mDescription;

    protected BuyDialog(Context context, String description) {
        super(context);
        mDescription = description;
    }


    @Override
    public void show() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_buy, null);

        TextView textView = view.findViewById(R.id.stock_desc);
        textView.setText(mDescription);

        // ImageButton iButton = view.findViewById(R.id.button_edit);


        //  iButton.setOnClickListener((View v) -> {
        //      mText = "Result";
        //       super.cancel();
        //     });

        setView(view);

        super.show();
    }

    @Override
    public void cancel() {
        //      TextView textView = findViewById(R.id.tagname);
        //    mText = textView.getText().toString();

        super.cancel();
    }

    public String getText() {
        return mDescription;
    }


}
