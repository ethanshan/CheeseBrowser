package net.codingpark.cheesebrowser;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Calendar;

import net.codingpark.cheesebrowser.serialport.SerialPortManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * Handle AlarmManager scheduled startup/shutdown intent
 */
public class ScheduleReceiver extends BroadcastReceiver {

    private static final String TAG = "ScheduleReceiver";
    private static long duration = 0L;
    
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
                    //Utils.execCmd(BrowserActivity.SCREEN_ON_BIN);
                } else if (action.contains("SHUTDOWN")) {
                    Log.d(TAG, "Trigger SHUTDOWN operate");
                    Utils.execCmd(Utils.SHUTDOWN);
                }
            }
        }

    }
    private Handler handler = new Handler();

    private Runnable writeTask = new Runnable() {
    	@Override
    	public void run() {
    		// 1. Write startup to MCU
    		Utils.setStartupTime(duration);
    		// 2. Loop read serial return results
    		//	if results == 85
    		//	then shutdown and return
    		// 	else
    		//	write startup to MCU, Again!
    		while (true) {
    			byte[] arrayOfByte = new byte[1];
    			Arrays.fill(arrayOfByte, (byte) 0);
    			try {
    				SerialPortManager.getInstance().getSerialPort()
    				.getInputStream().read(arrayOfByte);
    				Log.d(TAG, "Results: " + arrayOfByte[0]);
    				try {
    					Thread.sleep(2000);
    					if (arrayOfByte[0] == 0x55) {
    						Log.d(TAG, "Serial write success!");
    						Thread.sleep(3000);
    						Log.d(TAG, "Starting shutdown!");
    						//Utils.shutdown();
    						return;
    					}
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			} catch (InvalidParameterException e) {
    				e.printStackTrace();
    				Log.d(TAG, "1");
    				break;
    			} catch (SecurityException e) {
    				Log.d(TAG, "2");
    				e.printStackTrace();
    				break;
    			} catch (IOException e) {
    				Log.d(TAG, "3");
    				e.printStackTrace();
    				break;
    			}
    		} // End while
    		Log.d(TAG, "Write to MCU again");
    		handler.post(this);
    	}
    };
};
