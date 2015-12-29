package com.mj.updatereminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Frank on 12/29/2015.
 *
 * How it works?
 * --On app launched it fetches trivially up-to-date version from server in one thread and saves it
 *   in shared prefs
 *
 * --meanwhile on another thread it fetches stores version from prefs and compares it to the package
 *   and decides to launch the update dialog
 *
 * --if dialog dismissed without updating, remember to show again later.. if updated set
 *   next update ++or just -1 or 0
 *
 *
 */
public class UpdateReminder {
    private static final String SERVER_BASE_URL = "http://localhost:8989/?package=";
    private static final String PREFS_FILE_NAME = "update_reminder_prefs";
    private static final String PREFS_VERSION_CODE = "last_server_version";
    static int current_version_code;
    static int server_version_code;
    static int prefs_version_code;
    static String current_package_name;
    private static SharedPreferences prefs;


    public static void init(Context context) {
        //init shared prefs
        prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);



        current_version_code = getVersionForCurrentApp(context);
        prefs_version_code = getVersionCodeFromPrefs(context);
        current_package_name = getVersionNameForCurrentApp(context);

        if (current_version_code <= 0 || current_package_name.isEmpty()) {
            //stop cause things a already fucked up..
            return;
        }

        if (current_version_code < prefs_version_code) {
            createUpdateDialog(context);
        }


        //update shared prefs
        new Thread(new FetcherThread(), "update-prefs-worker-thread").start();

    }

    private static void createUpdateDialog(Context context) {

    }

    private static int getVersionCodeFromPrefs(Context context) {
        return prefs.getInt(PREFS_VERSION_CODE, 0);
    }

    static class FetcherThread implements  Runnable {
        @Override
        public void run() {
            updatePrefsVersionCode(current_package_name);
        }
    }

    private static String getVersionNameForCurrentApp(Context context) {
        return context.getPackageName();
    }
    
    private static int getVersionForCurrentApp(Context context) {
        try {
            return context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  0;
        }
    }

    private static void updatePrefsVersionCode(String current_package_name) {
        try {
            URL url = new URL(SERVER_BASE_URL +current_package_name);
            StringBuilder sb = new StringBuilder();
            BufferedInputStream bis = new BufferedInputStream(url.openConnection().getInputStream());
            int i;
            while (true) {
                i = bis.read();
                if (i < 0) break;
                sb.append((char)i);
            }
            int code =   Integer.parseInt(sb.toString());
            prefs.edit().putInt(PREFS_VERSION_CODE, code).apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
