package com.local.heartrunning;

import java.util.ArrayList;


/**
 * Analyse the run data...with SCIENCE
 *
 */
public class WorkoutData {

	ArrayList<MapDataPoint> points;
	
	private final float targetBPM;
	
	//Raw Metrics
	float averageHeartRate, distance;
	long time;
	
	//Analysed Metrics
	float speed, targetPercentage;
	
	/**
	 * Constructor
	 * @param pts
	 */
	public WorkoutData (ArrayList<MapDataPoint> pts, float target) {
		points = pts;
		targetBPM = target;
		calculateRawMetrics();
		analyseData();
	}
	
	/**
	 * Empty constructor so we don't crash
	 */
	public WorkoutData() {
		points = new ArrayList<MapDataPoint>();
		averageHeartRate = 0;
		distance = 0;
		time = 0;
		speed = 0;
		targetPercentage = 0;
		targetBPM = -1000;
	}
	
	public float getTargetBPM() {
		return targetBPM;
	}
	
	public ArrayList<MapDataPoint> getMapDataPoints() {
		return points;
	}
	
	/**
	 * Check if gps data exists so we can plot on a map
	 * @return
	 */
	public boolean hasGPS() {
        for (MapDataPoint m : points) {
        	if (m.hasLocationData()) {
        		return true;
		  	}
        }
        return false;
	}
	
	/**
	 * Calculate distance, time and average heart rate
	 */
	private void calculateRawMetrics() {
		distance = 0;
		for (int i = 1; i < points.size(); i++) {
			distance += points.get(i-1).getLocation().distanceTo(points.get(i).getLocation());
		}
		distance /= 1000;		
		
		averageHeartRate = 0;
		for (MapDataPoint p : points) {
			averageHeartRate += p.getHeartRate();
		}
		averageHeartRate /= points.size();
		
		time = 0;
		if (points.size() > 1) {
			time = points.get(points.size()-1).getTime() - points.get(0).getTime();
		}
			
	}
	
	public String getDistance() {
		return String.format("%.2f", distance) + " km";
	}
	
	public String getTime() {
		long hours = time/(1000*60*60);
		long minutes = (time - (hours*1000*60*60)) / (1000*60);
		long seconds = (time - (hours*1000*60*60) - (minutes*1000*60))/1000;

		String s = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return s;
	}
	
	public String getAvgHR() {
		
		if (averageHeartRate <= 0) {
			return "Unavailable";
		}
		else {
			int beats = Math.round(averageHeartRate);
			return Integer.toString(beats) + " bpm";
		}
	}
	
	
	/**
	 * Analyse the data
	 */
	private void analyseData() {
		//Calculate speed
		float t = 1000*60*60;
		t = ((float)time)/t;
		speed = distance/t;
		
		//Analyse zones
		long targetTime = 0;
		for (int i = 1; i < points.size(); i++) {
			float bpm = points.get(i).getHeartRate();
			long gapTime = points.get(i).getTime() - points.get(i-1).getTime();;
			if (bpm > targetBPM - 10 && bpm < targetBPM + 10) {
				targetTime += gapTime;
			}

		}
		float unitTime = 100/((float) time);
		targetPercentage = unitTime*((float)targetTime);

	}
	
	public String getSpeed() {
		return String.format("%.2f", speed) + " km/h";
	}
	
	public String getTargetTime() {
		return Integer.toString(Math.round(targetPercentage));
	}
	
	
	
}
