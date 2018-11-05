package com.wuyts.nik.pantry.Utilities;

import android.content.ContentValues;
import android.content.Context;

import com.wuyts.nik.pantry.R;

import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_CATEGORY;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_AMOUNT;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_UNIT;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.Data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class DbFiller {
    ContentValues itemValues;
    Context mContext;

    public DbFiller(Context context) {
        mContext = context;
    }

    public void addItems() {
        itemValues = new ContentValues();

        itemValues.put(COLUMN_NAME, mContext.getString(R.string.item_canned_white));
        itemValues.put(COLUMN_CATEGORY, mContext.getString(R.string.cat_canned));
        itemValues.put(COLUMN_AMOUNT, 3);
        itemValues.put(COLUMN_UNIT, mContext.getString(R.string.unit_jar));
        itemValues.put(COLUMN_SHOP, mContext.getString(R.string.shop_colruyt));
        mContext.getContentResolver().insert(CONTENT_URI, itemValues);
        itemValues.clear();

        itemValues.put(COLUMN_NAME, mContext.getString(R.string.item_fruit_bananas));
        itemValues.put(COLUMN_CATEGORY, mContext.getString(R.string.cat_fruit));
        itemValues.put(COLUMN_AMOUNT, 1);
        itemValues.put(COLUMN_UNIT, mContext.getString(R.string.unit_kg));
        itemValues.put(COLUMN_SHOP, mContext.getString(R.string.shop_delhaize));
        itemValues.put(COLUMN_NOTE, mContext.getString(R.string.note_bio_fair_trade));
        mContext.getContentResolver().insert(CONTENT_URI, itemValues);
        itemValues.clear();

        itemValues.put(COLUMN_NAME, mContext.getString(R.string.item_veg_onions));
        itemValues.put(COLUMN_CATEGORY, mContext.getString(R.string.cat_vegetables));
        itemValues.put(COLUMN_SHOP, mContext.getString(R.string.shop_delhaize));
        mContext.getContentResolver().insert(CONTENT_URI, itemValues);
        itemValues.clear();

        itemValues.put(COLUMN_NAME, mContext.getString(R.string.item_veg_garlic));
        itemValues.put(COLUMN_CATEGORY, mContext.getString(R.string.cat_vegetables));
        itemValues.put(COLUMN_SHOP, mContext.getString(R.string.shop_delhaize));
        mContext.getContentResolver().insert(CONTENT_URI, itemValues);
        itemValues.clear();

        itemValues.put(COLUMN_NAME, mContext.getString(R.string.item_veg_carrots));
        itemValues.put(COLUMN_CATEGORY, mContext.getString(R.string.cat_vegetables));
        mContext.getContentResolver().insert(CONTENT_URI, itemValues);
        itemValues.clear();
    }
}
