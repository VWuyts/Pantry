package com.wuyts.nik.pantry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyts.nik.pantry.utilities.DbFiller;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

/**
 * Created by Veronique Wuyts on 26/11/2018
 */
public class MainFragment extends Fragment implements ItemAdapter.ListItemClickListener {
    private Cursor mItemsCursor;
    private ItemAdapter mItemAdapter;
    public static final String ITEM_ID_KEY = "itemId";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Content provider: get pantry items
        String projection[] = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
        mItemsCursor = getActivity().getContentResolver().query(CONTENT_URI, projection, null, null, null);
        if (mItemsCursor != null && mItemsCursor.getCount() == 0) {
            // Add items to pantry
            DbFiller dbFiller = new DbFiller(getContext());
            dbFiller.addItems();
            mItemsCursor = getActivity().getContentResolver().query(CONTENT_URI, projection, null, null, null);
        }

        // RecyclerView
        RecyclerView itemsRV = rootView.findViewById(R.id.rv_pantry_items);
        itemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRV.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mItemsCursor, this);
        itemsRV.setAdapter(mItemAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(int itemPosition) {
        long itemID = mItemAdapter.getItemId(itemPosition);
        if (!MainActivity.mMasterDetail) {
            Intent detailIntent = new Intent(getContext(), DetailActivity.class);
            detailIntent.putExtra(ITEM_ID_KEY, itemID);
            startActivity(detailIntent);
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            DetailFragment detailFragment = new DetailFragment();

            // Set pantry item data in detail fragment
            // Get data of pantry item
            String selection = _ID + " = ?";
            String selectionArgs[] = {Long.toString(itemID)};
            Uri selectedItem = CONTENT_URI.buildUpon().appendPath(Long.toString(itemID)).build();
            Cursor itemCursor = getActivity().getContentResolver().query(selectedItem, null, selection, selectionArgs, null);
            detailFragment.setItemCursor(itemCursor);

            // Display fragment
            fragmentManager.beginTransaction()
                    .replace(R.id.fr_detail, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemsCursor.close();
    }
}
