package net.codingpark.cheesebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

/**
 * Handle AlarmManager scheduled startup/shutdown intent
 */
public class ScheduleReceiver extends BroadcastReceiver {

    private static final String TAG = "ScheduleReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Obtain SharedPreferences object
        SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);

        // 2. Compute now day of week
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        // 3. Obtain take place action from received intent
        String action = intent.getAction();
        Log.d(TAG, "Received action:" + action);

        // 4. Judge today day of week == The take place action mapping day
        //    If matching, starting execute
        if (day_of_week == Utils.action_dayOfWeek_maps.get(action)) {
            Log.d(TAG, "today_day_of_week:" + day_of_week + "\tmapping_day_of_week:" + Utils.action_dayOfWeek_maps.get(action));
            // 4.1 Judge the day of week today schedule task weather enabled
            //     If enable continue
            if (sp.getInt(Utils.action_enableKey_maps.get(action), BrowserActivity.SCHEDULE_ENABLE)
                    == BrowserActivity.SCHEDULE_ENABLE) {
                Log.d(TAG, "Today schedule enabled");
                // 4.2 Judge execute startup or shutdown operation
                if (action.contains("STARTUP")) {
                    Log.d(TAG, "Trigger STARTUP operate");
                    Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
                } else if (action.contains("SHUTDOWN")) {
                    Log.d(TAG, "Trigger SHUTDOWN operate");
                    Utils.execCmd(BrowserActivity.SCREEN_OFF_BIN);
                }
            }
        }

    }
};
