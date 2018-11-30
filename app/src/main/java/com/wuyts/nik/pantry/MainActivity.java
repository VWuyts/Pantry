package com.wuyts.nik.pantry;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements DetailFragment.OnToggleIsInPantryListener,
        MainFragment.OnListItemSelectedListener, MainFragment.OnSwipeLeftListener {

    private ArrayList<String> mShopList;
    private static boolean mMasterDetail = false;
    public static final String ITEM_ID_KEY = "itemId";
    public static final String UPDATE_CURSOR_KEY = "update cursor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.ll_two_pane) != null) {
            mMasterDetail = true;
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(UPDATE_CURSOR_KEY)) {
            if (intent.getBooleanExtra(UPDATE_CURSOR_KEY, true)) {
                updateMainCursor();
            }
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
    } // end onCreate

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShopList.clear();
    } // end onDestroy

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get shop data
        String[] projection = {"DISTINCT " + COLUMN_SHOP};
        String sortOrder = COLUMN_SHOP + " ASC";
        Cursor shopCursor = getContentResolver().query(CONTENT_URI, projection, null, null, sortOrder);

        // No menu shown if no shops in cursor
        if (shopCursor == null || shopCursor.getCount() == 0) {
            return false;
        }

        // Add shop data to ArrayList
        mShopList = new ArrayList<>();
        while (shopCursor.moveToNext()) {
            mShopList.add(shopCursor.getString(shopCursor.getColumnIndex(COLUMN_SHOP)));
        }
        shopCursor.close();

        // Add different shops to menu
        int counter = -1;
        for (String shop : mShopList) {
            menu.add(Menu.NONE, ++counter, menu.NONE, getResources().getString(R.string.go_to_shop) + " " + shop)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        return true;
    } // end onCreateOptionsMenu

    // React to selection of items in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if (menuItemId < mShopList.size()) {
            String shop = mShopList.get(menuItemId);
            Intent shopIntent = new Intent(MainActivity.this, ShopActivity.class);
            shopIntent.putExtra(ShopActivity.SHOP_KEY, shop);
            startActivity(shopIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    } // end onOptionsItemSelected

    @Override
    public void onToggleIsInPantry(long itemId, boolean isInPantry) {
        toggleIsInPantry(itemId, isInPantry);

        // Change cursor in detail fragment
        if (mMasterDetail) {
            updateDetailCursor(itemId);
        }
    } // end onToggleIsInPantry

    @Override
    public void onListItemSelected(long itemId) {
        if (!mMasterDetail) {
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra(ITEM_ID_KEY, itemId);
            startActivity(detailIntent);
        } else {
            DetailFragment detailFragment = new DetailFragment();

            // Set data of pantry item
            Cursor itemCursor = getPantryItem(itemId, null);
            detailFragment.setItemCursor(itemCursor);

            // Display fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fr_detail, detailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    } // end onListItemSelected

    @Override
    public void onSwipeLeft(long itemId) {
        // Get isInPantry data of pantry item
        boolean isInPantry;
        String[] projection = {COLUMN_IS_OK};
        Cursor itemCursor = getPantryItem(itemId, projection);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            isInPantry = itemCursor.getInt(itemCursor.getColumnIndex(COLUMN_IS_OK)) > 0;
            itemCursor.close();
            toggleIsInPantry(itemId, isInPantry);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    } // end onSwipeLeft


    /* Utility functions */

    private Cursor getPantryItem(long itemId, String[] projection) {
        String selection = _ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};
        Uri requiredItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
        return getContentResolver().query(requiredItem, projection, selection, selectionArgs, null);
    } // end getPantryItem

    private void toggleIsInPantry(long itemId, boolean isInPantry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, !isInPantry);
        String itemIdStr = Long.toString(itemId);
        String selection = _ID + " = ?";
        String[] selectionArgs = {itemIdStr};
        Uri updateItem = CONTENT_URI.buildUpon().appendPath(itemIdStr).build();

        // Change pantry item in database
        getContentResolver().update(updateItem, values, selection, selectionArgs);

        // Update main fragment and widget data
        updateMainCursor();
        updateWidget();
    } // end toggleIsInPantry

    private void updateDetailCursor(long itemId) {
        String selection = _ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};
        Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
        Cursor itemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fr_detail);
        if (detailFragment != null) {
            Cursor oldCursor = detailFragment.swapCursor(itemCursor);
            oldCursor.close();
        }
    } // end updateDetailCursor

    private void updateMainCursor() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fr_main);
        if (mainFragment != null) {
            String[] projection = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
            Cursor newCursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);
            mainFragment.swapCursor(newCursor);
        }
    } // end updateMainCursor

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, PantryAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.aw_lv_shops);
    } // end updateWidget
}
