package com.example.panicbuy;


import static com.example.panicbuy.R.drawable.baseline_settings_48;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        Drawable myDrawable =  getDrawable(R.drawable.baseline_home_48);
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

    private static final int READ_REQUEST_CODE = 42;

   public void selectFile(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = resultData.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        String filename = getFileName(fileUri);

        String dbName = "panicData.db";
        File dbFile = getDatabasePath(dbName);
        String dbPath = dbFile.getAbsolutePath();
        File destinationFile = new File(dbPath);

        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            OutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }



    protected void onDestroy() {

        super.onDestroy();
    }
}


