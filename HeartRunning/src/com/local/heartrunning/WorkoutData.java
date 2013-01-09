package com.local.heartrunning;

import java.util.ArrayList;

import com.local.heartrunning.HeartRate.Zone;

/**
 * Analyse the run data...with SCIENCE
 * @author Joshua
 *
 */
public class WorkoutData {

	ArrayList<MapDataPoint> points;
	//Raw Metrics
	float averageHeartRate, distance;
	long time;
	
	//Analysed Metrics
	float speed;
	float fat, aerobic, anaerobic;
	
	/**
	 * Constructor
	 * @param pts
	 */
	public WorkoutData (ArrayList<MapDataPoint> pts) {
		points = pts;
		calculateRawMetrics();
		analyseData();
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
		for (MapDataPoint p : RunningView.gps.getMapDataPoints()) {
			averageHeartRate += p.getBPM();
		}
		averageHeartRate /= RunningView.gps.getMapDataPoints().size();
		
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
		long fatTime = 0, aerobicTime = 0, anaerobicTime = 0;
		for (int i = 1; i < points.size(); i++) {
			Zone zone = points.get(i).getHeartRate().getZone();
			long gapTime = points.get(i).getTime() - points.get(i-1).getTime();;
			switch(zone) {
				case FAT:
					fatTime += gapTime;
					break;
				case AEROBIC:
					aerobicTime += gapTime;
					break;
				case ANAEROBIC:
					anaerobicTime += gapTime;
					break;
				default:
					break;
					
			}
		}
		float unitTime = 100/((float) time);
		fat = unitTime*((float)fatTime);
		aerobic = unitTime*((float)aerobicTime);
		anaerobic = unitTime*((float)anaerobicTime);
	}
	
	public String getSpeed() {
		return String.format("%.2f", speed) + " km/h";
	}
	
	public String getFat() {
		return Integer.toString(Math.round(fat)) + "%";
	}
	
	public String getAerobic() {
		return Integer.toString(Math.round(aerobic)) + "%";
	}
	
	public String getAnaerobic() {
		return Integer.toString(Math.round(anaerobic)) + "%";
	}
	
	
}
