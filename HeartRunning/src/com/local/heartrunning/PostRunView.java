package com.local.heartrunning;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays information about the run for analysis...with SCIENCE
 *
 */

public class PostRunView extends Activity {

	Button gotoMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_run_view);
		// Load GUI Components
        gotoMap = (Button)findViewById(R.id.goto_map);
        
        gotoMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadMap();			
			}
		});
        
        //Calculate and display the run data
       	TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText(calculateDistance() + " km");
        
        TextView hr = (TextView) findViewById(R.id.hr);
        hr.setText(calculateAvgHR());
        
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(calculateTime());
        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_post_run_map_view, menu);
		return true;
	}
	
	/**
	 * Load the map view
	 */
	public void loadMap() {
		Intent intentMapView = new Intent(this, PostRunMapView.class);
        startActivity(intentMapView);	
    }
	
	/**
	 * Calculate the distance the person has ventured
	 * @return the distance
	 */
	private String calculateDistance() {
		float x = 0;
		ArrayList<MapDataPoint> points = RunningView.gps.getMapDataPoints();
		for (int i = 1; i < points.size(); i++) {
			x += points.get(i-1).getLocation().distanceTo(points.get(i).getLocation());
		}
		x /= 1000;
		return String.format("%.2f", x);
	}
	
	/**
	 * Calculate the average HR of the person
	 * @return the average HR, or "Unavailable"
	 */
	private String calculateAvgHR() {
		float total = 0;
		for (MapDataPoint p : RunningView.gps.getMapDataPoints()) {
			total += p.getBPM();
		}
		total /= RunningView.gps.getMapDataPoints().size();
		int beats = Math.round(total);
		if (beats <= 0) {
			return "Unavailable";
		}
		else {
			return Integer.toString(beats) + " bpm";
		}
	}
	
	/**
	 * Calculate the time spent on the run
	 * @return time spent exercising
	 */
	@SuppressLint("NewApi")
	private String calculateTime() {
		ArrayList<MapDataPoint> points = RunningView.gps.getMapDataPoints();
		long time = points.get(points.size()-1).getTime() - points.get(0).getTime();
		
		long hours = time/(1000*60*60);
		long minutes = (time - (hours*1000*60*60)) / (1000*60);
		long seconds = (time - (hours*1000*60*60) - (minutes*1000*60))/1000;
		
		String s = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return s;
	}

}