package net.codingpark.cheesebrowser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // 1. Obtain SharedPreferences object
        SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);

        // 2. Listen boot completed action, then start Web Browser with the URL
        String urlText = sp.getString(BrowserActivity.WEB_URL_KEY,
                BrowserActivity.DEFAULT_WEB_URL);   // Web URL
        Uri uri = Uri.parse(urlText);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // 需要设置这个标志位, 否则会报异常
        context.startActivity(webIntent); // 打开浏览器

        // 3. Initial all scheduled task
        String trigger_time;
        for (String key : Utils.key_action_maps.keySet()) {
            trigger_time = sp.getString(key, Utils.getKeyType(key)
                    == BrowserActivity.STARTUP_TIME ? BrowserActivity.DEFAULT_STARTUP_TIME
                    : BrowserActivity.DEFAULT_SHUTDOWN_TIME);
            Utils.setScheduleTime(context, Utils.key_action_maps.get(key), key, trigger_time);
        }

    }

}
