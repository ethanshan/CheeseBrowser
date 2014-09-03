package net.codingpark.cheesebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    private static boolean first_time = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "TEST");
        // 1. Obtain SharedPreferences object
        SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);

        // 2. Listene connectivity change action
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            Log.d(TAG, "Receive ConnectivityChange Action");
            if (ni != null) {
                // Judge current network is connected, then, start browser
                if (ni.isConnected() && first_time) {
                    Log.d(TAG, "Receive Connected Action, start browser");
                    first_time = false;
                    try {
                        Log.d(TAG, "Wait 60 seconds");
                        Thread.sleep(60000);
                        Log.d(TAG, "Start Browser");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String urlText = sp.getString(BrowserActivity.WEB_URL_KEY,
                            BrowserActivity.DEFAULT_WEB_URL);   // Web URL
                    Uri uri = Uri.parse(urlText);
        /*
        TODO Current use Baidu Browser, because WebView play flash, the screen will be red
        Used to start self browser WebActivity
        Intent webIntent = new Intent(context, WebActivity.class);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        webIntent.putExtra("url", uri);
        */
                    Intent webIntent = new Intent();
                    webIntent.setAction(Intent.ACTION_VIEW);
                    webIntent.setData(uri);
                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // 需要设置这个标志位, 否则会报异常
                    context.startActivity(webIntent); // 打开浏览器
                }

            }
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // 3. Initial all scheduled task
            Log.d(TAG, "Receive BOOT_COMPLETED action");
            Log.d(TAG, "Initial schedule alarm:");
            String trigger_time;
            for (String key : Utils.key_action_maps.keySet()) {
                trigger_time = sp.getString(key, Utils.getKeyType(key)
                        == BrowserActivity.STARTUP_TIME ? BrowserActivity.DEFAULT_STARTUP_TIME
                        : BrowserActivity.DEFAULT_SHUTDOWN_TIME);
                Log.d(TAG, key + "(" + trigger_time + ")");
                Utils.setScheduleTime(context, Utils.key_action_maps.get(key), key, trigger_time);
            }
        }

    }

}
