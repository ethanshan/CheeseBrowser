package net.codingpark.cheesebrowser;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by ethanshan on 8/18/14.
 */
public class Utils {

    public static final String TAG = "Utils";

    /**
     * Execute shell command through java code
     * @param cmd
     */
    public static void execCmd(String cmd) {
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            cmd += "\n";
            dos.writeBytes(cmd);
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null)
                    dos.close();
                if (dis != null)
                    dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Public function used update scheduled startup/shutdown task time
     * @param ctx   Context
     * @param type
     *          STARTUP_TIME | SHUTDOWN_TIME
     * @param time
     *          The time to update, format(eg, "6:30")
     */
    public static void setScheduleTime(Context ctx, int type, String time) {
        AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences sp = ctx.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Intent r_intent = new Intent();
        int hours = Integer.valueOf(time.split(":")[0]).intValue();
        int minutes = Integer.valueOf(time.split(":")[1]).intValue();
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (hours < calendar.get(Calendar.HOUR) ||
                (hours == calendar.get(Calendar.HOUR)
                        && minutes <= calendar.get(Calendar.MINUTE))) {
            Log.d(TAG, "Set time before current time, trigger it tomorrow!");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);

        if (type == BrowserActivity.STARTUP_TIME) {
            Log.d(TAG, "Set auto startup time, time:\t" + time);
            // 1. Store startup time to shared preference
            editor.putString(BrowserActivity.STARTUP_TIME_KEY, time);
            editor.commit();
            // 2. Remove running auto startup schedule task
            r_intent.setAction(BrowserActivity.AUTO_STARTUP_ACTION);
            PendingIntent r_startup_pending_intent =
                    PendingIntent.getBroadcast(ctx, 0, r_intent, 0);
            alarmManager.cancel(r_startup_pending_intent);
            // 3. Use new time to create auto startup schedule task
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), BrowserActivity.ONE_DAY_TIME_MILLIS,
                    r_startup_pending_intent);
        } else if (type == BrowserActivity.SHUTDOWN_TIME) {
            Log.d(TAG, "Set auto shutdown time, time:\t" + time);
            // 1. Store shutdown time to shared preference
            editor.putString(BrowserActivity.SHUTDOWN_TIME_KEY, time);
            editor.commit();
            // 2. Remove running auto startup schedule task
            r_intent.setAction(BrowserActivity.AUTO_SHUTDOWN_ACTION);
            PendingIntent r_shutdown_pending_intent =
                    PendingIntent.getBroadcast(ctx, 0, r_intent, 0);
            alarmManager.cancel(r_shutdown_pending_intent);
            // 3. Use new time to update auto shutdown schedule task
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), BrowserActivity.ONE_DAY_TIME_MILLIS,
                    r_shutdown_pending_intent);
        }
    }
}
