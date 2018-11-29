package com.wuyts.nik.pantry;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;


public class ShopActivity extends AppCompatActivity {
    private Cursor mCursor;
    private String mShop;
    public static final String SHOP_KEY = "shop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Get data of intent
        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra(SHOP_KEY)) {
            mShop = receivedIntent.getStringExtra(SHOP_KEY);
        }

        // Get Cursor data
        String[] projection = {_ID, COLUMN_NAME, COLUMN_NOTE};
        String selection = COLUMN_IS_OK + " = ? AND " + COLUMN_SHOP + " = ?";
        String[] selectionArgs = {"0", mShop};
        mCursor = getContentResolver().query(CONTENT_URI, projection, selection, selectionArgs, null);

        // Setup views
        ListView shopsLV = findViewById(R.id.lv_shop_items);
        TextView emptyListTV = findViewById(R.id.tv_no_shop_items);
        if (mCursor != null && mCursor.getCount() > 0) {
            // Setup ListView with SimpleCursorAdapter
            String[] fromColumns = {COLUMN_NAME, COLUMN_NOTE};
            int[] toViews = {R.id.tv_shop_item_name, R.id.tv_shop_item_note};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.pantry_item_to_shop, mCursor, fromColumns, toViews, 0);
            shopsLV.setAdapter(adapter);
            shopsLV.setVisibility(View.VISIBLE);
            emptyListTV.setVisibility(View.GONE);
        } else {
            shopsLV.setVisibility(View.GONE);
            emptyListTV.setVisibility(View.VISIBLE);
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tb_shop);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mShop);
        }
    } // end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCursor.getCount() == 0) {
            return false;
        }
        getMenuInflater().inflate(R.menu.shop_menu, menu);
        return true;
    } // end onCreateOptionsMenu

    // React to selection of items in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_all_done:
                setAllDone(mShop);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    } // end onOptionsItemSelected

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCursor.close();
    } // end onDestroy


    /* Utility function */
    private void setAllDone(String shop) {
        boolean emptyShop = TextUtils.isEmpty(shop);
        String selection = emptyShop ? null : COLUMN_SHOP + " = ?";
        String[] selectionArgs = emptyShop ? null : new String[]{shop};
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_OK, 1);
        getContentResolver().update(CONTENT_URI, values, selection, selectionArgs);

        // Go back to MainActivity
        Intent mainIntent = new Intent(ShopActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra(MainActivity.UPDATE_CURSOR_KEY, true);
        startActivity(mainIntent);
    }
}
