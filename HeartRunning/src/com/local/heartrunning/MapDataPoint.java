package com.local.heartrunning;

import java.util.Date;
import org.w3c.dom.*;

import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.util.Log;

/**
 * The object class to hold workout data for a specific moment in time
 *
 */

public class MapDataPoint {
	private Location location;
	private HeartRate bpm;
	private Date time;
	
	public MapDataPoint(Location location, float bpm) {
		this.location = location;
		this.bpm = new HeartRate(bpm);
		this.time = new Date(System.currentTimeMillis());
	}
	
	/**
	 * Parse a document element to construct a new MapDataPoint
	 * @param e is the Element with name data
	 */
	public MapDataPoint(Element e) {
		//Get time
		Node node = (Node) e.getElementsByTagName("time").item(0);
		this.time = new Date(Long.parseLong(node.getChildNodes().item(0).getNodeValue()));
		
		//Get the beats per minute
		node = (Node) e.getElementsByTagName("bpm").item(0);
		this.bpm = new HeartRate(Float.parseFloat(node.getChildNodes().item(0).getNodeValue()));
		
		//Get the location
		Element element = (Element) e.getElementsByTagName("location").item(0);
		node = (Node) element.getElementsByTagName("latitude").item(0);
		double latitude = Double.parseDouble(node.getChildNodes().item(0).getNodeValue());
		node = (Node) element.getElementsByTagName("longitude").item(0);
		double longitude = Double.parseDouble(node.getChildNodes().item(0).getNodeValue());
		location = new Location("dummyprovider");
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public HeartRate getHeartRate() {
		return bpm;
	}
	
	public float getBPM() {
		return this.bpm.getBPM();
	}
	
	public GeoPoint getLocationAsGeoPoint() {
		int latitude = (int) (location.getLatitude() * 1E6);
		int longitude = (int) (location.getLongitude() * 1E6);
		Log.i("Latitude", Double.toString(getLocation().getLatitude()));
		Log.i("LatitudeGP", Double.toString(latitude));
		return new GeoPoint(latitude, longitude);
	}
	
	public long getTime() {
		return time.getTime();
	}
	
	public boolean hasLocationData() {
		if (location == null) {
			return false;
		}
		return true;
	}
	
	public boolean hasBPM() {
		if (bpm.getBPM() <= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Convert the object variables into xml
	 * <data>
	 * 		<time></time>
	 * 		<location>
	 * 			<latitude></latitude>
	 * 			<longitude></longitude>
	 * 		</location>
	 * 		<bpm></bpm>
	 * </data>
	 * @return
	 */
	public String getXml() {
		String xml = "";
		xml += "<data>";
		//Add time
		xml += "<time>";
		xml += Long.toString(getTime());
		xml += "</time>";
		Log.i("Latitude", Double.toString(getLocation().getLatitude()));

		//Add location
		xml += "<location>";
		xml += "<latitude>";
		xml += Double.toString(getLocation().getLatitude());
		xml += "</latitude>";
		xml += "<longitude>";
		xml += Double.toString(getLocation().getLongitude());
		xml += "</longitude>";
		xml += "</location>";
				
		//Add Heartrate
		xml += "<bpm>";
		xml += Float.toString(bpm.getBPM());
		xml += "</bpm>";
				
				
		xml += "</data>";
		
		return xml;
	}
	
	
}
