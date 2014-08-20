package net.codingpark.cheesebrowser;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Scheduled Time Store
 * ---------------------------------------------------------
 * |     name       |  type  | store path       | format  |
 * ---------------------------------------------------------
 * | startup_time   | String | PREFERENCE_DATA  | 6:30    |
 * ---------------------------------------------------------
 * | shutdown_time  | String | PREFERENCE_DATA  | 18:30   |
 * --------------------------------------------------------
 * |    web_url     | String | PREFERENCE_DATA | http://..|
 * ---------------------------------------------------------
 */
public class BrowserActivity extends Activity {

	private static final String TAG = "BrowserActivity";

	private TextView startup_time_view 			= null;
	private TextView shutdown_time_view 		= null;
    private TextView homepage_view              = null;
 
	private Button startup_time_edit_button 		= null;
	private Button shutdown_time_edit_button 		= null;
	private Button reboot_button 					= null;
    private Button homepage_edit_button             = null;
    private Button disable_display_button           = null;

    // The set time type, while call Utils.setScheduleTime
    // as a function parameters.
    public static final int STARTUP_TIME            = 0;
    public static final int SHUTDOWN_TIME           = 1;

    // The startup and shutdown action will execute scheduled,
    // Execute period is one day. (Millis second)
    public static final long ONE_DAY_TIME_MILLIS = 24 * 60 * 60 * 1000;

    // Private SharedPreferences data group name
    public static final String PREFERENCE_DATA              = "BrowserActivity";

    // Auto startup/shutdown action
    public static final String AUTO_STARTUP_ACTION          = "net.codingpark.AUTO_STARTUP_ACTION";
    public static final String AUTO_SHUTDOWN_ACTION         = "net.codingpark.AUTO_SHUTDOWN_ACTION";

    // The key used to find startup/shutdown/homepage_url
    // from SharedPreferences
    public static final String STARTUP_TIME_KEY             = "startup_time";
    public static final String SHUTDOWN_TIME_KEY            = "shutdown_time";
    public static final String WEB_URL_KEY                  = "web_url";
    public static final String DEFAULT_WEB_URL              = "http://192.168.0.15/show/index";

    // Scheduled execute binary file name
    public static final String REBOOT_BIN                   = "system_reboot.sh";
    public static final String SCREEN_ON_BIN                = "screen_on.sh";
    public static final String SCREEN_OFF_BIN               = "screen_off.sh";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		initUI();
	}

    /**
     * Use stored in SharedPreferences' data to refresh UI
     */
    private void updateView() {
        Log.d(TAG, "Update shutdown and startup time!");
        SharedPreferences sp = this.getSharedPreferences(
                BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        startup_time_view.setText(getString(R.string.startup_time_text_view_string)
                .concat(sp.getString(STARTUP_TIME_KEY, "6:30")));
        shutdown_time_view.setText(getString(R.string.shutdown_time_text_view_string)
                .concat(sp.getString(SHUTDOWN_TIME_KEY, "18.30")));
        Log.d(TAG, "Startup time " + sp.getString(STARTUP_TIME_KEY, "100"));
        homepage_view.setText(getString(R.string.homepage_text_view_string)
                .concat(sp.getString(WEB_URL_KEY, DEFAULT_WEB_URL)));
    }

	private void initUI() {
		startup_time_view = (TextView) this
				.findViewById(R.id.startup_time_text_view);
		shutdown_time_view = (TextView) this
				.findViewById(R.id.shutdown_time_text_view);
        homepage_view = (TextView) this
                .findViewById(R.id.homepage_text_view);


        updateView();

		startup_time_edit_button = (Button) this
				.findViewById(R.id.startup_time_edit_button);
		shutdown_time_edit_button = (Button) this
				.findViewById(R.id.shutdown_time_edit_button);
		reboot_button = (Button) this.findViewById(R.id.reboot_button);
        homepage_edit_button = (Button) this.findViewById(R.id.homepage_edit_button);
        disable_display_button = (Button) this.findViewById(R.id.disable_display_button);
		
		startup_time_edit_button.setOnClickListener(new OnClickListener() {
 @Override
			public void onClick(View v) {
				Log.d(TAG, "Handle edit startup time edit action");
				TimePickerDialog picker = new TimePickerDialog(BrowserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Log.d(TAG, "Set startup scheduled time:\t" + i + ":" + i2);
                        // Update startup scheduled task time
                        Utils.setScheduleTime(BrowserActivity.this, BrowserActivity.STARTUP_TIME, i + ":" + i2);
                        updateView();
                    }
                }, 0, 0, false);
				picker.show();
			}
			
		});

        shutdown_time_edit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Handle edit shutdown time edit action");
                TimePickerDialog picker = new TimePickerDialog(BrowserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        Log.d(TAG, "Set shutdown scheduled time:\t" + i + ":" + i2);
                        Utils.setScheduleTime(BrowserActivity.this, BrowserActivity.SHUTDOWN_TIME, i + ":" + i2);
                        updateView();
                    }
                }, 0, 0, false);
                picker.show();
            }
        });

		// Handle reboot action
		// shell: system_reboot.sh
		reboot_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "Handle reboot");
				Utils.execCmd(REBOOT_BIN);
			}

		});
		
		// Handle manual disable display action
		// shell: screen_off.sh
		disable_display_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "Handle disable display");
				Utils.execCmd(SCREEN_OFF_BIN);
			}
			
		});

        // Handle edit web site home page
        homepage_edit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editHomePage();
                updateView();
            }
        });

	}

    /**
     * Handle edit web home page action.
     * As user click edit button, a setting dialog show with
     * a editView, used to input new home page url. If user
     * click ok button, the new url will be stored to
     * SharedPreferences and refresh UI. At restart next time,
     * Web browser will auto open the new url.
     */
    private void editHomePage() {
        Log.d(TAG, "Handle set homepage action");
        // 1. Create edit dialog and initial related widget
        final Dialog dialog = new Dialog(BrowserActivity.this);
        dialog.setContentView(R.layout.edit_dialog);
        Button ok_b = (Button) dialog.findViewById(R.id.edit_dialog_ok_b);
        Button cancel_b = (Button) dialog.findViewById(R.id.edit_dialog_cancel_b);
        final EditText editText = (EditText) dialog.findViewById(R.id.edit_dialog_edit_text);

        // 2. Add ok/cancel button handler
        ok_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Update home page:\t" + editText.getText().toString());
                SharedPreferences sp = BrowserActivity.this
                        .getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(WEB_URL_KEY, editText.getText().toString());
                editor.commit();
            }
        });
        cancel_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Don't need update home page");
                dialog.dismiss();
            }
        });
        // 3. Make Dialog show
        dialog.show();
    }
}
