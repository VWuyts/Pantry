package com.wuyts.nik.pantry;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public class PantryWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PantryRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class PantryRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private static final Uri GROUP_BY_URI = CONTENT_URI.buildUpon().appendPath(Integer.toString(1000)).build();

    public PantryRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    } // end constructor

    @Override
    public void onCreate() {
        mCursor = getData(mContext);

    } // end onCreate

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = getData(mContext);
    } // end onDataSetChanged

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    } // end onDestroy

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    } // end getCount

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        // Construct a remote views item based on the pantry_app_widget_item xml file
        String shop = mCursor.getString(mCursor.getColumnIndex(COLUMN_SHOP));
        int noItems = mCursor.getInt(1);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.pantry_app_widget_item);
        views.setTextViewText(R.id.aw_tv_shop, shop);
        views.setTextViewText(R.id.aw_tv_no_items, Integer.toString(noItems));

        // Set fill intent for handling click events on items
        Intent fillIntent = new Intent();
        fillIntent.putExtra(ShopActivity.SHOP_KEY, shop);
        views.setOnClickFillInIntent(R.id.aw_ll_widget_item, fillIntent);

        return views;
    } // end getViewAt

    @Override
    public RemoteViews getLoadingView() {
        return null;
    } // end getLoadingView

    @Override
    public int getViewTypeCount() {
        return 1;
    } // end getViewTypeCount

    @Override
    public long getItemId(int position) {
        return position;
    } // end getItemId

    @Override
    public boolean hasStableIds() {
        return true;
    } // end hasStableIds

    /* Utility function */
    private Cursor getData(Context context) {
        // Revert to process' identity to be able to work with content provider
        final long identityToken = Binder.clearCallingIdentity();

        Cursor cursor;
        String selection = COLUMN_IS_OK + " = ?";
        String[] selectionArgs = {"0"};
        String sortOrder = COLUMN_SHOP + " ASC, " + _ID + " ASC";
        cursor =  context.getContentResolver().query(GROUP_BY_URI, null, selection, selectionArgs, sortOrder);

        // Restore the identity
        Binder.restoreCallingIdentity(identityToken);

        return cursor;
    } // end getData
}
