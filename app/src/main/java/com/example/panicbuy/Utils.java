package com.example.panicbuy;

import android.view.View;
import android.widget.TextView;

public class Utils {

    public static int viewToInt(View v) {
        String s = ((TextView) v).getText().toString();
        return Integer.parseInt(s);
    }

    public static View intToView() {
        return null;
    }
}
