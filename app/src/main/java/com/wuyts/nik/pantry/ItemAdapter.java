package com.wuyts.nik.pantry;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuyts.nik.pantry.data.PantryItem;

/**
 *  Created by Veronique Wuyts on 24/11/2018
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final Cursor mCursor;
    private final ListItemClickListener mListItemClickListener;
    private final boolean mDataValid;
    private final int mIdColumn;

    public ItemAdapter(Cursor cursor, ListItemClickListener listener) {
        mCursor = cursor;
        mListItemClickListener = listener;
        mDataValid = mCursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pantry_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("Only to be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Cursor could not move to position" + position);
        }

        PantryItem pantryItem = PantryItem.fromCursorMain(mCursor);
        if (pantryItem != null) {
            holder.nameTV.setText(pantryItem.getName());
            holder.shopTV.setText(pantryItem.getShop());
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor.moveToPosition(position))
            return mCursor.getLong(mIdColumn);
        return 0;
    }

    public interface ListItemClickListener {
        void onListItemClick(int itemPosition);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Declarations of views in view holder
        final TextView nameTV;
        final TextView shopTV;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tv_name);
            shopTV = itemView.findViewById(R.id.tv_shop);
            itemView.setOnClickListener(this);
        }

        // Set click listener on ItemViewHolder
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    }
}
