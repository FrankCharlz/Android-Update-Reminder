package com.mj.updatereminder;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Frank on 12/29/2015.
 */
public class Prefs {

    private static final String PREFS_FILE_NAME = "update_reminder_prefs";
    private static final String PREFS_VERSION_CODE = "last_server_version";
    private static final String PREFS_LAUNCHES_LEFT = "current_app_launches";
    private final Context context;
    private final SharedPreferences prefs;

    public Prefs(Context context) {
        //remember to destroy this object, avoid leaking context..
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

    }

    public int getLaunchesRemaining() {
        return prefs.getInt(PREFS_LAUNCHES_LEFT, 0);
    }

    public int getVersionCode() {
        return prefs.getInt(PREFS_VERSION_CODE, 0);
    }

    public void setPrefsVersionCode(int code) {
        prefs.edit().putInt(PREFS_VERSION_CODE, code).apply();
    }

    public void decrementLaunches() {
        //logic logic logic
        int rm = getLaunchesRemaining();
        rm--;
        if (rm < 0)
            prefs.edit().putInt(PREFS_LAUNCHES_LEFT, rm).apply();
        else
            prefs.edit().putInt(PREFS_LAUNCHES_LEFT, 0).apply();
    }

    public void setRemainingLaunches(int rm) {
        prefs.edit().putInt(PREFS_LAUNCHES_LEFT, rm).apply();
    }
}
