package com.wuyts.nik.pantry;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class MainActivity extends AppCompatActivity
        implements DetailFragment.OnToggleIsInPantryListener,
        MainFragment.OnListItemSelectedListener, MainFragment.OnSwipeLeftListener {

    public static boolean mMasterDetail = false;
    public static final String ITEM_ID_KEY = "itemId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.ll_two_pane) != null) {
            mMasterDetail = true;
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(DetailActivity.UPDATE_CURSOR_KEY)) {
            if (intent.getBooleanExtra(DetailActivity.UPDATE_CURSOR_KEY, true)) {
                updateCursor();
            }
        }
    } // end onCreate

    @Override
    public void onToggleIsInPantry(long itemId, boolean isInPantry) {
        toggleIsInPantry(itemId, isInPantry);

        // Change cursor in detail fragment
        if (mMasterDetail) {
            String selection = _ID + " = ?";
            String[] selectionArgs = {Long.toString(itemId)};
            Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
            Cursor itemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fr_detail);
            Cursor oldCursor = detailFragment.swapCursor(itemCursor);
            oldCursor.close();
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

            // Get data of pantry item
            String selection = _ID + " = ?";
            String[] selectionArgs = {Long.toString(itemId)};
            Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
            Cursor itemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
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
            //getSupportFragmentManager().beginTransaction().replace(R.id.fr_detail, detailFragment).commit();
        }
    } // end onListItemSelected

    @Override
    public void onSwipeLeft(long itemId) {
        // Get isInPantry data of pantry item
        boolean isInPantry;
        String[] projection = {COLUMN_IS_OK};
        String selection = _ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};
        Uri swipedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
        Cursor itemCursor = getContentResolver().query(swipedItem, projection, selection, selectionArgs, null);

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

    public void toggleIsInPantry(long itemId, boolean isInPantry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, !isInPantry);
        String itemIdStr = Long.toString(itemId);
        String selection = _ID + " = ?";
        String[] selectionArgs = {itemIdStr};
        Uri updateItem = CONTENT_URI.buildUpon().appendPath(itemIdStr).build();

        // Change pantry item in database
        getContentResolver().update(updateItem, values, selection, selectionArgs);

        // Change cursor in MainFragment
        updateCursor();
    } // end toggleIsInPantry

    private void updateCursor() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fr_main);
        if (mainFragment != null) {
            String[] projection = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
            Cursor newCursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);
            mainFragment.swapCursor(newCursor);
        }
    } // end updateCursor

    /*private void displayDetailFragment(long itemId) {
        DetailFragment detailFragment = new DetailFragment();

        // Get data of pantry item
        String selection = _ID + " = ?";
        String[] selectionArgs = {Long.toString(itemId)};
        Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
        Cursor itemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
        // Set pantry item data in detail fragment
        detailFragment.setItemCursor(itemCursor);

        // Display fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_detail, detailFragment).commit();
    } // end displayDetailFragment*/
}
