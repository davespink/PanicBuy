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


    public boolean update(Stock stock, Context context) {

        //      String sBarcode = stock.getBarcode();
        //   Stock dummy = findBarcode(sBarcode, context);

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("Select * from stock where barcode = %s", stock.getBarcode());
        Cursor cursor = null;
        try {
            cursor =
                    db.rawQuery(sql, null);
        } catch (Exception e) {
            String error = e.toString();
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }


        ContentValues newValues = new ContentValues();
        newValues.put("barcode", stock.getBarcode());
        newValues.put("description", stock.getDescription());
        newValues.put("qty", stock.getQty());

        String[] whereArgs = {stock.getBarcode()};

        if (cursor != null) {
            try {
                if (cursor.getCount() == 0)
                    db.insert("stock", null, newValues);
                else
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
            String sql = String.format("Select * from stock where barcode = %s", barcode);
            Cursor cursor =
                    db.rawQuery(sql, null);
            cursor.moveToFirst();
            Stock s = new Stock(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
            return s;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public void deleteBarcode(String barcode, Context context) {

        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Delete from stock where barcode = %s", barcode);
            db.execSQL(sql);
        } catch (Exception e) {
            Toast.makeText(context, "NOT deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /*
            public Stock findStock ( int stock_id, Context context){
                SQLiteDatabase db = getReadableDatabase();

                @SuppressLint("DefaultLocale")
                String sql =
                        String.format("Select * from stock where id = %d", stock_id);

                try {
                    Cursor cursor =
                            db.rawQuery(sql, null);

                    cursor.moveToFirst();

                      Toast.makeText(context, "Read OK " , Toast.LENGTH_SHORT).show();

                    Stock s = new Stock(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    db.close();

                    return s;
                } catch (Exception e) {
                    Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();

                    return null;
                }


            }
    */
    public boolean delete(String barcode, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = String.format("delete from stock where barcode = '%s'", barcode);
        Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();

        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Toast.makeText(context, "NOT Deleted", Toast.LENGTH_SHORT).show();
            return false;
        }

        db.close();

        return true;
    }

    public boolean readAll(Context context, ArrayList<Stock> stockList) {

        SQLiteDatabase db = getReadableDatabase();

        try {
            String sql = "Select * from stock where 1";
            Cursor cursor =
                    db.rawQuery(sql, null);

            cursor.moveToFirst();

            stockList.clear();
            while (!cursor.isAfterLast()) {
                Stock record = new Stock(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                stockList.add(record);
                cursor.moveToNext();
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return false;
        }

    return true;
    }


}




