package net.codingpark.cheesebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Handle AlarmManager scheduled sended startup/shutdown intent
 */
public class ScheduleReceiver extends BroadcastReceiver {

    private static final String TAG = "ScheduleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Obtain SharedPreferences object
        SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        Log.d(TAG, "Receiver schedule action");

        if (intent.getAction().equals(BrowserActivity.AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.MON_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive monday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Monday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.TUES_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Tuesday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Tuesday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.WED_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Wednesday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Wednesday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.THUR_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Thursday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Thursday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.FRI_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Friday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Friday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.SAT_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Saturday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Saturday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.SUN_AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive Sunday startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        } else if (intent.getAction().equals(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive Sunday shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        }
    }
};
