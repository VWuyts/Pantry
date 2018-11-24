package com.wuyts.nik.pantry;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wuyts.nik.pantry.Utilities.DbFiller;

import static com.wuyts.nik.pantry.Data.PantryContract.Item.CONTENT_URI;
import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_IS_OK;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class MainActivity extends AppCompatActivity implements ItemAdapter.ListItemClickListener {

    private Cursor mItemsCursor;
    private ItemAdapter mItemAdapter;
    private RecyclerView mItemsRV;
    public static final String ITEM_ID_KEY = "itemId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Content provider: get pantry items
        String projection[] = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
        mItemsCursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);
        if (mItemsCursor.getCount() == 0) {
            // Add items to pantry
            DbFiller dbFiller = new DbFiller(this);
            dbFiller.addItems();
        }

        // RecyclerView
        mItemsRV = findViewById(R.id.rv_pantry_items);
        mItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mItemsRV.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mItemsCursor, this);
        mItemsRV.setAdapter(mItemAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mItemsCursor.close();
    }

    // Implement listener for RecyclerView item
    @Override
    public void onListItemClick(int itemPosition) {
        long itemID = mItemAdapter.getItemId(itemPosition);
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(ITEM_ID_KEY, itemID);
        startActivity(detailIntent);
    }
}
