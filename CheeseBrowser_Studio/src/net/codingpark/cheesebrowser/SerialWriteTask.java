package net.codingpark.cheesebrowser;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import net.codingpark.cheesebrowser.entity.DayInfo;
import net.codingpark.cheesebrowser.serialport.SerialPortManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SerialWriteTask {
	
	private static final String TAG 	= "SerialWriteTask";
	private long duration 				= 0L;
	private Context context				= null;
	private WriteThread task			= null;
	private Handler completedHandler	= null;

	
	public class WriteThread  extends Thread {

	@Override
	public void run() {
		Utils.serial_write_completed = false;
		// 1. Write startup to MCU
		Utils.setStartupTime(duration);
		// 2. Loop read serial return results
		//	if results == 85(Indicate success)
		//	then shutdown and return
		// 	else (Indicate failed)
		//	write startup to MCU, Again!
		while (true) {
			byte[] arrayOfByte = new byte[1];
			Arrays.fill(arrayOfByte, (byte) 0);
			try {
				SerialPortManager.getInstance().getSerialPort()
				.getInputStream().read(arrayOfByte);
				Log.d(TAG, "Results: " + arrayOfByte[0]);
				if (arrayOfByte[0] == 0x55) {
					Log.d(TAG, "Serial write success!");
					Utils.serial_write_completed = true;
					return;
				} else {
					Log.d(TAG, "Serial write failed!");
					return;
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
	}
		
	};

	public SerialWriteTask() {
	}

	public SerialWriteTask(Context context) {
		this.context = context;
	}

	public SerialWriteTask(Context context, Handler completedHandler) {
		this.context = context;
		this.completedHandler = completedHandler;
	}
	
	/**
	 * Calculate next schedule startup time
	 * @return
	 * 	-1: User disable all schedule task
	 * 	>0: The next startup time
	 */
	public long getStartupTime() {
		// Initial the startup time negative
		// If user disable 1~7 schedule task, return negative value.
		long time = -1L;
		SharedPreferences sp = context.getSharedPreferences(BrowserActivity.PREFERENCE_DATA,
				Context.MODE_PRIVATE);
		DayInfo info = null;

		// Calculate current calendar
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTimeInMillis(System.currentTimeMillis());
		currentCal.set(Calendar.SECOND, 0);
		Log.d(TAG, "Current time: " + currentCal.getTime().toString());
		int current_day_of_week = currentCal.get(Calendar.DAY_OF_WEEK);

		// Start from tomorrow, loop a cycle
		for (int i = current_day_of_week + 1; i <= current_day_of_week + 7; i++) {
			// Calculate tomorrow value day of week
			int r_day = i;
			if (i > 7) {
				r_day = i - 7;
			}
			// Obtain the day related structure, such as action name, time key...
			info = Utils.getInstance().getSet().getByDayOfWeek(r_day);
			// Judge weather enable schedule startup/shutdown action
			// if enabled: next startup time is it, calculate time
			// if disabled: continue try next day
			if (sp.getInt(info.getSchedule_enable_key(),
					BrowserActivity.SCHEDULE_ENABLE) 
					== BrowserActivity.SCHEDULE_ENABLE) {
				String endTime = sp.getString(info.getStartup_time_key(), BrowserActivity.DEFAULT_STARTUP_TIME);
				long duration = 0;
				// Calculate end calendar
				Calendar endCal = Calendar.getInstance();
				endCal.setTimeInMillis(System.currentTimeMillis());
				int hours = Integer.valueOf(endTime.split(":")[0]).intValue();
				int minutes = Integer.valueOf(endTime.split(":")[1]).intValue();
				
				int diff = 1;
				if ( (r_day) > current_day_of_week)
					diff = (r_day) - current_day_of_week;
				else {
					diff = 7 - (current_day_of_week - (r_day));
				}
				Log.d(TAG, "Diff day: " + diff);
				Log.d(TAG, "End hour: " + hours);
				Log.d(TAG, "End minutes: " + minutes);
				endCal.set(Calendar.HOUR_OF_DAY, hours);
				endCal.set(Calendar.MINUTE, minutes);
				endCal.set(Calendar.SECOND, 0);
				endCal.add(Calendar.DAY_OF_MONTH, diff);
				Log.d(TAG, "End time: " + endCal.getTime().toString());
				// Calculate the duration between end time and start time
				time = (endCal.getTimeInMillis() - currentCal.getTimeInMillis()) / 1000;
				Log.d(TAG, "Duration: " + time);
				return time;
			}
		}
		Log.d(TAG, "Schedule all disabled! duration: " + time);
		return time;
	}
	
	/**
	 * Start serial port write task
	 */
	public void start() {
		if (completedHandler != null) {
			// Send serial write starting message
			Message msg = completedHandler.obtainMessage(BrowserActivity.WRITE_START);
			completedHandler.sendMessage(msg);
		}

		duration = this.getStartupTime();
		if (duration < 0)
			duration = 0;

		// 5 Write startup time to MCU
		final Timer writeTask = new Timer();
		writeTask.schedule(new TimerTask() {

			@Override
			public void run() {
				if (Utils.serial_write_completed) {
					Log.d(TAG, "Serial write completed, cancel write task!");
					this.cancel();
					writeTask.cancel();
					Utils.serial_write_completed = false;

					if (completedHandler != null) {
						// Send serial write completed message
						Message msg = completedHandler.obtainMessage(BrowserActivity.WRITE_COMPLETED);
						completedHandler.sendMessage(msg);
					}
				} else {
					Log.d(TAG, "Starting write task!");
					if (task != null) {
						if (task.isAlive())
							task.interrupt();
						while (task.isAlive()) {
							try {
								Thread.sleep(2000);
								Log.d(TAG, "Task not stop, waiting 2s");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Log.d(TAG, "Task stopped, starting again!");
					}
					if (Utils.serial_write_completed)
						return;
					task = new WriteThread();
					task.start();
				}
			}

		}, 0, 10000);
	}

}
