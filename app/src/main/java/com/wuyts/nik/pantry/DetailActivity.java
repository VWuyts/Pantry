package com.wuyts.nik.pantry;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.wuyts.nik.pantry.utilities.Utils;

public class DetailActivity extends AppCompatActivity
        implements DetailFragment.OnToggleIsInPantryListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MainActivity.ITEM_ID_KEY)) {
            long itemId = intent.getLongExtra(MainActivity.ITEM_ID_KEY, 0);
            Cursor itemCursor = Utils.getPantryItem(this, itemId, null);

            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_detail);
            if (detailFragment != null) {
                detailFragment.setItemCursor(itemCursor);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_detail, detailFragment).commit();
            }
        }

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tb_detail);
        //toolbar.setTitle(R.string.title_detail_activity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_detail_activity);
        }
    } // end onCreate

    @Override
    public void onToggleIsInPantry(long itemId, boolean isInPantry) {
        Utils.toggleIsInPantry(this, itemId, isInPantry);
        Utils.goToMainActivity(this, true);
    } // end onToggleIsInPantry
}
