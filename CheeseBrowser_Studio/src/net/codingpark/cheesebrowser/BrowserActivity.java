package net.codingpark.cheesebrowser;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

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

	//private TextView startup_time_view 			= null;
	//private TextView shutdown_time_view 		= null;
    private TextView homepage_view              = null;
 
	private Button startup_time_edit_button 		= null;
	private Button shutdown_time_edit_button 		= null;
	private Button reboot_button 					= null;
    private Button homepage_edit_button             = null;
    private Button disable_display_button           = null;

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
    public static final String PREFERENCE_DATA              = "MyData";

    // Auto startup/shutdown action
    public static final String AUTO_STARTUP_ACTION          = "net.codingpark.AUTO_STARTUP_ACTION";
    public static final String AUTO_SHUTDOWN_ACTION         = "net.codingpark.AUTO_SHUTDOWN_ACTION";

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
    public static final String STARTUP_TIME_KEY             = "startup_time";
    public static final String SHUTDOWN_TIME_KEY            = "shutdown_time";
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

    // Scheduled execute binary file name
    public static final String REBOOT_BIN                   = "system_reboot.sh";
    public static final String SCREEN_ON_BIN                = "screen_on.sh";
    public static final String SCREEN_OFF_BIN               = "screen_off.sh";

    private SharedPreferences sp                            = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

        sp = this.getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE);

		initUI();
        initHandler();
        updateView();
	}

    /**
     * Use stored in SharedPreferences' data to refresh UI
     */
    private void updateView() {
        Log.d(TAG, "Update widget display shutdown and startup time!");
        //SharedPreferences sp = getSharedPreferences(
                //BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
        /*
        startup_time_view.setText(getString(R.string.startup_time_text_view_string)
                .concat(sp.getString(STARTUP_TIME_KEY, "6:30")));
        shutdown_time_view.setText(getString(R.string.shutdown_time_text_view_string)
                .concat(sp.getString(SHUTDOWN_TIME_KEY, "18.30")));
                */

        Log.d(TAG, "Startup time " + sp.getString(STARTUP_TIME_KEY, "100"));
        homepage_view.setText(getString(R.string.homepage_text_view_string)
                .concat(sp.getString(WEB_URL_KEY, DEFAULT_WEB_URL)));

        // Update widget for Monday to Sunday
        // 1.
        mon_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.MON_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        mon_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.MON_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        mon_schedule_enable_cb.setChecked(
                sp.getInt(MON_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 2.
        tues_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.TUES_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        tues_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.TUES_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        tues_schedule_enable_cb.setChecked(
                sp.getInt(TUES_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 3.
        wed_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.WED_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        wed_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.WED_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        wed_schedule_enable_cb.setChecked(
                sp.getInt(WED_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 4.
        thur_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.THUR_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        thur_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.THUR_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        thur_schedule_enable_cb.setChecked(
                sp.getInt(THUR_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 5.
        fri_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.FRI_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        fri_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.FRI_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        fri_schedule_enable_cb.setChecked(
                sp.getInt(FRI_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 6.
        sat_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.SAT_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        sat_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.SAT_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        sat_schedule_enable_cb.setChecked(
                sp.getInt(SAT_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);

        // 7.
        sun_startup_edit_bt.setText(
                formatTime(sp.getString(
                        this.SUN_STARTUP_TIME_KEY, DEFAULT_STARTUP_TIME)));
        sun_shutdown_edit_bt.setText(
                formatTime((sp.getString(
                        this.SUN_SHUTDOWN_TIME_KEY, DEFAULT_SHUTDOWN_TIME))));
        sun_schedule_enable_cb.setChecked(
                sp.getInt(SUN_SCHEDULE_ENABLE_KEY, SCHEDULE_ENABLE)
                        == SCHEDULE_ENABLE);
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

    }

    public class ScheduleTimeEditListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Handle edit schedule time event");
            final String key = (String) view.getTag();
            TimePickerDialog picker = new TimePickerDialog(BrowserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    Log.d(TAG, "Set scheduled time:\t" + i + ":" + i2 + "\tKey:" + key);
                    // Update startup scheduled task time
                    setScheduleTime(BrowserActivity.this
                            , Utils.key_action_maps.get(key)
                            , key, i + ":" + i2);
                    updateView();
                }
            }, 0, 0, false);
            picker.show();
        }
    }

    public class ScheduleEnableEditListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Log.d(TAG, "Handle edit schedule enable event\t"
                    + "key:" + compoundButton.getTag()
                    + "\tEnabled:\t" + (b ? "true" : "false"));
            String key = (String) compoundButton.getTag();

            SharedPreferences.Editor editor = sp.edit();
            // Don't forget commit, seriously !
            editor.putInt(key, b ? 1 : 0).commit();
            updateView();
        }
    }


	private void initUI() {
        homepage_view = (TextView) this
                .findViewById(R.id.homepage_text_view);


		startup_time_edit_button = (Button) this
				.findViewById(R.id.startup_time_edit_button);
		shutdown_time_edit_button = (Button) this
				.findViewById(R.id.shutdown_time_edit_button);
		reboot_button = (Button) this.findViewById(R.id.reboot_button);
        homepage_edit_button = (Button) this.findViewById(R.id.homepage_edit_button);
        disable_display_button = (Button) this.findViewById(R.id.disable_display_button);

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
                //SharedPreferences sp = BrowserActivity.this
                        //.getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE);
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

    public  void setScheduleTime(Context ctx, String action, String key, String time) {
        Log.d(TAG, "Utils setScheduleTime key:" + key + "\taction:" + action + "\ttime:\t" + time);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        //SharedPreferences sp = this.getSharedPreferences(
                //BrowserActivity.PREFERENCE_DATA, Context.MODE_PRIVATE);
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
                PendingIntent.getBroadcast(this, 0, r_intent, 0);
        alarmManager.cancel(r_pending_intent);
        // 3. Use new time to create auto startup schedule task
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), BrowserActivity.ONE_DAY_TIME_MILLIS,
                r_pending_intent);
    }
}
