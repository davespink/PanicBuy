package com.example.panicbuy;

import android.annotation.SuppressLint;
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

    private final SQLiteDatabase m_db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        m_db = getWritableDatabase();
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

        ContentValues newValues = new ContentValues();
        newValues.put("barcode", stock.getBarcode());
        newValues.put("description", stock.getDescription());
        newValues.put("qty", stock.getQty());

        String[] whereArgs = {stock.getBarcode()};

        try {
            m_db.update("stock", newValues, "barcode = ?", whereArgs);
        }
       catch (Exception e) {
            String error = e.toString();
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }


        // close database somewhere!!!


        return true;
    }

    public Stock findBarcode(String barcode, Context context) {

        String sql = String.format("Select * from stock where barcode = %s", barcode);

        try {
            Cursor cursor =
                    m_db.rawQuery(sql, null);

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

        String sql = String.format("Delete from stock where barcode = %s", barcode);

        try{
        m_db.execSQL(sql);}
        catch(Exception e)
        {
            Toast.makeText(context, "NOT deleted", Toast.LENGTH_SHORT).show();
        }

    }


    public Stock findStock(int stock_id, Context context) {


        @SuppressLint("DefaultLocale")
        String sql =
                String.format( "Select * from stock where id = %d", stock_id);

        try {
            Cursor cursor =
                    m_db.rawQuery(sql, null);

            cursor.moveToFirst();

            //     Toast.makeText(context, "Read OK " + val, Toast.LENGTH_SHORT).show();

            Stock s = new Stock(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();

            return s;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();

            return null;
        }

    }

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

        String sql = "Select * from stock where 1";

        try {
            Cursor cursor =
                    m_db.rawQuery(sql, null);

            cursor.moveToFirst();

            while (!cursor.isLast()) {
                Stock record = new Stock(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));

                stockList.add(record);
                cursor.moveToNext();

            }
            cursor.close();

            return true;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}




