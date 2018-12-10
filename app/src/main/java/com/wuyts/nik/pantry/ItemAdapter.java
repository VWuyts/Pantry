package com.wuyts.nik.pantry;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuyts.nik.pantry.data.PantryItem;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Cursor mCursor;
    private final ListItemClickListener mListItemClickListener;
    private boolean mDataValid;
    private int mIdColumn;
    private int mInPantryColumn;

    ItemAdapter(Cursor cursor, ListItemClickListener listener) {
        mCursor = cursor;
        mListItemClickListener = listener;
        mDataValid = mCursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex(_ID) : -1;
        mInPantryColumn = mDataValid ? mCursor.getColumnIndex(COLUMN_IS_OK) : -1;
        setHasStableIds(true);
    } // end constructor

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pantry_item, parent, false);
        return new ItemViewHolder(view);
    } // end onCreateViewHolder

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
            if (pantryItem.isInPantry()) {
                holder.itemCL.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.teal_50));
            } else {
                holder.itemCL.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red_50));
            }
        }
    } // end onBindViewHolder

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    } // end getItemCount

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor.moveToPosition(position))
            return mCursor.getLong(mIdColumn);
        return 0;
    } // end getItemId

    boolean isInPantry(int position) {
        if (mDataValid && mCursor.moveToPosition(position))
            return mCursor.getInt(mInPantryColumn) > 0;
        return false;
    } // end getIsInPantry

    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        mDataValid = mCursor != null;
        mIdColumn = mDataValid ? mCursor.getColumnIndex(_ID) : -1;
        mInPantryColumn = mDataValid ? mCursor.getColumnIndex(COLUMN_IS_OK) : -1;
        notifyDataSetChanged();
        return oldCursor;
    } // end swapCursor

    public interface ListItemClickListener {
        void onListItemClick(int itemPosition);
    } // end interface ListItemClickListener

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Declarations of views in view holder
        final TextView nameTV;
        final TextView shopTV;
        final ConstraintLayout itemCL;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.tv_name);
            shopTV = itemView.findViewById(R.id.tv_shop);
            itemCL = itemView.findViewById(R.id.cl_pantry_item);
            itemView.setOnClickListener(this);
        }

        // Set click listener on ItemViewHolder
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListItemClickListener.onListItemClick(clickedPosition);
        }
    } // end class ItemViewHolder
}
