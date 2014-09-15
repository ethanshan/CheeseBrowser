package net.codingpark.cheesebrowser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.codingpark.cheesebrowser.entity.DayInfo;
import net.codingpark.cheesebrowser.entity.ScheduleSet;
import net.codingpark.cheesebrowser.serialport.SerialPort;
import net.codingpark.cheesebrowser.serialport.SerialPortManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ethanshan on 8/18/14.
 */
public class Utils {

	public static final String TAG 					= "Utils";
	
	public static boolean serial_write_completed 	= false;
	
	private static Utils instance 					= null;
	
	private ScheduleSet	day_info_set				= null;
	
	private Utils() {
		// Initial ScheduleSet
		day_info_set	= new ScheduleSet();
		// Sunday
		DayInfo info = new DayInfo();
		info.setDay_of_week(Calendar.SUNDAY);
		info.setStartup_time_key(BrowserActivity.SUN_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.SUN_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.SUN_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.SUN_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Monday
		info = new DayInfo();
		info.setDay_of_week(Calendar.MONDAY);
		info.setStartup_time_key(BrowserActivity.MON_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.MON_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.MON_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.MON_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Tuesday
		info = new DayInfo();
		info.setDay_of_week(Calendar.TUESDAY);
		info.setStartup_time_key(BrowserActivity.TUES_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.TUES_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.TUES_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.TUES_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Wednesday
		info = new DayInfo();
		info.setDay_of_week(Calendar.WEDNESDAY);
		info.setStartup_time_key(BrowserActivity.WED_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.WED_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.WED_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.WED_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Thursday
		info = new DayInfo();
		info.setDay_of_week(Calendar.THURSDAY);
		info.setStartup_time_key(BrowserActivity.THUR_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.THUR_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.THUR_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.THUR_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Friday
		info = new DayInfo();
		info.setDay_of_week(Calendar.FRIDAY);
		info.setStartup_time_key(BrowserActivity.FRI_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.FRI_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.FRI_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.FRI_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// Saturday 
		info = new DayInfo();
		info.setDay_of_week(Calendar.SATURDAY);
		info.setStartup_time_key(BrowserActivity.SAT_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.SAT_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.SAT_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.SAT_SCHEDULE_ENABLE_KEY);
		day_info_set.addDayInfo(info);
		// TODO Global needed??
		/*
		info = new DayInfo();
		info.setDay_of_week(BrowserActivity.GLOBAL_DAY_OF_WEEK);
		info.setStartup_time_key(BrowserActivity.GLOBAL_STARTUP_TIME_KEY);
		info.setShutdown_time_key(BrowserActivity.GLOBAL_SHUTDOWN_TIME_KEY);
		info.setStartup_action(BrowserActivity.GLOBAL_AUTO_STARTUP_ACTION);
		info.setShutdown_action(BrowserActivity.GLOBAL_AUTO_SHUTDOWN_ACTION);
		info.setSchedule_enable_key(BrowserActivity.GLOBAL_SCHEDULE_ENABLE_KEY);
		*/
	}
	
	public ScheduleSet getSet() {
		return day_info_set;
	}
	
	public static Utils getInstance() {
		if (instance == null)
			instance = new Utils();
		return instance;
	}

	public static final Map<String, String> key_action_maps = new HashMap<String, String>() {
		{
			this.put(BrowserActivity.MON_SHUTDOWN_TIME_KEY,
					BrowserActivity.MON_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.MON_STARTUP_TIME_KEY,
					BrowserActivity.MON_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.TUES_SHUTDOWN_TIME_KEY,
					BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.TUES_STARTUP_TIME_KEY,
					BrowserActivity.TUES_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.WED_SHUTDOWN_TIME_KEY,
					BrowserActivity.WED_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.WED_STARTUP_TIME_KEY,
					BrowserActivity.WED_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.THUR_SHUTDOWN_TIME_KEY,
					BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.THUR_STARTUP_TIME_KEY,
					BrowserActivity.THUR_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.FRI_SHUTDOWN_TIME_KEY,
					BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.FRI_STARTUP_TIME_KEY,
					BrowserActivity.FRI_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.SAT_SHUTDOWN_TIME_KEY,
					BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.SAT_STARTUP_TIME_KEY,
					BrowserActivity.SAT_AUTO_STARTUP_ACTION);
			this.put(BrowserActivity.SUN_SHUTDOWN_TIME_KEY,
					BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION);
			this.put(BrowserActivity.SUN_STARTUP_TIME_KEY,
					BrowserActivity.SUN_AUTO_STARTUP_ACTION);
		}
	};

	public static final Map<String, String> action_enableKey_maps = new HashMap<String, String>() {
		{
			this.put(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.MON_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.MON_AUTO_STARTUP_ACTION,
					BrowserActivity.MON_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.TUES_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.TUES_AUTO_STARTUP_ACTION,
					BrowserActivity.TUES_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.WED_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.WED_AUTO_STARTUP_ACTION,
					BrowserActivity.WED_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.THUR_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.THUR_AUTO_STARTUP_ACTION,
					BrowserActivity.THUR_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.FRI_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.FRI_AUTO_STARTUP_ACTION,
					BrowserActivity.FRI_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.SAT_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.SAT_AUTO_STARTUP_ACTION,
					BrowserActivity.SAT_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION,
					BrowserActivity.SUN_SCHEDULE_ENABLE_KEY);
			this.put(BrowserActivity.SUN_AUTO_STARTUP_ACTION,
					BrowserActivity.SUN_SCHEDULE_ENABLE_KEY);
		}
	};

	public static final Map<String, Integer> action_dayOfWeek_maps = new HashMap<String, Integer>() {
		{
			this.put(BrowserActivity.MON_AUTO_SHUTDOWN_ACTION, Calendar.MONDAY);
			this.put(BrowserActivity.MON_AUTO_STARTUP_ACTION, Calendar.MONDAY);
			this.put(BrowserActivity.TUES_AUTO_SHUTDOWN_ACTION,
					Calendar.TUESDAY);
			this.put(BrowserActivity.TUES_AUTO_STARTUP_ACTION, Calendar.TUESDAY);
			this.put(BrowserActivity.WED_AUTO_SHUTDOWN_ACTION,
					Calendar.WEDNESDAY);
			this.put(BrowserActivity.WED_AUTO_STARTUP_ACTION,
					Calendar.WEDNESDAY);
			this.put(BrowserActivity.THUR_AUTO_SHUTDOWN_ACTION,
					Calendar.THURSDAY);
			this.put(BrowserActivity.THUR_AUTO_STARTUP_ACTION,
					Calendar.THURSDAY);
			this.put(BrowserActivity.FRI_AUTO_SHUTDOWN_ACTION, Calendar.FRIDAY);
			this.put(BrowserActivity.FRI_AUTO_STARTUP_ACTION, Calendar.FRIDAY);
			this.put(BrowserActivity.SAT_AUTO_SHUTDOWN_ACTION,
					Calendar.SATURDAY);
			this.put(BrowserActivity.SAT_AUTO_STARTUP_ACTION, Calendar.SATURDAY);
			this.put(BrowserActivity.SUN_AUTO_SHUTDOWN_ACTION, Calendar.SUNDAY);
			this.put(BrowserActivity.SUN_AUTO_STARTUP_ACTION, Calendar.SUNDAY);
		}
	};

	public static final ArrayList<String> startup_time_keys = new ArrayList<String>() {
		{
			this.add(BrowserActivity.SUN_STARTUP_TIME_KEY);
			this.add(BrowserActivity.MON_STARTUP_TIME_KEY);
			this.add(BrowserActivity.TUES_STARTUP_TIME_KEY);
			this.add(BrowserActivity.WED_STARTUP_TIME_KEY);
			this.add(BrowserActivity.THUR_STARTUP_TIME_KEY);
			this.add(BrowserActivity.FRI_STARTUP_TIME_KEY);
			this.add(BrowserActivity.SAT_STARTUP_TIME_KEY);
		}
	};

	// Scheduled execute binary file name
	public static final String REBOOT_BIN = "reboot";
	public static final String SCREEN_ON_BIN = "echo 10 > /sys/class/disp/disp/attr/hdmi";
	public static final String SCREEN_OFF_BIN = "echo 255 > /sys/class/disp/disp/attr/hdmi";
	public static final String SHUTDOWN = "reboot -p";

	/**
	 * Execute shell command through java code
	 * 
	 * @param cmd
	 */
    public static void execCmd(final String cmd) {
    	new Thread() {

			@Override
			public void run() {
				super.run();
				DataOutputStream dos = null;
				DataInputStream dis = null;
				try {
					Process p = Runtime.getRuntime().exec("su");
					dos = new DataOutputStream(p.getOutputStream());
					String r_cmd = cmd + "\n";
					dos.writeBytes(r_cmd);
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

    	}.start();
    }                         

	private static String getActionByKey(String key) {
		String action = "";
		if (key.equals(BrowserActivity.MON_STARTUP_TIME_KEY)) {
			action = BrowserActivity.MON_AUTO_STARTUP_ACTION;
		} else if (key.equals(BrowserActivity.MON_SHUTDOWN_TIME_KEY)) {

		}
		return action;
	}

	public static void setScheduleTime(Context ctx, String action, String key,
			String time) {
		Log.d(TAG, "Utils setScheduleTime key:" + key + "\taction:" + action
				+ "\ttime:\t" + time);
		AlarmManager alarmManager = (AlarmManager) ctx
				.getSystemService(Context.ALARM_SERVICE);
		SharedPreferences sp = ctx.getSharedPreferences(
				BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		Intent r_intent = new Intent();
		int hours = Integer.valueOf(time.split(":")[0]).intValue();
		int minutes = Integer.valueOf(time.split(":")[1]).intValue();
		Calendar calendar = Calendar.getInstance();
		if (hours < calendar.get(Calendar.HOUR_OF_DAY)
				|| (hours == calendar.get(Calendar.HOUR_OF_DAY) && minutes <= calendar
						.get(Calendar.MINUTE))) {
			Log.d(TAG, "Set time before current time, trigger it tomorrow!");
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, 0);

		// 1. Store time to shared preference
		editor.putString(key, time).commit();

		// 2. Remove running auto schedule task
		r_intent.setAction(action);
		PendingIntent r_pending_intent = PendingIntent.getBroadcast(ctx, 0,
				r_intent, 0);
		alarmManager.cancel(r_pending_intent);

		// 3. Use new time to create auto startup schedule task
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				BrowserActivity.ONE_DAY_TIME_MILLIS, r_pending_intent);
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

	// JNI
	/*
	 * private native static FileDescriptor open(String path, int baudrate, int
	 * flags); public native void close(); static {
	 * System.loadLibrary("serial_port"); }
	 */

	public static void shutdown() {
		execCmd(SHUTDOWN);
	}

	public static boolean setStartupTime(long time) {
		// FileDescriptor fd = null;
		SerialPort sp = null;
		OutputStream mOutputStream = null;
		InputStream mInputStream = null;
		try {
			// fd = open("/dev/ttyS7",9600,0);
			sp = SerialPortManager.getInstance().getSerialPort();
			mOutputStream = sp.getOutputStream();
			//mInputStream = sp.getInputStream();

			byte[] mBuffer = longToByteArray(1, time);
			mOutputStream.write(mBuffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (mOutputStream != null)
				try {
					mOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (mInputStream != null)
				try {
					mInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (sp != null) {
				sp.close();
			}
		}

		return true;
	}

	/**
	 * Calculate the duration(Unit: second) between current and the endTime
	 * 
	 * @param endTime
	 *            The end time, format "6:30" or "18:30"
	 * @return Duration in second
	 */
	public static long getDuration(String endTime) {
		long duration = 0;
		// Calculate current time
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTimeInMillis(System.currentTimeMillis());
		currentCal.set(Calendar.SECOND, 0);
		// Calculate end time
		Calendar endCal = Calendar.getInstance();
		endCal.setTimeInMillis(System.currentTimeMillis());
		int hours = Integer.valueOf(endTime.split(":")[0]).intValue();
		int minutes = Integer.valueOf(endTime.split(":")[1]).intValue();
		endCal.add(Calendar.DAY_OF_MONTH, 1);
		endCal.set(Calendar.HOUR_OF_DAY, hours);
		endCal.set(Calendar.MINUTE, minutes);
		endCal.set(Calendar.SECOND, 0);
		// Calculate the duration between end time and start time
		duration = (endCal.getTimeInMillis() - currentCal.getTimeInMillis()) / 1000;
		Log.d(TAG, "Duration: " + duration);

		return duration;
	}

	public static byte[] longToByteArray(int flags, long times) {
		byte[] result = new byte[9];

		result[0] = (byte) 0x00; // 校验位不需改变
		result[1] = (byte) 0xaa; // 校验位不需改变
		result[2] = (byte) 0xff; // 校验位不需改变
		result[3] = (byte) 0x55; // 校验位不需改变

		result[4] = (byte) (flags);// 状态位，1代表写入数据,0代表写入无效数据　

		result[5] = (byte) ((times >> 16) & 0xFF); // 数据处理位
		result[6] = (byte) ((times >> 8) & 0xFF); // 数据处理位
		result[7] = (byte) (times & 0xFF); // 数据处理位

		result[8] = (byte) 0x55; // 校验位不需改变

		return result;
	}

	
}
