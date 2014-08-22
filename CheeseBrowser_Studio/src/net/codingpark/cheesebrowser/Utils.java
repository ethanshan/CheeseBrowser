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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethanshan on 8/18/14.
 */
public class Utils {

    public static final String TAG = "Utils";

    public static final Map<String, String> key_action_maps = new HashMap<String, String>(){
        {
            this.put(BrowserActivity.MON_SHUTDOWN_TIME_KEY, BrowserActivity.MON_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.MON_STARTUP_TIME_KEY, BrowserActivity.MON_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.TUES_SHUTDOWN_TIME_KEY, BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.TUES_STARTUP_TIME_KEY, BrowserActivity.TUES_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.WED_SHUTDOWN_TIME_KEY, BrowserActivity.WED_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.WED_STARTUP_TIME_KEY, BrowserActivity.WED_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.THUR_SHUTDOWN_TIME_KEY, BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.THUR_STARTUP_TIME_KEY, BrowserActivity.THUR_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.FRI_SHUTDOWN_TIME_KEY, BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.FRI_STARTUP_TIME_KEY, BrowserActivity.FRI_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.SAT_SHUTDOWN_TIME_KEY, BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.SAT_STARTUP_TIME_KEY, BrowserActivity.SAT_AUTO_STARTUP_ACTION);
            this.put(BrowserActivity.SUN_SHUTDOWN_TIME_KEY, BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION);
            this.put(BrowserActivity.SUN_STARTUP_TIME_KEY, BrowserActivity.SUN_AUTO_STARTUP_ACTION);
        }
    };

    public static final Map<String, String> action_enableKey_maps = new HashMap<String, String>(){
        {
            this.put(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION, BrowserActivity.MON_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.MON_AUTO_STARTUP_ACTION, BrowserActivity.MON_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION, BrowserActivity.TUES_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.TUES_AUTO_STARTUP_ACTION, BrowserActivity.TUES_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION, BrowserActivity.WED_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.WED_AUTO_STARTUP_ACTION, BrowserActivity.WED_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION, BrowserActivity.THUR_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.THUR_AUTO_STARTUP_ACTION, BrowserActivity.THUR_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION, BrowserActivity.FRI_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.FRI_AUTO_STARTUP_ACTION, BrowserActivity.FRI_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION, BrowserActivity.SAT_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.SAT_AUTO_STARTUP_ACTION, BrowserActivity.SAT_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION, BrowserActivity.SUN_SCHEDULE_ENABLE_KEY);
            this.put(BrowserActivity.SUN_AUTO_STARTUP_ACTION, BrowserActivity.SUN_SCHEDULE_ENABLE_KEY);
        }
    };

    public static final Map<String, Integer> action_dayOfWeek_maps = new HashMap<String, Integer>(){
        {
            this.put(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION, Calendar.MONDAY);
            this.put(BrowserActivity.MON_AUTO_STARTUP_ACTION, Calendar.MONDAY);
            this.put(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION, Calendar.TUESDAY);
            this.put(BrowserActivity.TUES_AUTO_STARTUP_ACTION, Calendar.TUESDAY);
            this.put(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION, Calendar.WEDNESDAY);
            this.put(BrowserActivity.WED_AUTO_STARTUP_ACTION, Calendar.WEDNESDAY);
            this.put(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION, Calendar.THURSDAY);
            this.put(BrowserActivity.THUR_AUTO_STARTUP_ACTION, Calendar.THURSDAY);
            this.put(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION, Calendar.FRIDAY);
            this.put(BrowserActivity.FRI_AUTO_STARTUP_ACTION, Calendar.FRIDAY);
            this.put(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION, Calendar.SATURDAY);
            this.put(BrowserActivity.SAT_AUTO_STARTUP_ACTION, Calendar.SATURDAY);
            this.put(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION, Calendar.SUNDAY);
            this.put(BrowserActivity.SUN_AUTO_STARTUP_ACTION, Calendar.SUNDAY);
        }
    };

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

    private static String getActionByKey(String key) {
        String action = "";
        if (key.equals(BrowserActivity.MON_STARTUP_TIME_KEY)) {
           action = BrowserActivity.MON_AUTO_STARTUP_ACTION;
        } else if (key.equals(BrowserActivity.MON_SHUTDOWN_TIME_KEY)) {

        }
        return action;
    }

    public static void setScheduleTime(Context ctx, String action, String key, String time) {
        Log.d(TAG, "Utils setScheduleTime key:" + key + "\taction:" + action + "\ttime:\t" + time);
        AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences sp = ctx.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Intent r_intent = new Intent();
        int hours = Integer.valueOf(time.split(":")[0]).intValue();
        int minutes = Integer.valueOf(time.split(":")[1]).intValue();
        Calendar calendar= Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        if (hours < calendar.get(Calendar.HOUR) ||
                (hours == calendar.get(Calendar.HOUR)
                        && minutes <= calendar.get(Calendar.MINUTE))) {
            Log.d(TAG, "Set time before current time, trigger it tomorrow!");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "#####" + hours + "####" + minutes + "####action###" + action + "###time##" + calendar.getTime().toString());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        // 1. Store time to shared preference
        editor.putString(key, time).commit();

        // 2. Remove running auto schedule task
        r_intent.setAction(action);
        PendingIntent r_pending_intent =
                PendingIntent.getBroadcast(ctx, 0, r_intent, 0);
        alarmManager.cancel(r_pending_intent);
        // 3. Use new time to create auto startup schedule task
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), BrowserActivity.ONE_DAY_TIME_MILLIS,
                r_pending_intent);
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
            editor.putString(BrowserActivity.STARTUP_TIME_KEY, time).commit();
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
            editor.putString(BrowserActivity.SHUTDOWN_TIME_KEY, time).commit();
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

    public static int getKeyType(String key) {
        int type = BrowserActivity.STARTUP_TIME;

        if (key.equals(BrowserActivity.MON_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.TUES_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.WED_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.THUR_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.FRI_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.SAT_SHUTDOWN_TIME_KEY)
                || key.equals(BrowserActivity.SUN_SHUTDOWN_TIME_KEY))
            type = BrowserActivity.SHUTDOWN_TIME;
        return type;
    }
}
