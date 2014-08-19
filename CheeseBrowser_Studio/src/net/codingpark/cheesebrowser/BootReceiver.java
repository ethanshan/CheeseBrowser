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

        SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);

        // 1. Listen boot completed action, then start Web Browser with the URL
        Log.d(TAG, "Boot completed, starting BrowserActivity!");
        // TODO The URL sended to Web Browser should can be editable
        String urlText = sp.getString(BrowserActivity.WEB_URL_KEY,
                "http://192.168.0.15/show/index");   // Web URL
        Uri uri = Uri.parse(urlText);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // 需要设置这个标志位, 否则会报异常
        context.startActivity(webIntent); // 打开浏览器

        // 2. Initial scheduled startup task
        // 2.1 Obtain startup time from shared preference
        String startup_time = sp.getString(BrowserActivity.STARTUP_TIME_KEY, "6:30");
        // 2.2 Set scheduled startup task
        Utils.setScheduleTime(context, BrowserActivity.STARTUP_TIME, startup_time);


        // 3. Initial scheduled shutdown task
        // 3.1 Obtain shutdown time from shared preference
        String shutdown_time = sp.getString(BrowserActivity.SHUTDOWN_TIME_KEY, "18:30");
        // 3.2 Set scheduled shutdown task
        Utils.setScheduleTime(context, BrowserActivity.SHUTDOWN_TIME, shutdown_time);

    }

}
