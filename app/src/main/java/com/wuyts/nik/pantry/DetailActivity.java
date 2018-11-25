package com.wuyts.nik.pantry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.wuyts.nik.pantry.data.PantryItem;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class DetailActivity extends AppCompatActivity {

    private Cursor mItemCursor;
    private long mItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.ITEM_ID_KEY)) {
            mItemID = intent.getLongExtra(MainActivity.ITEM_ID_KEY, 0);
        }

        // Retrieve views
        TextView nameTV = findViewById(R.id.tv_item_name);
        ImageView inPantryIV = findViewById(R.id.iv_item_in_pantry);
        TextView categoryTV = findViewById(R.id.tv_item_category);
        TextView shopTV = findViewById(R.id.tv_item_shop);
        TextView noteTV = findViewById(R.id.tv_item_note);

        // Get data of pantry item
        String selection = _ID + " = ?";
        String selectionArgs[] = {Long.toString(mItemID)};
        Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(mItemID)).build();
        mItemCursor = getContentResolver().query(selectedItem, null, selection, selectionArgs, null);

        // Setup views
        if (mItemCursor != null && mItemCursor.moveToFirst()) {
            PantryItem mItem = PantryItem.fromCursorDetail(mItemCursor);
            nameTV.setText(mItem.getName());
            if (!mItem.isInPantry()) {
                inPantryIV.setVisibility(View.INVISIBLE);
            }
            categoryTV.setText(mItem.getCategory());
            shopTV.setText(mItem.getShop());
            noteTV.setText(mItem.getNote());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mItemCursor.close();
    }
}
