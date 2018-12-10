package com.wuyts.nik.pantry;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AddItemFragment extends Fragment {
    private Button mAddItemBtn;
    private Button mCancelBtn;
    private Spinner mCategorySP;
    private Spinner mShopSP;
    private OnButtonClickListener mOnButtonClickListener;

    public AddItemFragment() {
        // Required empty public constructor
    } // end constructor

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnButtonClickListener = (OnButtonClickListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement mOnButtonClickListener");
        }
    } // end onAttach

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        // Retrieve views
        mAddItemBtn = rootView.findViewById(R.id.btn_add_done);
        mCancelBtn = rootView.findViewById(R.id.btn_add_cancel);
        mCategorySP = rootView.findViewById(R.id.sp_add_category);
        mShopSP = rootView.findViewById(R.id.sp_add_shop);

        return rootView;
    } // end onCreateView

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Setup spinners
        Context context = getActivity();
        if (context != null) {
            ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(context, R.array.categories, android.R.layout.simple_spinner_item);
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategorySP.setAdapter(catAdapter);
            ArrayAdapter<CharSequence> shopAdapter = ArrayAdapter.createFromResource(context, R.array.shops, android.R.layout.simple_spinner_item);
            shopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mShopSP.setAdapter(shopAdapter);
        }

        // set onClickListener on buttons
        View.OnClickListener onAddItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onAddItem();
            }
        };
        mAddItemBtn.setOnClickListener(onAddItemClickListener);
        View.OnClickListener onCancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnButtonClickListener.onCancel();
            }
        };
        mCancelBtn.setOnClickListener(onCancelClickListener);
    } // end onActivityCreated

    @Override
    public void onDetach() {
        super.onDetach();
        mOnButtonClickListener = null;
    } // end onDetach

    // Container Activity must implement this interface
    // Ref: https://developer.android.com/training/basics/fragments/communicating
    // Ref: https://developer.android.com/guide/components/fragments#CommunicatingWithActivity
    public interface OnButtonClickListener {
        void onAddItem();
        void onCancel();
    } // end interface OnButtonClickListener
}
