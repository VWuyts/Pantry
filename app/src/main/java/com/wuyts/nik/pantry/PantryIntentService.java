package com.wuyts.nik.pantry;

import android.app.IntentService;
import android.content.Intent;

import com.wuyts.nik.pantry.utilities.Utils;

public class PantryIntentService extends IntentService {
    public static final String ACTION_ALL_DONE = "com.wuyts.nik.pantry.action.ALL_DONE";


    public PantryIntentService() {
        super("PantryIntentService");
    } // end constructor

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ALL_DONE.equals(action)) {
                Utils.setAllDoneInDb(this, null);
            }
        }
    } // end onHandleIntent
}
