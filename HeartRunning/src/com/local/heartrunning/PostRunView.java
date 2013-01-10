package com.local.heartrunning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
	public static WorkoutData data;

	
	private void back() {
		Intent intentStopRunning = new Intent(this, MainMenu.class);
    	startActivity(intentStopRunning);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_run_view);
		
		//Get file name from previous activity
		Bundle extras = getIntent().getExtras();
		String file = extras.getString("file");
		
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
        
        //If we've got a file name, access it
        if (file.length() > 0) {
        	data = new WorkoutData(getDataPoints(file));
        }
        //If not, get the latest file
        else if (getFileList().length > 0) {
        	data = new WorkoutData(getDataPoints());
        }
        //The user deserves a medal if this is fired off
        else {
        	data = new WorkoutData();
        }

        //Check if gps data exists
        if (!data.hasGPS()) {
        	gotoMap.setEnabled(false);
        }
        
        
        
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
	
	/**
	 * Gets data points from an xml file
	 * @param file is the name of the file
	 * @return
	 */
	private ArrayList<MapDataPoint> getDataPoints(String file) {
		Document doc = loadData(file);
		return parseDocument(doc);
	}
	
	/**
	 * Get the data points from the first file in the list
	 * @return
	 */
	private ArrayList<MapDataPoint> getDataPoints() {
		Document doc = loadData(getFileList()[0]);
		return parseDocument(doc);
	}
	
	/**
	 * Parse an xml file
	 * @param doc
	 * @return
	 */
	private ArrayList<MapDataPoint> parseDocument(Document doc) {
		ArrayList<MapDataPoint> points = new ArrayList<MapDataPoint>();
		//For each node of data, add to our list of points
		NodeList nl = (NodeList) doc.getElementsByTagName("data");
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			points.add(new MapDataPoint(e));
		}
		return points;
	}
	
	/**
	 * Get a list of files we store
	 * @return
	 */
	private String[] getFileList() {
		return getApplicationContext().fileList();
	}
	
	/**
	 * Load an xml file from internal storage
	 * @param fileName
	 * @return
	 */
	private Document loadData(String fileName) {
		FileInputStream fis;
		Document d = null;
		try {
			fis = openFileInput(fileName);
			String file = "";
			int temp;
			while ((temp = fis.read()) != -1) {
				file += (char) temp;
			}
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			d = builder.parse(new InputSource(new StringReader(file)));
			return d;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return d;
	}

	
	

}