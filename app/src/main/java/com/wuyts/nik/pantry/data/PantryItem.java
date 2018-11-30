package com.wuyts.nik.pantry.data;

import android.database.Cursor;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_CATEGORY;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;

public class PantryItem {
    private final long id;
    private final String name;
    private final String category;
    private final String shop;
    private final String note;
    private final boolean isInPantry;

    private PantryItem(long id, String name, String category, String shop, String note,
                       boolean isInPantry) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.shop = shop;
        this.note = note;
        this.isInPantry = isInPantry;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getShop() {
        return shop;
    }

    public String getNote() {
        return note;
    }

    public boolean isInPantry() {
        return isInPantry;
    }

    public static PantryItem fromCursorMain(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        long id = cursor.getLong(cursor.getColumnIndex(_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String shop = cursor.getString(cursor.getColumnIndex(COLUMN_SHOP));
        boolean isInPantry = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_OK)) > 0;

        return new PantryItem(id, name, "", shop, "", isInPantry);
    } // end fromCursorMain

    public static PantryItem fromCursorDetail(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        long id = cursor.getLong(cursor.getColumnIndex(_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
        String shop = cursor.getString(cursor.getColumnIndex(COLUMN_SHOP));
        String note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
        boolean isInPantry = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_OK)) > 0;

        return new PantryItem(id, name, category, shop, note, isInPantry);
    } // end fromCursorDetail
}
