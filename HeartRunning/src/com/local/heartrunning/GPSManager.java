package com.local.heartrunning;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Manages the GPS signal
 * @author Joshua
 *
 */
public class GPSManager {
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private ArrayList<MapDataPoint> mapDataPoints;
	private static final String debugTag = "GPS";
	
	private RunningView parent;

	/**
	 * Constructor
	 * @param context is the context from which this is called
	 */
	public GPSManager(Context context) {
		
		//Initialise location manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			//Called when the location has changed
		    public void onLocationChanged(Location location) {
		    	newLocation(location);
		    }

		    //Called when the location provider changes status (i.e. Available, out of service etc)
		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}

		};
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
		mapDataPoints = new ArrayList<MapDataPoint>();
	}
	
	//Stop getting location updates
	public void destroy() {
		locationManager.removeUpdates(locationListener);
	}
	
	public void linkRunnignView(RunningView parent) {
		this.parent = parent;
	}
	
	protected void newLocation(Location location) {
		mapDataPoints.add(new MapDataPoint(location,parent.calculateBPM()));
		Log.i(debugTag, String.valueOf(mapDataPoints.size()));
	}
	
	public ArrayList<MapDataPoint> getMapDataPoints() {
		return mapDataPoints;
	}
}
