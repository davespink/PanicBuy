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

        setContentView(R.layout.activity_third);

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

    /*
    public void downloadFile(View v) {


        String sourcePath = "/data/data/com.example.panicbuy/databases/panicData"; // Path of file in app folder
        String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/panicData.db";
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        try {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            Toast.makeText(this, "File downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();


        }
    }
*/
    protected void onDestroy() {

        super.onDestroy();
    }
}


