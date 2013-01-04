package com.local.heartrunning;

import com.google.android.maps.GeoPoint;

import android.location.Location;

/**
 * The object class to hold workout data for a specific moment in time
 *
 */

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
	
	public GeoPoint getLocationAsGeoPoint() {
		int latitude = (int) (location.getLatitude() * 1E6);
		int longitude = (int) (location.getLongitude() * 1E6);
		return new GeoPoint(latitude, longitude);
	}
	
	public boolean hasLocationData() {
		if (location == null) {
			return false;
		}
		return true;
	}
	
	public boolean hasBPM() {
		if (bpm <= 0) {
			return false;
		}
		return true;
	}
}
