package com.local.heartrunning;

import android.location.Location;

public class DataPoint {
	
	private Location location;
	private int heartRate;
	
	public DataPoint(Location location, int heartRate) {
		this.location = location;
		this.heartRate = heartRate;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public int getHeartRate() {
		return this.heartRate;
	}
}
