package net.codingpark.cheesebrowser.entity;

import java.util.Calendar;

public class DayInfo {

	private int day_of_week 				= Calendar.SUNDAY;
	private String startup_time_key			= "";
	private String shutdown_time_key		= "";
	private String startup_action			= "";
	private String shutdown_action			= "";
	private String schedule_enable_key		= "";

	public DayInfo() {
		
	}

	public int getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(int day_of_week) {
		this.day_of_week = day_of_week;
	}

	public String getStartup_time_key() {
		return startup_time_key;
	}

	public void setStartup_time_key(String startup_time_key) {
		this.startup_time_key = startup_time_key;
	}

	public String getShutdown_time_key() {
		return shutdown_time_key;
	}

	public void setShutdown_time_key(String shutdown_time_key) {
		this.shutdown_time_key = shutdown_time_key;
	}

	public String getStartup_action() {
		return startup_action;
	}

	public void setStartup_action(String startup_action) {
		this.startup_action = startup_action;
	}

	public String getShutdown_action() {
		return shutdown_action;
	}

	public void setShutdown_action(String shutdown_action) {
		this.shutdown_action = shutdown_action;
	}

	public String getSchedule_enable_key() {
		return schedule_enable_key;
	}

	public void setSchedule_enable_key(String schedule_enable_key) {
		this.schedule_enable_key = schedule_enable_key;
	}


	
}
