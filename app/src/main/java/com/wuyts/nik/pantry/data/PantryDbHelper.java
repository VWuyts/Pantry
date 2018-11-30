package com.wuyts.nik.pantry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDbHelper extends SQLiteOpenHelper {
    // Database name and version
    public static final String DATABASE_NAME = "pantry.db";
    public static final int DATABASE_VERSION = 1;

    // SQL statement to create table item
    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE " + PantryContract.Item.TABLE_NAME + " (" +
                    PantryContract.Item._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PantryContract.Item.COLUMN_NAME + " TEXT NOT NULL, " +
                    PantryContract.Item.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                    PantryContract.Item.COLUMN_SHOP + " TEXT, " +
                    PantryContract.Item.COLUMN_NOTE + " TEXT, " +
                    PantryContract.Item.COLUMN_IS_OK + " INTEGER NOT NULL DEFAULT 1)";

    // SQL statement to delete table item
    private static final String SQL_DELETE_ITEM = "DROP TABLE IF EXISTS " +
            PantryContract.Item.TABLE_NAME;

    // Constructor
    PantryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        db.execSQL(SQL_CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: implement onUpgrade according to the upgrade policy

        // During development
        db.execSQL(SQL_DELETE_ITEM);
        onCreate(db);
    }
}
