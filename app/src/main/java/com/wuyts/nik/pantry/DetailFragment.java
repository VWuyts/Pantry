package com.wuyts.nik.pantry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyts.nik.pantry.data.PantryItem;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 *  Created by Veronique Wuyts on 26/11/2018
 */
public class DetailFragment extends Fragment {
    private Cursor mItemCursor;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        long itemID;

        // Retrieve views
        TextView nameTV = rootView.findViewById(R.id.tv_item_name);
        ImageView inPantryIV = rootView.findViewById(R.id.iv_item_in_pantry);
        TextView categoryTV = rootView.findViewById(R.id.tv_item_category);
        TextView shopTV = rootView.findViewById(R.id.tv_item_shop);
        TextView noteTV = rootView.findViewById(R.id.tv_item_note);

        if (!MainActivity.mMasterDetail) {
            // Retrieve data from intent
            Intent intent = getActivity().getIntent();
            if (intent != null & intent.hasExtra(MainFragment.ITEM_ID_KEY)) {
                itemID = intent.getLongExtra(MainFragment.ITEM_ID_KEY, 0);
                // Get data of pantry item
                String selection = _ID + " = ?";
                String[] selectionArgs = {Long.toString(itemID)};
                Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemID)).build();
                mItemCursor = getActivity().getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
            }
        }

        // Setup views
        if (mItemCursor != null && mItemCursor.moveToFirst()) {
            PantryItem mItem = PantryItem.fromCursorDetail(mItemCursor);
            nameTV.setText(mItem.getName());
            if (mItem.isInPantry()) {
                inPantryIV.setVisibility(View.VISIBLE);
            } else {
                inPantryIV.setVisibility(View.INVISIBLE);
                nameTV.setTextColor(getResources().getColor(R.color.red));
            }
            categoryTV.setText(mItem.getCategory());
            shopTV.setText(mItem.getShop());
            noteTV.setText(mItem.getNote());
        }

        return rootView;
    }

    // Set cursor to cursor with pantry item with given itemID
    public void setItemCursor(Cursor itemCursor) {
        mItemCursor = itemCursor;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemCursor.close();
    }
}
