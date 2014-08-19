package net.codingpark.cheesebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Handle AlarmManager scheduled sended startup/shutdown intent
 */
public class ScheduleReceiver extends BroadcastReceiver {

    private static final String TAG = "ScheduleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BrowserActivity.AUTO_SHUTDOWN_ACTION)) {
            Log.d(TAG, "Receive shutdown action");
            Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
        } else if (intent.getAction().equals(BrowserActivity.AUTO_STARTUP_ACTION)) {
            Log.d(TAG, "Receive startup action");
            Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
        }
    }
};
