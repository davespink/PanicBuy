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

    private SQLiteDatabase m_db;

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

    public boolean update(String barcode, String description, String qty, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = String.format("Insert into stock (barcode,description,qty) values('%s','%s',%s)", barcode, description, qty);
        //    Toast.makeText(context, "Update " + sql, Toast.LENGTH_SHORT).show();
        try {

            ContentValues newValues = new ContentValues();
            newValues.put("barcode", barcode);
            newValues.put("description", description);
            newValues.put("qty", qty);

            db.insert("stock", null, newValues);
            Toast.makeText(context, "Updated" + sql, Toast.LENGTH_SHORT).show();

        } catch (Error e) {
            Toast.makeText(context, "Some error" + sql, Toast.LENGTH_SHORT).show();
        }

        // close database somewhere!!!
        db.close();

        return true;
    }

    public Stock findBarcode(String barcode, Context context) {

        String sql = String.format("Select * from stock where barcode = %s", barcode);

        try {
            Cursor cursor =
                    m_db.rawQuery(sql, null);

            cursor.moveToFirst();

            String description = cursor.getString(2);

       //     Toast.makeText(context, "Read OK " + val, Toast.LENGTH_SHORT).show();

            cursor.close();

            Stock s = new Stock("1","1234",description,"10");

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

     //   m_db  = this.getWritableDatabase();

        //   MainActivity mainActivity = new MainActivity();

        String sql = String.format("Select * from stock where 1");
        Toast.makeText(context, "Reading " + sql, Toast.LENGTH_SHORT).show();

        try {
            Cursor cursor =
                   m_db.rawQuery(sql, null);

            cursor.moveToFirst();

            String val;

            int i = 0;
            while (!cursor.isLast()) {
        //        val = cursor.getString(0)  + '-' + cursor.getString(1) + '-' + cursor.getString(2) + '-' + cursor.getString(3);
                //     Toast.makeText(context, "Read OK " + i + val, Toast.LENGTH_SHORT).show();

           //     stockList.add(val);

                Stock record = new Stock(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),cursor.getString(3));

                stockList.add(record);

                cursor.moveToNext();
                i++;
            }
            cursor.close();

            return true;
        } catch (Exception e) {
            Toast.makeText(context, "NOT Read", Toast.LENGTH_SHORT).show();
            return false;
        }


    }
}




