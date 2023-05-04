package com.example.panicbuy;


import static com.example.panicbuy.R.drawable.baseline_settings_48;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


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

        ImageButton but  = findViewById(R.id.settings);

        Drawable myDrawable = getResources().getDrawable(R.drawable.baseline_home_48);
        but.setImageDrawable(myDrawable);

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

    public void launchActivity(View view) {


        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

    }


    protected void onDestroy() {

        super.onDestroy();
    }
}


