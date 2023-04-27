package com.example.panicbuy;


import android.app.Activity;
import android.content.ContentValues;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "panicData";
    public static final String STOCK_TABLE_NAME = "stock";

    public static final String META_TABLE_NAME = "meta";
    private Context m_context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        m_context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STOCK_TABLE_NAME + "(id integer primary key, barcode text,description text,stocklevel integer,tobuy char,minstock int,lastupdate date)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STOCK_TABLE_NAME);
        onCreate(db);
    }


    public boolean update(Stock stock, Context context) {

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("Select * from stock where barcode = '%s'", stock.getBarcode());
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            String error = e.toString();
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }


        ContentValues newValues = new ContentValues();
        newValues.put("barcode", stock.getBarcode());
        newValues.put("description", stock.getDescription());
        newValues.put("stocklevel", stock.getStockLevel());


        String[] whereArgs = {stock.getBarcode()};

        if (cursor != null) {
            try {
                if (cursor.getCount() == 0) {
                    newValues.put("tobuy", stock.getToBuy());
                    newValues.put("notes", stock.getNotes());
                    newValues.put("tags", stock.getTags());
                    db.insert("stock", null, newValues);
                } else db.update("stock", newValues, "barcode = ?", whereArgs);
            } catch (Exception e) {
                String error = e.toString();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
            db.close();
            return true;
        }
        return false;
    }

    public boolean update2(Stock stock, Context context) {

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("Select * from stock where barcode = '%s'", stock.getBarcode());
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            String error = e.toString();
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }

        ContentValues newValues = new ContentValues();
        newValues.put("notes", stock.getNotes());
        newValues.put("tags", stock.getTags());


        String[] whereArgs = {stock.getBarcode()};

        if (cursor != null) {
            try {
                db.update("stock", newValues, "barcode = ?", whereArgs);
            } catch (Exception e) {
                String error = e.toString();
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
            db.close();
            return true;
        }
        return false;
    }

    public Stock findBarcode(String barcode, Context context) {

        try (SQLiteDatabase db = getReadableDatabase()) {
            String sql = String.format("Select * from stock where barcode = '%s'", barcode);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            Stock s = new Stock(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            cursor.close();
            db.close();
            return s;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public void deleteBarcode(String barcode, Context context) {

        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Delete from stock where barcode = '%s'", barcode);
            db.execSQL(sql);
        } catch (Exception e) {
            Toast.makeText(context, "NOT deleted", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor readAll(Context context, String sFilter) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        try {

            StringBuilder sWhere = new StringBuilder("1");
            String sOr = "";

            // is "All" in the filter

// TODO Dont rely on the string. Call with a null is better.
            if (sFilter != null) {
                if (sFilter.length() > 0 && !sFilter.contains("All")) {
                    sWhere = new StringBuilder();
                    String[] sTags = sFilter.split(",");
                    for (String sTag : sTags) {
                        sWhere.append(sOr).append(" tags like  \"%").append(sTag).append("%\" ");

                        sOr = " OR ";
                    }

                }
            }

            Activity activity = (Activity) context;
            Button b = (Button) activity.findViewById(R.id.button_shop);
            if (b.getVisibility() == View.VISIBLE) {
                sWhere.append(" AND tobuy = ").append("\"Y").append("\"");
            }

            String sql = "Select * from stock where " + sWhere + " order by  description  COLLATE NOCASE ASC";

            cursor = db.rawQuery(sql, null);

            cursor.moveToFirst();

        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return null;
        }

        return cursor;
    }

    public boolean toggleToBuy(String barcode, Context context) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Select tobuy from stock where barcode = '%s'", barcode);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return true;
            }
            cursor.moveToFirst();
            String toBuy;

            toBuy = cursor.getString(0);
            if (toBuy.equals("Y")) toBuy = "N";
            else toBuy = "Y";

            ContentValues newValues = new ContentValues();
            newValues.put("tobuy", toBuy);

            String[] whereArgs = {barcode};
            db.update("stock", newValues, "barcode = ?", whereArgs);

            cursor.close();
            db.close();

            return toBuy.equals("Y");
        } catch (Exception e) {
            Toast.makeText(context, "Error in toggle tobuy", Toast.LENGTH_SHORT).show();
            return true;
        }

    }


    public String getMeta(String mKey) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            String sql = String.format("Select * from " + META_TABLE_NAME + " where m_key = '%s'", mKey);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                //  Toast.makeText(this, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            String m_value = cursor.getString(1);

            cursor.close();
            db.close();
            return m_value;
        }
    }

    public boolean setMeta(Context context, String mKey, String data) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Update %s set m_value = '%s' where m_key = '%s'", META_TABLE_NAME, data, mKey);

            db.execSQL(sql);

            db.close();

            //     String value = getMeta("tags");

            return true;
        } catch (Exception e) {

            return false;
        }

    }


    public boolean downloadDb() {
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
            //   Toast.makeText(this, "File downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //  Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show();


        }
        return true;
    }

// TODO escape commas etc
// TODO  extract this process to return SQL or CSV output.
    public boolean exportDbSQL() {
        String values = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        String sql = "Select * from Stock where 1";
        int c = 0;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            c = cursor.getColumnCount();
            while (!cursor.isAfterLast()) {
                for (int i = 0; i < c - 1; i++)
                    if (i < c - 2)
                        values = values + cursor.getString(i) + ",";
                    else
                        values = values + cursor.getString(i) + "LF";

                cursor.moveToNext();
            }
        } catch (Exception e) {
            Toast.makeText(m_context, "NOT Read", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public boolean exportDbCSV() {
        return true;
    }


}










