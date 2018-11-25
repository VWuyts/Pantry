package com.wuyts.nik.pantry.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public final class PantryContract {
    public static final String AUTHORITY = "com.wuyts.nik.pantry.provider";
    public static final String ITEM_PATH = "item";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private PantryContract() {}

    // inner class to define table PantryItem
    public static class Item implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(ITEM_PATH).build();
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SHOP = "shop";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_IS_OK = "isInPantry";
    }
}
