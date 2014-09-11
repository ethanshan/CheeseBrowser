package net.codingpark.cheesebrowser;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import net.codingpark.cheesebrowser.serialport.SerialPortManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
	
	public void start() {
			SharedPreferences sp = context.getSharedPreferences(BrowserActivity.PREFERENCE_DATA, 
					Context.MODE_PRIVATE);
            // 4. Compute now day of week
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
            // 5. Calculate next startup time
            String startup_time = sp.getString(Utils.startup_time_keys.get(day_of_week - 1)
            		, BrowserActivity.DEFAULT_STARTUP_TIME);
            //duration = Utils.getDuration(startup_time);
            duration = 120L;
            Log.d(TAG, "Startup duration: " + duration);
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
							completedHandler.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(context, "Serial write OK!", Toast.LENGTH_SHORT).show();
								}

							});
							// TODO Just for test
							Utils.shutdown();
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
