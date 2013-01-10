package com.local.heartrunning;

import java.util.ArrayList;

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
	WorkoutData data;
	
	private void back() {
		Intent intentStopRunning = new Intent(this,RunningView.class);
    	startActivity(intentStopRunning);
	}
	
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
        
        Button back = (Button)findViewById(R.id.back_run_view);
        back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				back();
			}
		});
        
        boolean hasGPS = false;
        
        for (MapDataPoint m : RunningView.gps.getMapDataPoints()) {
        	if (m.hasLocationData()) {
        		hasGPS = true;
        		break;
        	}
        }
        if (!hasGPS) {
        	gotoMap.setEnabled(false);
        }
        
        data = new WorkoutData(RunningView.gps.getMapDataPoints());
        
        //Calculate and display the run data
       	TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText(data.getDistance());
        
        TextView hr = (TextView) findViewById(R.id.hr);
        hr.setText(data.getAvgHR());
        
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(data.getTime());
        
        TextView speed = (TextView) findViewById(R.id.speed);
        speed.setText(data.getSpeed());
        
        TextView fat = (TextView) findViewById(R.id.fat);
        fat.setText(data.getFat());
        
        TextView aerobic = (TextView) findViewById(R.id.aerobic);
        aerobic.setText(data.getAerobic());
        
        TextView anaerobic = (TextView) findViewById(R.id.anaerobic);
        anaerobic.setText(data.getAnaerobic());
        
        
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

	
	

}