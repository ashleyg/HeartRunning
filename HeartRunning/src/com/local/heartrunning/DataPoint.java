package com.local.heartrunning;

import android.location.Location;

public class DataPoint {
	
	private Location location;
	//private int heartRate;
	private int brightness;
	private long time; // This is stored in milliseconds since Jan 1st 1970.
	
	public DataPoint(Location location, int brightness) {
		this.location = location;
		//this.heartRate = heartRate;
		this.brightness = brightness;
		this.time = System.currentTimeMillis();
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	/*
	public int getHeartRate() {
		return this.heartRate;
	}
	*/
	
	public long getTime() {
		return this.time;
	}
	
	public int getBrightness() {
		return this.brightness;
	}
}
