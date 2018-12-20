package com.wuyts.nik.pantry;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.wuyts.nik.pantry.utilities.Utils;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_CATEGORY;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;
import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements AddItemFragment.OnButtonClickListener, DetailFragment.OnToggleIsInPantryListener,
        MainFragment.OnListItemSelectedListener, MainFragment.OnSwipeLeftListener {

    private ArrayList<String> mShopList;
    private static boolean mMasterDetail;
    public static final String ITEM_ID_KEY = "itemId";
    public static final String UPDATE_CURSOR_KEY = "update cursor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View detailFragment = findViewById(R.id.fr_dynamic);
        mMasterDetail = detailFragment != null && detailFragment.getVisibility() == View.VISIBLE;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(UPDATE_CURSOR_KEY)) {
            if (intent.getBooleanExtra(UPDATE_CURSOR_KEY, true)) {
                updateMainCursor();
            }
        }

        // Toolbar
        Toolbar toolbar = mMasterDetail ? (Toolbar) findViewById(R.id.tb_main) : (Toolbar) findViewById(R.id.tb_in_ctb);
        toolbar.setTitle(R.string.title_main_activity);
        setSupportActionBar(toolbar);

        // Set click listener on FAB
        final FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMasterDetail) {
                    AddItemFragment addItemFragment = new AddItemFragment();

                    // Display fragment
                    replaceDynamicFragment(addItemFragment);
                } else {
                    Intent addItemIntent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivity(addItemIntent);
                }
            }
        });
        // Hide FAB when scrolling down
        RecyclerView recyclerView = findViewById(R.id.rv_pantry_items);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
    } // end onCreate

    @Override
    public void onDestroy() {
        super.onDestroy();
        mShopList.clear();
    } // end onDestroy

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Get shop data
        String[] projection = {"DISTINCT " + COLUMN_SHOP};
        String sortOrder = COLUMN_SHOP + " ASC";
        Cursor shopCursor = getContentResolver().query(CONTENT_URI, projection, null, null, sortOrder);

        // No menu shown if no shops in cursor
        if (shopCursor == null) {
            return false;
        }
        if (shopCursor.getCount() == 0) {
            shopCursor.close();
            return false;
        }

        // Add shop data to ArrayList
        mShopList = new ArrayList<>();
        while (shopCursor.moveToNext()) {
            mShopList.add(shopCursor.getString(shopCursor.getColumnIndex(COLUMN_SHOP)));
        }
        shopCursor.close();

        // Add different shops to menu
        int counter = -1;
        for (String shop : mShopList) {
            menu.add(Menu.NONE, ++counter, menu.NONE, getResources().getString(R.string.go_to_shop) + " " + shop)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }

        return true;
    } // end onCreateOptionsMenu

    // React to selection of items in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int menuItemId = menuItem.getItemId();
        if (menuItemId < mShopList.size()) {
            String shop = mShopList.get(menuItemId);
            Intent shopIntent = new Intent(MainActivity.this, ShopActivity.class);
            shopIntent.putExtra(ShopActivity.SHOP_KEY, shop);
            startActivity(shopIntent);
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    } // end onOptionsItemSelected

    @Override
    public void onAddItem() {
        ContentValues values = new ContentValues();
        String name = ((EditText) findViewById(R.id.et_add_name)).getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getText(R.string.required_name), Toast.LENGTH_SHORT).show();
        } else {
            values.put(COLUMN_NAME, name);
            String category = ((Spinner) findViewById(R.id.sp_add_category)).getSelectedItem().toString();
            values.put(COLUMN_CATEGORY, category);
            String shop = ((Spinner) findViewById(R.id.sp_add_shop)).getSelectedItem().toString();
            values.put(COLUMN_SHOP, shop);
            String note = ((EditText) findViewById(R.id.et_add_note)).getText().toString().trim();
            values.put(COLUMN_NOTE, note);
            boolean isInPantry = ((Switch) findViewById(R.id.sw_add_in_pantry)).isChecked();
            values.put(COLUMN_IS_OK, isInPantry);

            getContentResolver().insert(CONTENT_URI, values);

            // Update main fragment and widget data
            updateMainCursor();
            Utils.updateWidget(this);

            clearDetailFragment();
        }
    } // end onAddItem

    @Override
    public void onCancel() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    } // end onCancel

    @Override
    public void onToggleIsInPantry(long itemId, boolean isInPantry) {
        toggleIsInPantry(itemId, isInPantry);

        if (mMasterDetail) {
            updateDetailCursor(itemId);
        }
    } // end onToggleIsInPantry

    @Override
    public void onListItemSelected(long itemId) {
        if (!mMasterDetail) {
            Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
            detailIntent.putExtra(ITEM_ID_KEY, itemId);
            startActivity(detailIntent);
        } else {
            DetailFragment detailFragment = new DetailFragment();

            // Set data of pantry item
            Cursor itemCursor = Utils.getPantryItem(this, itemId, null);
            detailFragment.setItemCursor(itemCursor);

            // Display fragment
            replaceDynamicFragment(detailFragment);
        }
    } // end onListItemSelected

    @Override
    public void onSwipeLeft(int position) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fr_main);
        if (mainFragment != null) {
            boolean isInPantry = mainFragment.isInPantry(position);
            long itemId = mainFragment.getItemId(position);
            toggleIsInPantry(itemId, isInPantry);

            clearDetailFragment();
        }
    } // end onSwipeLeft


    /* Utility functions */

    private void clearDetailFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    } // end clearDetailFragment

    private void replaceDynamicFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fr_dynamic, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void toggleIsInPantry(long itemId, boolean isInPantry) {
        Utils.toggleIsInPantry(this, itemId, isInPantry);
        updateMainCursor();
        Utils.updateWidget(this);
    } // end toggleIsInPantry

    private void updateDetailCursor(long itemId) {
        Cursor itemCursor = Utils.getPantryItem(this, itemId, null);
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fr_dynamic);
        if (detailFragment != null) {
            Cursor oldCursor = detailFragment.swapCursor(itemCursor);
            oldCursor.close();
        }
    } // end updateDetailCursor

    private void updateMainCursor() {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fr_main);
        if (mainFragment != null) {
            String[] projection = {_ID, COLUMN_NAME, COLUMN_SHOP, COLUMN_IS_OK};
            Cursor newCursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);
            mainFragment.swapCursor(newCursor);
        }
    } // end updateMainCursor
}
