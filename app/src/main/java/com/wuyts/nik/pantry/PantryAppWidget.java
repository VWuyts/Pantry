package com.wuyts.nik.pantry;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class PantryAppWidget extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Set up the intent that starts the PantryWidgetService
        Intent intent = new Intent(context, PantryWidgetService.class);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pantry_app_widget);
        views.setTextViewText(R.id.aw_tv_title, context.getString(R.string.aw_title));
        views.setRemoteAdapter(R.id.aw_lv_shops, intent);
        views.setEmptyView(R.id.aw_lv_shops, R.id.aw_tv_empty_view);

        // Set up intents to handle click events
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
        views.setOnClickPendingIntent(R.id.aw_tv_title, mainPendingIntent);
        views.setOnClickPendingIntent(R.id.aw_tv_overview_label, mainPendingIntent);
        Intent allDoneIntent = new Intent(context,PantryIntentService.class);
        allDoneIntent.setAction(PantryIntentService.ACTION_ALL_DONE);
        PendingIntent allDonePendingIntent = PendingIntent.getService(context, 0, allDoneIntent, 0);
        views.setOnClickPendingIntent(R.id.aw_btn_all_done, allDonePendingIntent);

        // React to clicks on list item
        Intent clickIntentTemplate = new Intent(context, ShopActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.aw_lv_shops, clickPendingIntentTemplate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    } // end updateAppWidget

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    } // end onUpdate


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    } // end onDisabled 
}
