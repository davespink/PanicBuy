package com.example.panicbuy;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            ThirdActivity.class.getSimpleName();

    private DatabaseHelper helper;
    private MyAlertDialog dialog;

    public Bundle m_Bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_Bundle = savedInstanceState;

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_third);

    }

    public void doFinish(View v){

        finish();
    }

    // String exportQuery = "SELECT * INTO OUTFILE '/path/to/export/file.csv' FIELDS TERMINATED BY ',' FROM mytable";
    public boolean downloadFile(View v) {
        helper = new DatabaseHelper(this);

        String viewID = getResources().getResourceName(v.getId());
        String[] str = viewID.split("_");
        String selectDownload = str[1];

        if (selectDownload.equals("db"))
            return helper.downloadDb();
        else if (selectDownload.equals("sql"))
            return helper.exportDbSQL();
        else if (selectDownload.equals("csv"))
            return helper.exportDbCSV();
        else {
            return false;
        }

        // TODO handle error

    }

    protected void onDestroy() {

        super.onDestroy();
    }
}


