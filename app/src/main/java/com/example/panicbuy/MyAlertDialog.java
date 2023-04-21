package com.example.panicbuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public class MyAlertDialog extends AlertDialog {
    private String mText;

    protected MyAlertDialog(Context context, String text) {
        super(context);
        mText = text;
    }

    @Override
    public void show() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_tags, null);

        TextView textView = view.findViewById(R.id.tagname);
        textView.setText(mText);

        setView(view);

        super.show();
    }

    @Override
    public void cancel() {
        TextView textView = findViewById(R.id.tagname);
        mText = textView.getText().toString();

        super.cancel();
    }

    public String getText() {
        return mText;
    }


}
