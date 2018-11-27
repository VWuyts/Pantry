package com.wuyts.nik.pantry.utilities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;

import com.wuyts.nik.pantry.R;

import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_CATEGORY;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class DbFiller {
    private final Context mContext;

    public DbFiller(Context context) {
        mContext = context;
    }

    public void addItems() {
        ContentValues itemValues = new ContentValues();
        ContentResolver contentResolver = mContext.getContentResolver();
        Resources resources = mContext.getResources();

        for(String item : resources.getStringArray(R.array.canned_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_canned));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_colruyt));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.pasta_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_pasta));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_colruyt));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.breakfast_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_breakfast));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.dairy_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_dairy));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_colruyt));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.cheese_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_cheese));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.sauces_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_sauces));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.spices_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_spices));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.fruit_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_fruit));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_NOTE, resources.getString(R.string.note_bio_fair_trade));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.veg_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_vegetables));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_NOTE, resources.getString(R.string.note_bio));
            itemValues.put(COLUMN_IS_OK, false);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
        for(String item : resources.getStringArray(R.array.frozen_data)) {
            itemValues.put(COLUMN_NAME, item);
            itemValues.put(COLUMN_CATEGORY, resources.getString(R.string.cat_frozen));
            itemValues.put(COLUMN_SHOP, resources.getString(R.string.shop_delhaize));
            itemValues.put(COLUMN_IS_OK, true);
            contentResolver.insert(CONTENT_URI, itemValues);
            itemValues.clear();
        }
    }
}
