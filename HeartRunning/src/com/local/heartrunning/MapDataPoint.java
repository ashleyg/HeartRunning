package com.local.heartrunning;

import android.location.Location;

public class MapDataPoint {
	Location location;
	float bpm;
	
	public MapDataPoint(Location location, float bpm) {
		this.location = location;
		this.bpm = bpm;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public float getBPM() {
		return this.bpm;
	}
}
