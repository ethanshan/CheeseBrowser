package net.codingpark.cheesebrowser.entity;

import java.util.ArrayList;

/**
 * Encapsulate ArrayList stored DayInfo object, help to
 * obtain the DayInfo object by any param of DayInfo
 * @author ethanshan
 *
 */
public class ScheduleSet {
	private ArrayList<DayInfo>	scheduleSet	= null;

	public ScheduleSet() {
		scheduleSet = new ArrayList<DayInfo>();
	}
	
	public void addDayInfo(DayInfo info) {
		scheduleSet.add(info);
	}
	
	public DayInfo getByDayOfWeek(int day) {
		for(DayInfo info : scheduleSet) {
			if (info.getDay_of_week() == day)
				return info;
		}
		return null;
	}
	
	public DayInfo getByStartupTimeKey(String startup_time_key) {
		for (DayInfo info : scheduleSet) {
			if (info.getStartup_time_key().equals(startup_time_key))
				return info;
		}
		return null;
	}
	
	public DayInfo getByShutdownTimeKey(String shutdown_time_key) {
		for (DayInfo info : scheduleSet) {
			if (info.getShutdown_time_key().equals(shutdown_time_key)) {
				return info;
			}
		}
		return null;
	}
	
	public DayInfo getByStartupAction(String startup_action) {
		for (DayInfo info : scheduleSet) {
			if (info.getStartup_action().equals(startup_action))
				return info;
		}
		return null;
	}
	
	public DayInfo getByShutdownAction(String shutdown_action) {
		for (DayInfo info : scheduleSet) {
			if (info.getShutdown_action().equals(shutdown_action))
				return info;
		}
		return null;
	}
	
	public ArrayList<DayInfo> getSet() {
		return scheduleSet;
	}

}
