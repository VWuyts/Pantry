package com.wuyts.nik.pantry;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyts.nik.pantry.data.PantryItem;

/**
 *  Created by Veronique Wuyts on 26/11/2018
 */
public class DetailFragment extends Fragment {
    private Button mSetInPantryBtn;
    private Button mSetNotInPantryBtn;
    private ImageView mInPantryIV;
    private Cursor mItemCursor;
    private PantryItem mPantryItem;
    private TextView mCategoryTV;
    private TextView mNameTV;
    private TextView mNoteTV;
    private TextView mShopTV;
    private OnToggleIsInPantryListener mOnToggleIsInPantryListener;

    public DetailFragment() {
        // Required empty public constructor
    } // end constructor

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnToggleIsInPantryListener = (OnToggleIsInPantryListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnToggleIsInPantryListener");
        }
    } // end onAttach

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Retrieve views
        mNameTV = rootView.findViewById(R.id.tv_item_name);
        mInPantryIV = rootView.findViewById(R.id.iv_item_in_pantry);
        mCategoryTV = rootView.findViewById(R.id.tv_item_category);
        mShopTV = rootView.findViewById(R.id.tv_item_shop);
        mNoteTV = rootView.findViewById(R.id.tv_item_note);
        mSetInPantryBtn = rootView.findViewById(R.id.btn_set_in_pantry);
        mSetNotInPantryBtn = rootView.findViewById(R.id.btn_set_not_in_pantry);

        return rootView;
    } // end onCreateView

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Setup views
        if (mItemCursor != null && mItemCursor.moveToFirst()) {
            mPantryItem = PantryItem.fromCursorDetail(mItemCursor);
            setupViews();
        }

        // Set onClickListener on buttons
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnToggleIsInPantryListener.onToggleIsInPantry(mPantryItem.getId(), mPantryItem.isInPantry());
            }
        };
        mSetInPantryBtn.setOnClickListener(onClickListener);
        mSetNotInPantryBtn.setOnClickListener(onClickListener);
    } // end onActivityCreated


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mItemCursor.close();
    } // end onDestroyView

    @Override
    public void onDetach() {
        super.onDetach();
        mOnToggleIsInPantryListener = null;
    } // end onDetach

    // Set cursor to cursor with pantry item with given itemID
    public void setItemCursor(Cursor itemCursor) {
        mItemCursor = itemCursor;
    } // end setItemCursor

    public Cursor swapCursor(Cursor newCursor) {
        if (mItemCursor == newCursor) {
            return null;
        }

        Cursor oldCursor = mItemCursor;
        mItemCursor = newCursor;

        // Set views
        if (mItemCursor != null && mItemCursor.moveToFirst()) {
            mPantryItem = PantryItem.fromCursorDetail(mItemCursor);
            setupViews();
        }

        return oldCursor;
    } // end swapCursor

    private void setupViews() {
        if (mPantryItem != null) {
            mNameTV.setText(mPantryItem.getName());
            mCategoryTV.setText(mPantryItem.getCategory());
            mShopTV.setText(mPantryItem.getShop());
            mNoteTV.setText(mPantryItem.getNote());
            if (mPantryItem.isInPantry()) {
                mNameTV.setTextColor(getResources().getColor(R.color.green));
                mInPantryIV.setVisibility(View.VISIBLE);
                mSetInPantryBtn.setVisibility(View.GONE);
                mSetNotInPantryBtn.setVisibility(View.VISIBLE);
            } else {
                mNameTV.setTextColor(getResources().getColor(R.color.red));
                mInPantryIV.setVisibility(View.INVISIBLE);
                mSetInPantryBtn.setVisibility(View.VISIBLE);
                mSetNotInPantryBtn.setVisibility(View.GONE);
            }
        }
    } // end setupViews

    // Container Activity must implement this interface
    // Ref: https://developer.android.com/training/basics/fragments/communicating
    // Ref: https://developer.android.com/guide/components/fragments#CommunicatingWithActivity
    public interface OnToggleIsInPantryListener {
        void onToggleIsInPantry(long itemId, boolean isInPantry);
    } // end interface OnToggleIsInPantryListener
}
