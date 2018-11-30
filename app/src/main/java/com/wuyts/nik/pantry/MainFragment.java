package com.wuyts.nik.pantry;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuyts.nik.pantry.utilities.DbFiller;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public class MainFragment extends Fragment implements ItemAdapter.ListItemClickListener {
    private Cursor mItemsCursor;
    private ItemAdapter mItemAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnListItemSelectedListener mOnListItemSelectedListener;
    private OnSwipeLeftListener mOnSwipeLeftListener;
    private RecyclerView mItemsRV;

    public MainFragment() {
        // Required empty public constructor
    } // end constructor

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnListItemSelectedListener = (OnListItemSelectedListener)context;
            mOnSwipeLeftListener = (OnSwipeLeftListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnListItemSelectedListener and OnSwipeLeftListener");
        }
    } // end onAttach

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Content provider: get pantry items
        String[] projection = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
        Context context = getActivity();
        if (context != null) { // Check if fragment is added to activity, so that getActivity() cannot return null
            mItemsCursor = context.getContentResolver().query(CONTENT_URI, projection, null, null, null);
            if (mItemsCursor != null && mItemsCursor.getCount() == 0) {
                // Add items to pantry
                DbFiller dbFiller = new DbFiller(getContext());
                dbFiller.addItems();
                mItemsCursor = getActivity().getContentResolver().query(CONTENT_URI, projection, null, null, null);
            }
        }

        // RecyclerView
        mItemsRV = rootView.findViewById(R.id.rv_pantry_items);
        mItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mItemsRV.setHasFixedSize(true);
        mItemAdapter = new ItemAdapter(mItemsCursor, this);
        mItemsRV.setAdapter(mItemAdapter);

        // Swipe to toggle isInPantry
        mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mItemsRV);

        return rootView;
    } // end OnCreateView

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemsCursor.close();
    } // end onDestroyView

    @Override
    public void onDetach() {
        super.onDetach();
        mOnListItemSelectedListener = null;
        mOnSwipeLeftListener = null;
    } // end onDetach

    @Override
    public void onListItemClick(int itemPosition) {
        long itemId = mItemAdapter.getItemId(itemPosition);
        mOnListItemSelectedListener.onListItemSelected(itemId);
    } // end onListItemClick

    public void swapCursor(Cursor newCursor) {
        if (mItemsCursor == newCursor) {
            return;
        }

        mItemsCursor = newCursor;
        Cursor oldCursor = mItemAdapter.swapCursor(mItemsCursor);
        oldCursor.close();
    } // end swapCursor

    private final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true; // true if moved, false otherwise
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            long itemID = mItemAdapter.getItemId(viewHolder.getAdapterPosition());
            mOnSwipeLeftListener.onSwipeLeft(itemID);
            // mItemAdapter.notifyItemChanged(viewHolder.getAdapterPosition()); // does not restore ViewHolder
            // Ref: https://stackoverflow.com/questions/31787272/android-recyclerview-itemtouchhelper-revert-swipe-and-restore-view-holder
            mItemTouchHelper.attachToRecyclerView(null);
            mItemTouchHelper.attachToRecyclerView(mItemsRV);
        }
    };

    // Container Activity must implement these interfaces
    // Ref: https://developer.android.com/training/basics/fragments/communicating
    // Ref: https://developer.android.com/guide/components/fragments#CommunicatingWithActivity
    public interface OnListItemSelectedListener {
        void onListItemSelected(long itemId);
    } // end interface onListItemSelectedListener
    public interface OnSwipeLeftListener {
        void onSwipeLeft(long itemId);
    } // end interface OnSwipeLeftListener
}
