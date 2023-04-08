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
                "create table "
                        + STOCK_TABLE_NAME + "(id integer primary key, barcode text,description text,stocklevel integer,tobuy char,minstock int,lastupdate date)"
        );
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
            cursor =
                    db.rawQuery(sql, null);
        } catch (Exception e) {
            String error = e.toString();
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }


        ContentValues newValues = new ContentValues();
        newValues.put("barcode", stock.getBarcode());
        newValues.put("description", stock.getDescription());
        newValues.put("stocklevel", stock.getStockLevel());
        newValues.put("tobuy", stock.getToBuy());
        newValues.put("notes", stock.getNotes());
        newValues.put("tags", stock.getTags());

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
            String sql = String.format("Select * from stock where barcode = '%s'", barcode);
            Cursor cursor =
                    db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            Stock s = new Stock(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8)
            );
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


    public boolean readAll(Context context, ArrayList<Stock> stockList) {

        SQLiteDatabase db = getReadableDatabase();

        try {


            //     String sql = "update stock set tags = none";

            String sql = "Select * from stock where 1 order by  description  COLLATE NOCASE ASC";
            Cursor cursor =
                    db.rawQuery(sql, null);

            cursor.moveToFirst();

            stockList.clear();
            while (!cursor.isAfterLast()) {
                Stock record = new Stock(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7),
                        "tags");

                stockList.add(record);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean toggleToBuy(String barcode, Context context) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            String sql = String.format("Select tobuy from stock where barcode = '%s'", barcode);
            Cursor cursor =
                    db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                Toast.makeText(context, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return true;
            }
            cursor.moveToFirst();
            String toBuy = "";

            toBuy = cursor.getString(0);
            if (toBuy.equals("Y")) toBuy = "N";
            else toBuy = "Y";

            ContentValues newValues = new ContentValues();
            newValues.put("tobuy", toBuy);

            String[] whereArgs = {barcode};
            db.update("stock", newValues, "barcode = ?", whereArgs);

            cursor.close();
            db.close();

            if (toBuy == "Y") return true;
            else return false;
        } catch (Exception e) {
            Toast.makeText(context, "Error in toggle tobuy", Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public boolean setMeta(String mKey, String mValue) {
        return true;
    }

    public String getMeta(String mKey) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            String sql = String.format("Select * from meta where m_key = '%s'", mKey);
            Cursor cursor =
                    db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
              //  Toast.makeText(this, "NOT FOUND", Toast.LENGTH_SHORT).show();
                return null;
            }
            cursor.moveToFirst();
            String m_value =  cursor.getString(1);

            cursor.close();
            db.close();
            return m_value;
        }
    }
}



