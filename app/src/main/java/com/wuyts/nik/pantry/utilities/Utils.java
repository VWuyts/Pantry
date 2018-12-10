package com.wuyts.nik.pantry.utilities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wuyts.nik.pantry.MainActivity;
import com.wuyts.nik.pantry.PantryAppWidget;
import com.wuyts.nik.pantry.R;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public final class Utils {
    public static Cursor getPantryItem(@NonNull Context context, long itemId, String[] projection) {
        String selection = _ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};
        Uri requiredItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
        return context.getContentResolver().query(requiredItem, projection, selection, selectionArgs, null);
    } // end getPantryItem

    public static void goToMainActivity(Context context, boolean updateCursor) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (updateCursor) {
            mainIntent.putExtra(MainActivity.UPDATE_CURSOR_KEY, true);
        }
        context.startActivity(mainIntent);
    } // end goToMainActivity

    public static void toggleIsInPantry(@NonNull Context context, long itemId, boolean isInPantry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, !isInPantry);
        String itemIdStr = Long.toString(itemId);
        String selection = _ID + " = ?";
        String[] selectionArgs = {itemIdStr};
        Uri updateItem = CONTENT_URI.buildUpon().appendPath(itemIdStr).build();
        context.getContentResolver().update(updateItem, values, selection, selectionArgs);
    } // end toggleIsInPantry

    public static void setAllDoneInDb(@NonNull Context context, String[] selectionArgs) {
        String selection = selectionArgs == null ? null : COLUMN_SHOP + " = ?";
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, true);
        context.getContentResolver().update(CONTENT_URI, values, selection, selectionArgs);
    } // end setAllDoneInDb

     public static void updateWidget(@NonNull Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, PantryAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.aw_lv_shops);
    } // end updateWidget
}
