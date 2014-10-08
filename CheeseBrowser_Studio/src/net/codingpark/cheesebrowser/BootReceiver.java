package net.codingpark.cheesebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    private static boolean first_time = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1. Obtain SharedPreferences object
        final SharedPreferences sp = context.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        final Context mContext = context;

        // 2. Listening connectivity change action
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            Log.d(TAG, "Receive ConnectivityChange Action");
            if (ni != null) {
                // Judge current network is connected, then, start browser
                if (ni.isConnected() && first_time) {
                    Log.d(TAG, "Receive Connected Action, start browser");
                    first_time = false;
                    Thread t = new Thread(new Runnable() {

						@Override
						public void run() {
							String delay_time = 
									sp.getString(BrowserActivity.SHOW_DELAY_KEY, BrowserActivity.DEFAULT_SHOW_DELAY); 
							try {
								Log.d(TAG, "Waiting " + delay_time + " seconds");
								Thread.sleep(Integer.valueOf(delay_time) * 1000);
								Log.d(TAG, "Start Browser");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							// Get broadcast url from SharedPreferences
							String urlText = sp.getString(BrowserActivity.WEB_URL_KEY,
									BrowserActivity.DEFAULT_WEB_URL);   // Web URL
							// Put the url to intent
							Intent webIntent = new Intent(mContext, WebActivity.class);
							webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							webIntent.putExtra("url", urlText);
							// Start WebActivity to play
							mContext.startActivity(webIntent);
						}
                    	
                    });
                    t.start();
                     
                    /* Use system default browser as display application */
//                    Uri uri = Uri.parse(urlText);
//                    Intent webIntent = new Intent();
//                    webIntent.setAction(Intent.ACTION_VIEW);
//                    webIntent.setData(uri);
//                    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            SerialWriteTask task = new SerialWriteTask(context);
            task.start();
        }

    }

}
