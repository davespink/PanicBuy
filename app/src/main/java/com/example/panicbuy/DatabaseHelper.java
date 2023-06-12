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
import android.widget.RadioButton;
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
    public static final String DATABASE_NAME = "panicData.db";
    public static final String STOCK_TABLE_NAME = "stock";

    public static final String META_TABLE_NAME = "meta";

    public static final int EXPORT_TYPE_SQL = 1;

    public static final int EXPORT_TYPE_CSV = 2;
    private Context m_context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        m_context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STOCK_TABLE_NAME + "(_id integer primary key, barcode text,description text,stocklevel integer,tobuy char,minstock int,lastupdate date,notes,tags)");

        String strSQL =
                "INSERT INTO " + STOCK_TABLE_NAME + " VALUES('10','722345','Manzanas','2','0','0','2023-03-22 15:02:48','Add a note','Walmart,Fruit')\n";
        db.execSQL(strSQL);
        strSQL = "INSERT INTO " + STOCK_TABLE_NAME + " VALUES('11','12345','Agua Mineral','12','0','0','2023-03-22 15:02:48','Good for you','Drinks,Walmart')\n";
        db.execSQL(strSQL);

        db.execSQL("create table " + META_TABLE_NAME + "(m_key primary key, m_value text)");
        strSQL = "INSERT INTO meta VALUES('tags','Drinks,Fruit,Veg,Meat,Frozen,Costco,Walmart')";
        db.execSQL(strSQL);
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
            RadioButton shopping = (RadioButton) activity.findViewById(R.id.shopping);
            if (shopping.isChecked())
                sWhere.append(" AND tobuy > ").append("\"0").append("\"");


            String sql = "Select * from stock where " + sWhere + " order by  description  COLLATE NOCASE ASC";

            cursor = db.rawQuery(sql, null);

            cursor.moveToFirst();

        } catch (
                Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return null;
        }

        return cursor;
    }

    public boolean persistToBuy(Context context,String barcode, String toBuy) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Select tobuy from stock where barcode = '%s'", barcode);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return true;
            }
            cursor.moveToFirst();

            ContentValues newValues = new ContentValues();
            newValues.put("tobuy", toBuy);

            String[] whereArgs = {barcode};
            db.update("stock", newValues, "barcode = ?", whereArgs);

            cursor.close();
            db.close();

            return !toBuy.equals("0");
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
        String sourcePath = "/data/data/com.example.panicbuy/databases/panicData.db"; // Path of file in app folder
        String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/panicData.db";
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);
 //  TODO deal with overwriting existing file
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
            Toast.makeText(m_context, "File downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(m_context, "Failed to download file", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    // TODO escape commas etc
// TODO  extract this process to return SQL or CSV output.
    public boolean exportDbSQL() {
        return exportDb(EXPORT_TYPE_SQL);
    }

    public boolean exportDbCSV() {
        return exportDb(EXPORT_TYPE_CSV);
    }

    public boolean exportDb(int exportType) {

        String sFormat = "";
        String sOutputFile = "";

        if (exportType == EXPORT_TYPE_SQL) {
            sFormat = "INSERT INTO stock VALUES(%s)%n";
            sOutputFile = "/panicDataDump.sql";
        } else if (exportType == EXPORT_TYPE_CSV) {
            sFormat = "%s%n";
            sOutputFile = "/panicDataDump.csv";
        }

        String s = getDumpData();
        String[] sBuff = s.split("\0");

        String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + sOutputFile;
        File destinationFile = new File(destinationPath);

        try {
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer;


            for (int i = 0; i < sBuff.length; i++) {
                String sOutput = String.format(sFormat, sBuff[i]);
                buffer = sOutput.getBytes();
                outputStream.write(buffer, 0, sOutput.length());
            }

            outputStream.close();
            Toast.makeText(m_context, "File downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(m_context, "Failed to download file", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public String getDumpData() {
        String values = "";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;

        String sql = "Select * from Stock where 1";
        int c = 0;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            c = cursor.getColumnCount();
            String val;
            int rCount = 0;
            while (!cursor.isAfterLast()) {
                for (int i = 0; i < c - 1; i++) {
                    val = cursor.getString(i);
                    if (val == null)
                        val = "";
                    if (i == 0)
                        values = values + "'" + String.valueOf(rCount) + "'";

                    values = values + "," + "'" + val + "'";

                }
                values = values + '\0';
                cursor.moveToNext();
                rCount++;
            }
        } catch (Exception e) {
            Toast.makeText(m_context, "NOT Read", Toast.LENGTH_SHORT).show();
            return null;
        }

        return values;
    }


}










