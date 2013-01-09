package com.local.heartrunning;


/**
 * Helpful class to tell you the zone working in dependent on
 * bpm
 *
 */
public class HeartRate {
	
	public enum Zone {
		DEAD, NORMAL, FAT, AEROBIC, ANAEROBIC, HEARTATTACK
	}
	
	float bpm;
	Zone zone;
	
	public HeartRate(float beats) {
		bpm = beats;
		setZone();
	}
	
	/**
	 * Calculate zone working in
	 */
	private void setZone() {
		if (bpm < 10) {
			zone = Zone.DEAD;
		}
		else if (bpm < 220*0.5) {
			zone = Zone.NORMAL;
		}
		else if (bpm < 220*0.6) {
			zone = Zone.FAT;
		}
		else if (bpm < 220*0.8) {
			zone = Zone.AEROBIC;
		}
		else if (bpm < 220) {
			zone = Zone.ANAEROBIC;
		}
		else {
			zone = Zone.HEARTATTACK;
		}
	}
	
	public float getBPM() {
		return bpm;
	}
	
	public Zone getZone() {
		return zone;
	}
	
	public String getZoneAsString() {
		return zone.toString();
	}
	

	
}
