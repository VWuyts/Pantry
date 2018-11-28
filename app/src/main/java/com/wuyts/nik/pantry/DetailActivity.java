package com.wuyts.nik.pantry;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnToggleIsInPantryListener {

    public static final String UPDATE_CURSOR_KEY = "update cursor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null & intent.hasExtra(MainActivity.ITEM_ID_KEY)) {
            long itemId = intent.getLongExtra(MainActivity.ITEM_ID_KEY, 0);
            // Get data of pantry item
            String selection = _ID + " = ?";
            String[] selectionArgs = {Long.toString(itemId)};
            Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemId)).build();
            Cursor itemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_detail);
            detailFragment.setItemCursor(itemCursor);
            // Display fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_detail, detailFragment).commit();
        }
    }

    @Override
    public void onToggleIsInPantry(long itemId, boolean isInPantry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, !isInPantry);
        String itemIdStr = Long.toString(itemId);
        String selection = _ID + " = ?";
        String[] selectionsArgs = {itemIdStr};
        Uri updateItem = CONTENT_URI.buildUpon().appendPath(itemIdStr).build();

        // Change pantry item in database
        getContentResolver().update(updateItem, values, selection, selectionsArgs);

        // Change cursor main activity
        Intent mainIntent = new Intent(DetailActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra(UPDATE_CURSOR_KEY, true);
        startActivity(mainIntent);

        // Remove DetailActivity from stack
        //finish();
    }
}
