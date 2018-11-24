package com.wuyts.nik.pantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class DetailActivity extends AppCompatActivity {

    private long mItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve data from intent
        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.ITEM_ID_KEY)) {
            mItemID = intent.getLongExtra(MainActivity.ITEM_ID_KEY, 0);
        }

        Toast.makeText(this, Long.toString(mItemID), Toast.LENGTH_SHORT).show();

        // Get data of pantry item


        // Retrieve views

        // Setup views
    }
}
