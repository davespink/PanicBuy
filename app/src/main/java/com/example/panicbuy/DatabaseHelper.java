package com.example.panicbuy;

import android.content.ContentValues;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.TextView;
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

    // public boolean update(String barcode, String description, String qty, Context context) {
    public boolean update(Stock stock) {

/*
        String barcode = ((TextView) (findViewById(R.id.barcodeResultView))).getText().toString();
        String description = ((TextView) (findViewById(R.id.editTextDescription))).getText().toString();
        EditText eQty = (EditText) findViewById(R.id.editTextQty);
        String qty = (eQty.getText()).toString();


        String sql = String.format("INSERT OR UPDATE stock (barcode,description,qty) VALUES('%s','%s',%s)",
                stock.getBarcode(),stock.getDescription(),stock.getQty());


        ContentValues newValues = new ContentValues();
            newValues.put("barcode", barcode);
            newValues.put("description", description);
            newValues.put("qty", qty);

            m_db.insert("stock", null, newValues);
            Toast.makeText(context, "Updated" + sql, Toast.LENGTH_SHORT).show();
*/
        ContentValues newValues = new ContentValues();
        newValues.put("barcode", stock.getBarcode());
        newValues.put("description", stock.getDescription());
        newValues.put("qty", stock.getQty());

        //     String sql = String.format("INSERT OR UPDATE stock (barcode,description,qty) VALUES('%s','%s',%s)",
        //             stock.getBarcode(),stock.getDescription(),stock.getQty());
        try {
            //    m_db.rawQuery(sql, null);
            m_db.insert("stock", null, newValues);

        } catch (Exception e) {

            String error = e.toString();

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

            String description = cursor.getString(2);

            //     Toast.makeText(context, "Read OK " + val, Toast.LENGTH_SHORT).show();

            cursor.close();

            Stock s = new Stock("1", "1234", description, "10");

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

        String sql = String.format("Select * from stock where 1");
        Toast.makeText(context, "Reading " + sql, Toast.LENGTH_SHORT).show();

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




