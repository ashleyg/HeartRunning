package com.local.heartrunning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Displays information about the run for analysis...with SCIENCE
 *
 */

public class PostRunView extends Activity {

	Button gotoMap;
	public static WorkoutData data;
	float target;

	
	private void back() {
		Intent intentStopRunning = new Intent(this, MainMenu.class);
    	startActivity(intentStopRunning);
	}
	
	@Override
	public void onBackPressed() {
		back();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_run_view);
		
		//Get file name from previous activity
		Bundle extras = getIntent().getExtras();
		String file = extras.getString("file");
		target = extras.getFloat("target");
		
		// Load GUI Components
		Button back = (Button)findViewById(R.id.back_run_view);
        back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				back();
			}
		});
		
		
        gotoMap = (Button)findViewById(R.id.goto_map);
        gotoMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				loadMap();			
			}
		});
        
        
        
		
        
        String[] files = getFileList();
        final Spinner spinner = (Spinner) findViewById(R.id.workout_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, files);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
				changeWorkout(convertToFileName(spinner.getItemAtPosition(pos).toString()));
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
        	
        });
        //spinner.setSelection(files.length -1);
        
        changeWorkout(file);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_post_run_map_view, menu);
		return true;
	}
	
	private void changeWorkout(String file) {
		
		Toast toast = Toast.makeText(getApplicationContext(), "Processing Run", Toast.LENGTH_LONG);
    	toast.show();
		gotoMap.setEnabled(false);
		loadingDataFields();
		//If we've got a file name, access it
        if (file.length() > 0) {
        	data = new WorkoutData(getDataPoints(file), target);
        }
        //If not, get the latest file
        else if (getFileList().length > 0) {
        	data = new WorkoutData(getDataPoints(), target);
        }
        //The user deserves a medal if this is fired off
        else {
        	data = new WorkoutData();
        }
        
        //Check if gps data exists
        if (!data.hasGPS()) {
        	gotoMap.setEnabled(false);
        }
        else {
        	gotoMap.setEnabled(true);
        }
        
        populateDataFields();
	}
	
	private Date convertToHumanReadable(String file) {
		return new Date(Long.parseLong(file.substring(0, file.lastIndexOf('.'))));
	}
	
	private String convertToFileName(String date) {
		Date d = new Date();
		try {
			d = new SimpleDateFormat("dd/mm/yy hh:mm a").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Long.toString(d.getTime()) + ".xml";
	}
	
	
	
	private void populateDataFields() {
		//Calculate and display the run data
       	TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText(data.getDistance());
        
        TextView hr = (TextView) findViewById(R.id.hr);
        hr.setText(data.getAvgHR());
        
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(data.getTime());
        
        TextView speed = (TextView) findViewById(R.id.speed);
        speed.setText(data.getSpeed());
        
        TextView heartscore = (TextView) findViewById(R.id.heartscore);
        heartscore.setText(data.getTargetTime());

	}
	
	private void loadingDataFields() {
		
		String calculating = "Calculating";
		
		//Calculate and display the run data
       	TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText(calculating);
        
        TextView hr = (TextView) findViewById(R.id.hr);
        hr.setText(calculating);
        
        TextView time = (TextView) findViewById(R.id.time);
        time.setText(calculating);
        
        TextView speed = (TextView) findViewById(R.id.speed);
        speed.setText(calculating);
        
        TextView heartscore = (TextView) findViewById(R.id.heartscore);
        heartscore.setText(calculating);
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
		Document doc = loadData(convertToFileName(getFileList()[0]));
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
		String[] files = getApplicationContext().fileList();
		ArrayList<String> array = new ArrayList<String>();
		for (String s : files) {
			if (s.endsWith(".xml")) {
				array.add(s);
			}
		}

		SimpleDateFormat formatter;
		ArrayList<String> dateList = new ArrayList<String>();
		for (String s : array) {
			formatter = new SimpleDateFormat("dd/mm/yy hh:mm a");
			dateList.add(formatter.format(convertToHumanReadable(s)));
		}
		files = new String[dateList.size()];
		dateList.toArray(files);
		return files;
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
			target = Float.parseFloat(d.getDocumentElement().getAttribute("target"));
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