package com.wuyts.nik.pantry;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;

import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public class PantryIntentService extends IntentService {
    public static final String ACTION_ALL_DONE = "com.wuyts.nik.pantry.action.ALL_DONE";


    public PantryIntentService() {
        super("PantryIntentService");
    } // end constructor

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ALL_DONE.equals(action)) {
                handleActionAllDone();
            }
        }
    } // end onHandleIntent


    /* Utility functions */

    private void handleActionAllDone() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, true);
        getContentResolver().update(CONTENT_URI, values, null, null);
        updateWidget();
    } // end handleActionAllDone

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PantryAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.aw_lv_shops);
    } // end updateWidget
}
