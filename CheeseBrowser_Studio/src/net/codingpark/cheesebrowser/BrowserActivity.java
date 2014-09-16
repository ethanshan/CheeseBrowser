package net.codingpark.cheesebrowser;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
 * | mon_startup_time | String | PREFERENCE_DATA | 6:30 |
 * ---------------------------------------------------------
 * | mon_shutdown_time | String | PREFERENCE_DATA | 18:30 |
 * ---------------------------------------------------------
 * | mon_schedule_enable | int | PREFERENCE_DATA | 1 |
 * ---------------------------------------------------------
 * |..tues..wed..thur..fri..sat..sun|
 */
public class BrowserActivity extends Activity {

	private static final String TAG = "BrowserActivity";

    private TextView homepage_view              = null;
 
	private Button reboot_button 					= null;
    private Button homepage_edit_button             = null;
    private Button disable_display_button           = null;
    private Button play_button                      = null;
    private Button start_server_button              = null;
    private Button stop_server_button               = null;
    private Button test_button                      = null;

    // -------------- Monday to Sunday related show/edit widget --------
    private Button mon_startup_edit_bt              = null;
    private Button mon_shutdown_edit_bt             = null;
    private CheckBox mon_schedule_enable_cb         = null;
    private Button tues_startup_edit_bt             = null;
    private Button tues_shutdown_edit_bt            = null;
    private CheckBox tues_schedule_enable_cb        = null;
    private Button wed_startup_edit_bt              = null;
    private Button wed_shutdown_edit_bt             = null;
    private CheckBox wed_schedule_enable_cb         = null;
    private Button thur_startup_edit_bt             = null;
    private Button thur_shutdown_edit_bt            = null;
    private CheckBox thur_schedule_enable_cb        = null;
    private Button fri_startup_edit_bt              = null;
    private Button fri_shutdown_edit_bt             = null;
    private CheckBox fri_schedule_enable_cb         = null;
    private Button sat_startup_edit_bt              = null;
    private Button sat_shutdown_edit_bt             = null;
    private CheckBox sat_schedule_enable_cb         = null;
    private Button sun_startup_edit_bt              = null;
    private Button sun_shutdown_edit_bt             = null;
    private CheckBox sun_schedule_enable_cb         = null;
    private Button global_startup_edit_bt           = null;
    private Button global_shutdown_edit_bt          = null;
    private CheckBox global_schedule_enable_cb      = null;

    // The set time type, while call Utils.setScheduleTime
    // as a function parameters.
    public static final int STARTUP_TIME            = 0;
    public static final int SHUTDOWN_TIME           = 1;

    // Schedule enable/disable
    public static final int SCHEDULE_DISABLE        = 0;
    public static final int SCHEDULE_ENABLE         = 1;

    // The startup and shutdown action will execute scheduled,
    // Execute period is one day. (Millis second)
    public static final long ONE_DAY_TIME_MILLIS = 24 * 60 * 60 * 1000;

    // Private SharedPreferences data group name
    public static final String PREFERENCE_DATA              = "BrowserActivity";

    // Auto startup/shutdown action for monday to sunday
    public static final String MON_AUTO_STARTUP_ACTION          = "net.codingpark.MON_AUTO_STARTUP_ACTION";
    public static final String MON_AUTO_SHUTDOWN_ACTION         = "net.codingpark.MON_AUTO_SHUTDOWN_ACTION";
    public static final String TUES_AUTO_STARTUP_ACTION         = "net.codingpark.TUES_AUTO_STARTUP_ACTION";
    public static final String TUES_AUTO_SHUTDOWN_ACTION        = "net.codingpark.TUES_AUTO_SHUTDOWN_ACTION";
    public static final String WED_AUTO_STARTUP_ACTION          = "net.codingpark.WED_AUTO_STARTUP_ACTION";
    public static final String WED_AUTO_SHUTDOWN_ACTION         = "net.codingpark.WED_AUTO_SHUTDOWN_ACTION";
    public static final String THUR_AUTO_STARTUP_ACTION         = "net.codingpark.THUR_AUTO_STARTUP_ACTION";
    public static final String THUR_AUTO_SHUTDOWN_ACTION        = "net.codingpark.THUR_AUTO_SHUTDOWN_ACTION";
    public static final String FRI_AUTO_STARTUP_ACTION          = "net.codingpark.FRI_AUTO_STARTUP_ACTION";
    public static final String FRI_AUTO_SHUTDOWN_ACTION         = "net.codingpark.FRI_AUTO_SHUTDOWN_ACTION";
    public static final String SAT_AUTO_STARTUP_ACTION          = "net.codingpark.SAT_AUTO_STARTUP_ACTION";
    public static final String SAT_AUTO_SHUTDOWN_ACTION         = "net.codingpark.SAT_AUTO_SHUTDOWN_ACTION";
    public static final String SUN_AUTO_STARTUP_ACTION          = "net.codingpark.SUN_AUTO_STARTUP_ACTION";
    public static final String SUN_AUTO_SHUTDOWN_ACTION         = "net.codingpark.SUN_AUTO_SHUTDOWN_ACTION";

    // The key used to find startup/shutdown/homepage_url
    // from SharedPreferences
    public static final String WEB_URL_KEY                  = "web_url";
    public static final String DEFAULT_WEB_URL              = "http://192.168.0.15/show/index";
    public static final String DEFAULT_STARTUP_TIME         = "6:30";
    public static final String DEFAULT_SHUTDOWN_TIME        = "18:30";
    public static final int DEFAULT_SCHEDULE_STATE          = SCHEDULE_ENABLE;

    // ----------------Monday to Sunday key value----------------
    public static final String MON_STARTUP_TIME_KEY         = "mon_startup_time";
    public static final String MON_SHUTDOWN_TIME_KEY        = "mon_shutdown_time";
    public static final String MON_SCHEDULE_ENABLE_KEY      = "mon_schedule_enable";
    public static final String TUES_STARTUP_TIME_KEY        = "tues_startup_time";
    public static final String TUES_SHUTDOWN_TIME_KEY       = "tues_shutdown_time";
    public static final String TUES_SCHEDULE_ENABLE_KEY     = "tues_schedule_enable";
    public static final String WED_STARTUP_TIME_KEY         = "wed_startup_time";
    public static final String WED_SHUTDOWN_TIME_KEY        = "wed_shutdown_time";
    public static final String WED_SCHEDULE_ENABLE_KEY      = "wed_schedule_enable";
    public static final String THUR_STARTUP_TIME_KEY        = "thur_startup_time";
    public static final String THUR_SHUTDOWN_TIME_KEY       = "thur_shutdown_time";
    public static final String THUR_SCHEDULE_ENABLE_KEY     = "thur_schedule_enable";
    public static final String FRI_STARTUP_TIME_KEY         = "fri_startup_time";
    public static final String FRI_SHUTDOWN_TIME_KEY        = "fri_shutdown_time";
    public static final String FRI_SCHEDULE_ENABLE_KEY      = "fri_schedule_enable";
    public static final String SAT_STARTUP_TIME_KEY         = "sat_startup_time";
    public static final String SAT_SHUTDOWN_TIME_KEY        = "sat_shutdown_time";
    public static final String SAT_SCHEDULE_ENABLE_KEY      = "sat_schedule_enable";
    public static final String SUN_STARTUP_TIME_KEY         = "sun_startup_time";
    public static final String SUN_SHUTDOWN_TIME_KEY        = "sun_shutdown_time";
    public static final String SUN_SCHEDULE_ENABLE_KEY      = "sun_schedule_enable";
    public static final String GLOBAL_STARTUP_TIME_KEY      = "global_startup_time";
    public static final String GLOBAL_SHUTDOWN_TIME_KEY     = "global_shutdown_time";
    public static final String GLOBAL_SCHEDULE_ENABLE_KEY   = "global_schedule_enable";
    
    public static final int GLOBAL_DAY_OF_WEEK				= Calendar.SATURDAY + 1;


    private SharedPreferences sp                            = null;
    
    private Handler completedHandler						= null;
    
    private static boolean enable_handle					= true;

    public static final int WRITE_START						= 0;
    public static final int WRITE_COMPLETED					= 1;
    public static final int WRITE_FAILED					= 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

        sp = this.getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE);
        completedHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int what = msg.what;
				switch (what)  {
					case WRITE_START:
						setUIState(false);
						Log.d(TAG, "Write start");
						Toast.makeText(BrowserActivity.this, 
								BrowserActivity.this.getResources().getString(R.string.write_serial_starting_string),
								5000).show();
						break;
					case WRITE_COMPLETED:
						setUIState(true);
						Log.d(TAG, "Write completed");
						Toast.makeText(BrowserActivity.this, 
								BrowserActivity.this.getResources().getString(R.string.write_serial_completed_string),
								5000).show();
						break;
					case WRITE_FAILED:
						setUIState(true);
						Log.d(TAG, "Write failed");
						Toast.makeText(BrowserActivity.this, 
								BrowserActivity.this.getResources().getString(R.string.write_serial_failed_string),
								5000).show();
						break;
					default:
						Log.d(TAG, "Valid message!");
				}
				
			}
        	
        };

		initUI();
        initHandler();
        updateView();
	}
	
	private void setUIState(boolean state) {
		// Monday
		mon_startup_edit_bt.setEnabled(state);
		mon_shutdown_edit_bt.setEnabled(state);
		mon_schedule_enable_cb.setEnabled(state);
		// Tuesday 
		tues_startup_edit_bt.setEnabled(state);
		tues_shutdown_edit_bt.setEnabled(state);
		tues_schedule_enable_cb.setEnabled(state);
		// Wednesday
		wed_startup_edit_bt.setEnabled(state);
		wed_shutdown_edit_bt.setEnabled(state);
		wed_schedule_enable_cb.setEnabled(state);
		// Thursday 
		thur_startup_edit_bt.setEnabled(state);
		thur_shutdown_edit_bt.setEnabled(state);
		thur_schedule_enable_cb.setEnabled(state);
		// Friday
		fri_startup_edit_bt.setEnabled(state);
		fri_shutdown_edit_bt.setEnabled(state);
		fri_schedule_enable_cb.setEnabled(state);
		// Saturday
		sat_startup_edit_bt.setEnabled(state);
		sat_shutdown_edit_bt.setEnabled(state);
		sat_schedule_enable_cb.setEnabled(state);
		// Sunday
		sun_startup_edit_bt.setEnabled(state);
		sun_shutdown_edit_bt.setEnabled(state);
		sun_schedule_enable_cb.setEnabled(state);
		// Global
		global_startup_edit_bt.setEnabled(state);
		global_shutdown_edit_bt.setEnabled(state);
		global_schedule_enable_cb.setEnabled(state);
		
	}

    /**
     * Use stored in SharedPreferences' data to refresh UI
     */
    private void updateView() {
    	enable_handle = false;
        Log.d(TAG, "Update widget display shutdown and startup time!");

        homepage_view.setText(getString(R.string.homepage_text_view_string)
                .concat(sp.getString(WEB_URL_KEY, DEFAULT_WEB_URL)));

        // Update widget for Monday to Sunday
        // 1.
        mon_startup_edit_bt.setText(
                formatTime(sp.getString(
                        MON_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        mon_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        MON_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        mon_schedule_enable_cb.setChecked(
                sp.getInt(MON_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 2.
        tues_startup_edit_bt.setText(
                formatTime(sp.getString(
                        TUES_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        tues_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        TUES_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        tues_schedule_enable_cb.setChecked(
                sp.getInt(TUES_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 3.
        wed_startup_edit_bt.setText(
                formatTime(sp.getString(
                        WED_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        wed_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        WED_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        wed_schedule_enable_cb.setChecked(
                sp.getInt(WED_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 4.
        thur_startup_edit_bt.setText(
                formatTime(sp.getString(
                        THUR_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        thur_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        THUR_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        thur_schedule_enable_cb.setChecked(
                sp.getInt(THUR_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 5.
        fri_startup_edit_bt.setText(
                formatTime(sp.getString(
                        FRI_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        fri_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        FRI_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        fri_schedule_enable_cb.setChecked(
                sp.getInt(FRI_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 6.
        sat_startup_edit_bt.setText(
                formatTime(sp.getString(
                        SAT_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        sat_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        SAT_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        sat_schedule_enable_cb.setChecked(
                sp.getInt(SAT_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 7.
        sun_startup_edit_bt.setText(
                formatTime(sp.getString(
                        SUN_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        sun_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        SUN_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        sun_schedule_enable_cb.setChecked(
                sp.getInt(SUN_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // Global.
        global_startup_edit_bt.setText(
                formatTime(sp.getString(
                        GLOBAL_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        global_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        GLOBAL_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        /*
        global_schedule_enable_cb.setChecked(
                sp.getInt(GLOBAL_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);
                        */

        enable_handle = true;
    }

    /**
     * Format time string stored in SharedPreference.
     * Exp:
     * 6:30     -->     06:30
     * 18:2     -->     18:20
     * 1:2      -->     01:02
     * 12:20    -->     12:20
     * @param ori_time
     * @return
     *      Formatted time string
     */
    private String formatTime(String ori_time) {
        Log.d(TAG, "***************************" + ori_time);

        int hours = Integer.valueOf(ori_time.split(":")[0]);
        int minutes = Integer.valueOf(ori_time.split(":")[1]);
        String time = "";
        if (hours < 10)
            time += "0";
        time += hours;
        time += ":";
        if (minutes < 10)
            time += "0";
        time += minutes;
        return time;
    }

    private void initHandler() {

        // Handle reboot action
        // shell: system_reboot.sh
        reboot_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Handle reboot");
                Utils.execCmd(Utils.REBOOT_BIN);
            }

        });

        // Handle manual disable display action
        // shell: screen_off.sh
        disable_display_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Handle disable display");
                Utils.execCmd(Utils.SCREEN_OFF_BIN);
            }

        });

        play_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Start open web browser to play ad");
                // Use WebView to display
                String urlText = sp.getString(BrowserActivity.WEB_URL_KEY,
                        BrowserActivity.DEFAULT_WEB_URL);   // Web URL
                Intent webIntent = new Intent(BrowserActivity.this, WebActivity.class);
                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                webIntent.putExtra("url", urlText);
                BrowserActivity.this.startActivity(webIntent); // 打开浏览器
            }
        });

        start_server_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "start_server_button clicked");
                Intent r_intent = new Intent();
                r_intent.setClass(BrowserActivity.this, ControlClient.class);
                BrowserActivity.this.startService(r_intent);
            }
        });

        stop_server_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "stop_server_button clicked");
            }
        });

        test_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Starting test!");
                SerialWriteTask task = new SerialWriteTask(BrowserActivity.this, completedHandler);
                task.start();
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

        ScheduleTimeEditListener timeListener       = new ScheduleTimeEditListener();
        ScheduleEnableEditListener enableListener   = new ScheduleEnableEditListener();
        // Set edit schedule handler to Monday to Sunday
        // 1.
        mon_startup_edit_bt.setOnClickListener(timeListener);
        mon_shutdown_edit_bt.setOnClickListener(timeListener);
        mon_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 2.
        tues_startup_edit_bt.setOnClickListener(timeListener);
        tues_shutdown_edit_bt.setOnClickListener(timeListener);
        tues_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 3.
        wed_startup_edit_bt.setOnClickListener(timeListener);
        wed_shutdown_edit_bt.setOnClickListener(timeListener);
        wed_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 4.
        thur_startup_edit_bt.setOnClickListener(timeListener);
        thur_shutdown_edit_bt.setOnClickListener(timeListener);
        thur_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 5.
        fri_startup_edit_bt.setOnClickListener(timeListener);
        fri_shutdown_edit_bt.setOnClickListener(timeListener);
        fri_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 6.
        sat_startup_edit_bt.setOnClickListener(timeListener);
        sat_shutdown_edit_bt.setOnClickListener(timeListener);
        sat_schedule_enable_cb.setOnCheckedChangeListener(enableListener);
        // 7.
        sun_startup_edit_bt.setOnClickListener(timeListener);
        sun_shutdown_edit_bt.setOnClickListener(timeListener);
        sun_schedule_enable_cb.setOnCheckedChangeListener(enableListener);

        // Global
        global_startup_edit_bt.setOnClickListener(timeListener);
        global_shutdown_edit_bt.setOnClickListener(timeListener);
        global_schedule_enable_cb.setOnCheckedChangeListener(enableListener);

    }

    public class ScheduleTimeEditListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Handle edit schedule time event");
            final SharedPreferences sp = BrowserActivity.this.getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
            final String key = (String) view.getTag();

            TimePickerDialog picker = new TimePickerDialog(BrowserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    Log.d(TAG, "Set scheduled time:\t" + i + ":" + i2 + "\tKey:" + key);

                    // 1. If edited is global startup time
                    //    Loop set schedule startup time in all startup keys
                    if (key.equals(GLOBAL_STARTUP_TIME_KEY)) {
                        for (String r_key : Utils.key_action_maps.keySet()) {
                            if (r_key.contains("startup"))
                            Utils.setScheduleTime(BrowserActivity.this
                                    , Utils.key_action_maps.get(r_key)
                                    , r_key, i + ":" + i2);
                        }
                        editor.putString(GLOBAL_STARTUP_TIME_KEY, i + ":" + i2).commit();
                        // Update startup time
                        SerialWriteTask task = new SerialWriteTask(BrowserActivity.this, completedHandler);
                        task.start();
                    }
                    // 2. If edited is global shutdown time
                    //    Loop set schedule shutdown time in all startup keys
                    else if (key.equals(GLOBAL_SHUTDOWN_TIME_KEY)) {
                        for (String r_key : Utils.key_action_maps.keySet()) {
                            if (r_key.contains("shutdown"))
                                Utils.setScheduleTime(BrowserActivity.this
                                        , Utils.key_action_maps.get(r_key)
                                        , r_key, i + ":" + i2);
                        }
                        editor.putString(GLOBAL_SHUTDOWN_TIME_KEY, i + ":" + i2).commit();
                    }
                    // 3. If edited is one day startup/shutdown time
                    // Update the day scheduled task time
                    else {
                        Utils.setScheduleTime(BrowserActivity.this
                                , Utils.key_action_maps.get(key)
                                , key, i + ":" + i2);
                        // Update startup time
                        SerialWriteTask task = new SerialWriteTask(BrowserActivity.this, completedHandler);
                        task.start();
                    }
                    updateView();
                }
            }, 0, 0, false);
            // Set TimePicker initial time from SharedPreferences
            String init_time = DEFAULT_STARTUP_TIME;
            if (key.contains("startup")) {
                init_time = sp.getString(key, DEFAULT_STARTUP_TIME);
            } else if (key.contains("shutdown")) {
                init_time = sp.getString(key, DEFAULT_SHUTDOWN_TIME);
            }
            picker.updateTime(Integer.valueOf(init_time.split(":")[0]), Integer.valueOf(init_time.split(":")[1]));
            picker.show();
        }
    }

    public class ScheduleEnableEditListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        	if (!enable_handle)
        		return;
            Log.d(TAG, "Handle edit schedule enable event\t"
                    + "key:" + compoundButton.getTag()
                    + "\tEnabled:\t" + (b ? "true" : "false"));
            String key = (String) compoundButton.getTag();
            SharedPreferences.Editor editor = sp.edit();
            // 1. If edit global schedule enable state
            //    Loop set schedule enable state from Monday to Sunday
            if (key.equals(GLOBAL_SCHEDULE_ENABLE_KEY)) {
                for (String r_key : Utils.action_enableKey_maps.values()) {
                    editor.putInt(r_key, b ? 1 : 0).commit();
                }
                updateView();
            }
            // 2. If edit one day schedule enable state
            else {
                // Don't forget commit, seriously !
                editor.putInt(key, b ? 1 : 0).commit();
            }
            SerialWriteTask task = new SerialWriteTask(BrowserActivity.this, completedHandler);
            task.start();
        }
    }


	private void initUI() {
        homepage_view = (TextView) this
                .findViewById(R.id.homepage_text_view);

		reboot_button = (Button) this.findViewById(R.id.reboot_button);
        homepage_edit_button = (Button) this.findViewById(R.id.homepage_edit_button);
        disable_display_button = (Button) this.findViewById(R.id.disable_display_button);
        play_button = (Button) findViewById(R.id.play_button);
        start_server_button = (Button) findViewById(R.id.start_server_button);
        stop_server_button = (Button) findViewById(R.id.stop_server_button);
        test_button = (Button) findViewById(R.id.test_button);


        // One week widget initial
        mon_startup_edit_bt         = (Button)findViewById(R.id.mon_set_startup_bt);
        mon_shutdown_edit_bt        = (Button)findViewById(R.id.mon_set_shutdown_bt);
        mon_schedule_enable_cb      = (CheckBox)findViewById(R.id.mon_enable_checkbox);
        tues_startup_edit_bt        = (Button)findViewById(R.id.tues_set_startup_bt);
        tues_shutdown_edit_bt       = (Button)findViewById(R.id.tues_set_shutdown_bt);
        tues_schedule_enable_cb     = (CheckBox)findViewById(R.id.tues_enable_checkbox);
        wed_startup_edit_bt         = (Button)findViewById(R.id.wed_set_startup_bt);
        wed_shutdown_edit_bt        = (Button)findViewById(R.id.wed_set_shutdown_bt);
        wed_schedule_enable_cb      = (CheckBox)findViewById(R.id.wed_enable_checkbox);
        thur_startup_edit_bt        = (Button)findViewById(R.id.thur_set_startup_bt);
        thur_shutdown_edit_bt       = (Button)findViewById(R.id.thur_set_shutdown_bt);
        thur_schedule_enable_cb     = (CheckBox)findViewById(R.id.thur_enable_checkbox);
        fri_startup_edit_bt         = (Button)findViewById(R.id.fri_set_startup_bt);
        fri_shutdown_edit_bt        = (Button)findViewById(R.id.fri_set_shutdown_bt);
        fri_schedule_enable_cb      = (CheckBox)findViewById(R.id.fri_enable_checkbox);
        sat_startup_edit_bt         = (Button)findViewById(R.id.sat_set_startup_bt);
        sat_shutdown_edit_bt        = (Button)findViewById(R.id.sat_set_shutdown_bt);
        sat_schedule_enable_cb      = (CheckBox)findViewById(R.id.sat_enable_checkbox);
        sun_startup_edit_bt         = (Button)findViewById(R.id.sun_set_startup_bt);
        sun_shutdown_edit_bt        = (Button)findViewById(R.id.sun_set_shutdown_bt);
        sun_schedule_enable_cb      = (CheckBox)findViewById(R.id.sun_enable_checkbox);
        global_startup_edit_bt      = (Button)findViewById(R.id.default_startup_time_edit_button);
        global_shutdown_edit_bt     = (Button)findViewById(R.id.default_shutdown_time_edit_button);
        global_schedule_enable_cb   = (CheckBox)findViewById(R.id.default_enable_checkbox);

        mon_startup_edit_bt.setTag(MON_STARTUP_TIME_KEY);
        mon_shutdown_edit_bt.setTag(MON_SHUTDOWN_TIME_KEY);
        mon_schedule_enable_cb.setTag(MON_SCHEDULE_ENABLE_KEY);
        tues_startup_edit_bt.setTag(TUES_STARTUP_TIME_KEY);
        tues_shutdown_edit_bt.setTag(TUES_SHUTDOWN_TIME_KEY);
        tues_schedule_enable_cb.setTag(TUES_SCHEDULE_ENABLE_KEY);
        wed_startup_edit_bt.setTag(WED_STARTUP_TIME_KEY);
        wed_shutdown_edit_bt.setTag(WED_SHUTDOWN_TIME_KEY);
        wed_schedule_enable_cb.setTag(WED_SCHEDULE_ENABLE_KEY);
        thur_startup_edit_bt.setTag(THUR_STARTUP_TIME_KEY);
        thur_shutdown_edit_bt.setTag(THUR_SHUTDOWN_TIME_KEY);
        thur_schedule_enable_cb.setTag(THUR_SCHEDULE_ENABLE_KEY);
        fri_startup_edit_bt.setTag(FRI_STARTUP_TIME_KEY);
        fri_shutdown_edit_bt.setTag(FRI_SHUTDOWN_TIME_KEY);
        fri_schedule_enable_cb.setTag(FRI_SCHEDULE_ENABLE_KEY);
        sat_startup_edit_bt.setTag(SAT_STARTUP_TIME_KEY);
        sat_shutdown_edit_bt.setTag(SAT_SHUTDOWN_TIME_KEY);
        sat_schedule_enable_cb.setTag(SAT_SCHEDULE_ENABLE_KEY);
        sun_startup_edit_bt.setTag(SUN_STARTUP_TIME_KEY);
        sun_shutdown_edit_bt.setTag(SUN_SHUTDOWN_TIME_KEY);
        sun_schedule_enable_cb.setTag(SUN_SCHEDULE_ENABLE_KEY);
        global_startup_edit_bt.setTag(GLOBAL_STARTUP_TIME_KEY);
        global_shutdown_edit_bt.setTag(GLOBAL_SHUTDOWN_TIME_KEY);
        global_schedule_enable_cb.setTag(GLOBAL_SCHEDULE_ENABLE_KEY);
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
        editText.setText(sp.getString(WEB_URL_KEY, DEFAULT_WEB_URL));

        // 2. Add ok/cancel button handler
        ok_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Update home page:\t" + editText.getText().toString());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(WEB_URL_KEY, editText.getText().toString()).commit();
                dialog.dismiss();
                updateView();
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
