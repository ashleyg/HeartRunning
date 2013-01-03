package com.local.heartrunning;

import android.location.Location;

public class DataPoint {
	//private int heartRate;
	private int brightness;
	private long time; // This is stored in milliseconds since Jan 1st 1970.
	
	public DataPoint(int brightness) {
		//this.heartRate = heartRate;
		this.brightness = brightness;
		this.time = System.currentTimeMillis();
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
