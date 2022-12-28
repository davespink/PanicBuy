package com.example.panicbuy;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "panicData";
    public static final String STOCK_TABLE_NAME = "stock";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ STOCK_TABLE_NAME +"(id integer primary key, barcode text,description text,qty integer)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+STOCK_TABLE_NAME);
        onCreate(db);
    }
    public boolean insert(String barcode, String description,String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("barcode", barcode);
        contentValues.put("description", description);
        contentValues.put("qty", qty);
        db.insert(STOCK_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean read(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();


     //   ContentValues contentValues = new ContentValues();
      //  contentValues.put("barcode", barcode);

        Cursor cursor  = db.rawQuery("SELECT * FROM " + STOCK_TABLE_NAME + " WHERE 'barcode' = barcode " , null);

        return true;
    }

}




