package com.example.panicbuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "panicData";
    public static final String STOCK_TABLE_NAME = "stock";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + STOCK_TABLE_NAME + "(id integer primary key, barcode text,description text,qty integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STOCK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(String barcode, String description, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("barcode", barcode);
        contentValues.put("description", description);
        contentValues.put("qty", qty);
        db.insert(STOCK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean read(String barcode, Context context) {
        SQLiteDatabase db = this.getReadableDatabase();

        //   MainActivity mainActivity = new MainActivity();

        String sql = String.format("Select * from stock where barcode = %s", barcode);
        Toast.makeText(context, "Reading " + sql, Toast.LENGTH_LONG).show();

        try {
            Cursor cursor =
                    db.rawQuery(sql, null);

            //   String col = ((Integer) cursor.getColumnCount()).toString();
            //   String[] s = cursor.getColumnNames();

            cursor.moveToFirst();
            String val = cursor.getString(2);
            Toast.makeText(context, "Read OK " + val, Toast.LENGTH_LONG).show();

            cursor.close();

            return true;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_LONG).show();
            return false;
        }


    }


    public boolean readAll(Context context, ArrayList<String> festivals) {

        SQLiteDatabase db = this.getReadableDatabase();

        //   MainActivity mainActivity = new MainActivity();

        String sql = String.format("Select * from stock where 1");
        Toast.makeText(context, "Reading " + sql, Toast.LENGTH_LONG).show();

        try {
            Cursor cursor =
                    db.rawQuery(sql, null);

            //   String col = ((Integer) cursor.getColumnCount()).toString();
            //   String[] s = cursor.getColumnNames();

            cursor.moveToFirst();

            String val;

            int i = 0;
            while (!cursor.isLast()) {
                val = cursor.getString(2);
                //     Toast.makeText(context, "Read OK " + i + val, Toast.LENGTH_SHORT).show();

                festivals.add(val);

                cursor.moveToNext();
                i++;
            }
            cursor.close();

            return true;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_LONG).show();
            return false;
        }


    }
}




