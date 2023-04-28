package com.example.panicbuy;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ThirdActivity extends AppCompatActivity {


    public Bundle m_Bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_Bundle = savedInstanceState;

        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

        setContentView(R.layout.activity_third);

    }

    public void doFinish(View v){

        finish();
    }

    // String exportQuery = "SELECT * INTO OUTFILE '/path/to/export/file.csv' FIELDS TERMINATED BY ',' FROM mytable";
    public boolean downloadFile(View v) {
        DatabaseHelper helper = new DatabaseHelper(this);

        String viewID = getResources().getResourceName(v.getId());
        String[] str = viewID.split("_");
        String selectDownload = str[1];

        switch (selectDownload) {
            case "db":
                return helper.downloadDb();
            case "sql":
                return helper.exportDbSQL();
            case "csv":
                return helper.exportDbCSV();
            default:
                return false;
        }

        // TODO handle error

    }

    protected void onDestroy() {

        super.onDestroy();
    }
}


