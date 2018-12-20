package com.wuyts.nik.pantry;

import android.content.ContentValues;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.wuyts.nik.pantry.utilities.Utils;

import static com.wuyts.nik.pantry.data.PantryContract.Item.CONTENT_URI;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_CATEGORY;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_IS_OK;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NAME;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_NOTE;
import static com.wuyts.nik.pantry.data.PantryContract.Item.COLUMN_SHOP;

public class AddItemActivity extends AppCompatActivity
        implements AddItemFragment.OnButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.tb_add);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_add_activity);
        }
    } // end onCreate

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
            Utils.updateWidget(this);
            Utils.goToMainActivity(this, true);
        }
    } // end onAddItem

    @Override
    public void onCancel() {
        Utils.goToMainActivity(this, false);
    } // end onCancel
}
