package com.wuyts.nik.pantry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_INDEX_SUMMARY_SUM;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.SUMMARY_URI;

public class PantryWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PantryRemoteViewsFactory(this.getApplicationContext());
    }
}

class PantryRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private Cursor mCursor;

    public PantryRemoteViewsFactory(Context context) {
        mContext = context;
    } // end constructor

    @Override
    public void onCreate() {
        mCursor = getData();

    } // end onCreate

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = getData();
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
        int noItems = mCursor.getInt(COLUMN_INDEX_SUMMARY_SUM);
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
    private Cursor getData() {
        // Revert to process' identity to be able to work with content provider
        final long identityToken = Binder.clearCallingIdentity();

        Cursor cursor;
        String selection = COLUMN_IS_OK + " = ?";
        String[] selectionArgs = {"0"};
        String sortOrder = COLUMN_SHOP + " ASC";
        cursor =  mContext.getContentResolver().query(SUMMARY_URI, null, selection, selectionArgs, sortOrder);

        // Restore the identity
        Binder.restoreCallingIdentity(identityToken);

        return cursor;
    } // end getData
}
