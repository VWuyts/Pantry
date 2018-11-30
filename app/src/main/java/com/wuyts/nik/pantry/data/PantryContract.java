package com.wuyts.nik.pantry.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class PantryContract {
    static final String AUTHORITY = "com.wuyts.nik.pantry.provider";
    static final String ITEM_PATH = "item";
    static final String SUMMARY_PATH = "summary";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private PantryContract() {}

    // inner class to define table PantryItem
    public static class Item implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(ITEM_PATH).build();
        public static final Uri SUMMARY_URI =
                CONTENT_URI.buildUpon().appendPath(SUMMARY_PATH).build();
        public static final int COLUMN_INDEX_SUMMARY_SUM = 1;
        static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SHOP = "shop";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_IS_OK = "isInPantry";
    }
}
