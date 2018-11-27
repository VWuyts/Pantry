package com.wuyts.nik.pantry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 *  Created by Veronique Wuyts on 05/11/2018
 */
public class MainActivity extends AppCompatActivity /*implements ItemAdapter.ListItemClickListener*/ {

    public static boolean mMasterDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.ll_two_pane) != null) {
            mMasterDetail = true;
        }
    }
}
