package com.example.wizbank.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    public static final String  DB_NAME= "fb_mei_bank";
    public static final int  DB_VERSION=1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate: started");
        String createUserTable = "Create TABLE users (_id INTEGER  PRIMARY KEY AUTOINCREMENT , email  TEXT NOT NULL  , password TEXT NOT NULL , first_name TEXT , last_name TEXT , address TEXT , image_url TEXT , remained_amount DOUBLE);";

        String createShoppingTable = "CREATE TABLE shopping(_id INTEGER PRIMARY KEY AUTOINCREMENT , item_id INTEGER , user_id INTEGER , transction_id INTEGER , price DOUBLE , date Date , description TEXT )";


        String createInvestmentTable = "CREATE TABLE investments(_id INTEGER PRIMARY KEY AUTOINCREMENT , amount DOUBLE , monthly_roi DOUBLE , name TEXT , init_date DATE , finish_data DATE , user_id INTEGER , transaction_id INTEGER)";

        String createLoansTable = "CREATE TABLE loans(_id INTEGER PRIMARY KEY AUTOINCREMENT , init_date DATE, finish_date Date , init_amount DOUBLE  ,remained_amount DOUBLE , monthly_payment DOUBLE , monthly_roi DOUBLE" +
                "" +
                ",name TEXT , user_id INTEGER )" ;

        String createTransactionTable = "create table transctions (_id integer primary key autoincrement , amount DOUBLE , date DATE , type text , user_id integer, recipient text , description text );";

        String createItemsTable ="create table items (_id integer primary key autoincrement , name text , image_url text , description text);";

        db.execSQL(createUserTable);
        db.execSQL(createShoppingTable);
        db.execSQL(createInvestmentTable);
        db.execSQL(createLoansTable);
        db.execSQL(createTransactionTable);
        db.execSQL(createItemsTable);

        addIntialItems(db);

    }

    private void addIntialItems(SQLiteDatabase db){
        Log.d(TAG, "addIntialItems: started");
        ContentValues values = new ContentValues();
        values.put("name"  , "Bike");
        values.put("image_url" , "https://cdn-images.cure.fit/www-curefit-com/image/upload/fl_progressive,f_auto,q_auto:eco,w_500,ar_3:4,c_fill/dpr_2/cultgear-content/q1S4DwudvD6D86TUcuSikyUZ");
        values.put("description" , "the perfect mountain bike");
        db.insert("items" , null , values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
