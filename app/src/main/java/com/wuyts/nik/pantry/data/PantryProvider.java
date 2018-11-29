package com.wuyts.nik.pantry.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static com.wuyts.nik.pantry.data.PantryContract.AUTHORITY;
import static com.wuyts.nik.pantry.data.PantryContract.ITEM_PATH;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class PantryProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private PantryDbHelper mPantryDbHelper;
    private static final int ALL_ITEMS = 100;
    private static final int ITEM_ID = 101;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Uri for complete pantry item table
        uriMatcher.addURI(AUTHORITY, ITEM_PATH, ALL_ITEMS);
        // Uri for one row in item table
        uriMatcher.addURI(AUTHORITY, ITEM_PATH + "/#", ITEM_ID);

        return uriMatcher;
    } // end buildUriMatcher

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPantryDbHelper = new PantryDbHelper(context);

        return true;
    }// end onCreate

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mPantryDbHelper.getReadableDatabase();
        if (TextUtils.isEmpty(sortOrder)) sortOrder = PantryContract.Item._ID + " ASC";

        Cursor itemData;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case ALL_ITEMS:
                itemData = db.query(PantryContract.Item.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                itemData = db.query(PantryContract.Item.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return itemData;
    } // end query

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    } // end getType

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mPantryDbHelper.getWritableDatabase();
        Uri returnUri;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case ALL_ITEMS:
                long id = db.insert(PantryContract.Item.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PantryContract.Item.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        // Notify registered observers that a row was updated
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    } // end insert

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mPantryDbHelper.getWritableDatabase();
        int noDeleted = 0;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case ALL_ITEMS:
                break;
            case ITEM_ID:
                noDeleted = db.delete(PantryContract.Item.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return noDeleted;
    } // end delete

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mPantryDbHelper.getWritableDatabase();
        int noUpdated;
        int match = sUriMatcher.match(uri);

        switch(match) {
            case ALL_ITEMS:
                noUpdated = db.update(PantryContract.Item.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ITEM_ID:
                noUpdated = db.update(PantryContract.Item.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        return noUpdated;
    } // end update
}
