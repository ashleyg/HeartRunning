package com.local.heartrunning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RunningView extends Activity {

	// GUI Components
	Button finishRunningButton;
	TextView hrText;
	GraphView graph;
	
	// Other stuff
	private GPSManager gps;	
	private Camera mCamera;
    private CameraPreview mPreview;
    
    // Data
    ArrayList<DataPoint> data;
    int runningAverage = -1;
    float targetBPM;
    float oldBPM = 70.0f; 
    int area = -1; //Search area
    
    // Sound stuff
    MediaPlayer mpRunFaster;
    MediaPlayer mpRunSlower;
    float timeBetweenMessages = 5000;
    float nextMessage = 0;
    
    private void openCamera() {
    	mCamera = getCameraInstance();
        // The following turns the flash on
        Parameters p = mCamera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(p);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
        this.checkCameraHardware(this);
        
        // Initialize the data
        data = new ArrayList<DataPoint>();
        
        // Initialize the GPS view
        gps = new GPSManager(this);
        gps.linkRunnignView(this);
        
        // Load GUI Components
        finishRunningButton = (Button)findViewById(R.id.button_finish_run);
        finishRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopRunning();
			}
		});
        
        hrText = (TextView)findViewById(R.id.hrText);
        graph = (GraphView)findViewById(R.id.heart_rate_graph);
        graph.linkData(data);
        
        // Create an instance of Camera
        openCamera();
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mPreview.linkParent(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        // Media player
        mpRunFaster = MediaPlayer.create(this, R.raw.runfaster);
        mpRunSlower = MediaPlayer.create(this, R.raw.runslower);
        
        //Get file name from previous activity
  		Bundle extras = getIntent().getExtras();
  		int speed = extras.getInt("speed");
        
        setTargetBPM(speed);
        playEncouragement(); // might as well give some starting encouragement
        //mpRunFaster.start();
    }
    
    public void playEncouragement(){
    	//TODO oldBPM needs to be the current BPM
    	//Don't worry about oldBPM being current BPM as it'll only be a few 100ms out
    	//at the worst of time - AG
    	if(oldBPM < ((double) (targetBPM - 5.0))){
    		playRunFaster();
    	}else if(oldBPM > ((double)(targetBPM + 5.0))){
    		playRunSlower();
    	}else {
    		playWellDone();
    	}
    }
    
    public void setTargetBPM(int Speed){
    	if(Speed == 0){
    		//slow 100
    		targetBPM = (float)100.0;
    	}else if(Speed == 1){
    		//medium 120 
    		targetBPM = (float)120.0;
    	}else{
    		//fast 140 
    		targetBPM = (float)140.0;
    	}
    	Log.i("Target", Float.toString(targetBPM));
    }
    
    private void updatePlayStats() {
    	nextMessage = System.currentTimeMillis() + timeBetweenMessages;
    }
    
    public void playWellDone() {
    	//TODO - Make a decent run normally noise
    	playRunFaster();
    }
    
    public void playRunFaster() {
    	/*
    	mpRunFaster.reset();
    	try {
			mpRunFaster.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
    	
    	if(System.currentTimeMillis() > nextMessage) {
    		mpRunFaster.start();
    		updatePlayStats();
    	}
    }
    
    public void playRunSlower() {
    	/*
    	mpRunSlower.reset();
    	try {
    		mpRunSlower.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
    	if(System.currentTimeMillis() > nextMessage) {
    		mpRunSlower.start();
    		updatePlayStats();
    	}
    }
    
    //public void processImage(Bitmap bitmap) {
    public void processImage(byte[] imageData, int width, int height) {
    	int total = 0;
    	
    	//Loop through the first width*height which are apparently
    	//http://stackoverflow.com/questions/5272388/need-help-with-androids-nv21-format 
    	
    	//NOTE the imageData.size() != width*height so please please don't do enhanced forloop.
    	for(int x = 0; x < width*height; x++) {
    		total += (int)imageData[x];
    	}
    	
    	//double avgBrightness = total / (width*height);
    	int avgBrightness = total;
    	
    	//Update the running average
    	if(this.runningAverage == -1) {
    		this.runningAverage = avgBrightness;
    	} else {
    		long tmpRA = data.size() * this.runningAverage;
    		tmpRA += avgBrightness;
        	this.runningAverage = (int) (tmpRA / (data.size() + 1));	
    	}
    	//Log.d("running",""+runningAverage);
    	
    	//hrText.setText(avgBrightness+" bpm");
    	//Log.d("avg",""+avgBrightness);
    	data.add(new DataPoint(avgBrightness));
    	graph.invalidate();
    	
    	
    	this.calculateBPM();
    	this.playEncouragement();
    	
    }
    
    public int getSmoothedPoint(int index) {
    	int smoothingRange = 2;
    	int total = 0;
    	for(int i=index-smoothingRange; i<index+smoothingRange; i++) {
    		total = data.get(i).getBrightness();
    	}
    	return Math.round((float)total/(2*smoothingRange+1));
    }
    
    public float calculateBPM() {
    	
    	int wait = 200;
    	if(data.size() > wait + area) {  
    		ArrayList<DataPoint> peaks = new ArrayList<DataPoint>();
	    	boolean goingUp = false;
	    	
	    	long tba = data.get(data.size()-wait).getTime()-data.get(data.size()-wait-area).getTime();
	    	Log.d("BAH","TBA: "+tba);
	    	for(int i=data.size() - wait; i<data.size()-area; i++) {
	    		//Store the area variables for convenience
	    		int cur = data.get(i).getBrightness();
	    		int prev = data.get(i-1).getBrightness();
	    		int next = data.get(i+1).getBrightness();
	    		
	    		//Search the surrounding area
		    	boolean peak = true;
		    	for(int j=i-area;j<i+area;j++) {
		    		if(cur > data.get(j).getBrightness()) {
		    			peak = false;
		    			break;
		    		}
		    	}
		    	if(peak) {
		    		peaks.add(data.get(i));
		    	}
	    	}
	    	
	    	try {
		    	// Now we calculate the beat timing
		    	float timeDelta = (float)(peaks.get(peaks.size()-1).getTime()-peaks.get(0).getTime()); 
		    	float tpb = timeDelta/(float)peaks.size(); //Time in milliseconds per beat
		    	
		    	//Calculate BPM
		    	//(1 minute in milliseconds) / time per beat
		    	float bpm = (1000*60)/tpb;
		    	
		    	bpm -= 10.0f; // Tuning parameter
		    	bpm = Math.max(1, bpm);
		    	
		    	
		    	//hrText.setText(Math.round(bpm)+" bpm" + " TBA - "+tba+" Area: "+area);
		    	hrText.setText(Math.round(bpm)+" bpm");
		    	//Log.d("BPM",""+bpm);
		    	//graph.drawPeaks(peaks);
		    	
		    	oldBPM = bpm;
		    	return bpm;
	    	} catch (Exception e) {
	    		// The GC seems to dump the peaks array whilst trying to read it causing random crashes.
	    		// So it'll return the old value and will stop this happening hopefully!
	    		return oldBPM; // Just in case
	    	}
    	} else {
    		// This is before it works out.
    		if(area == -1 && data.size() > 10+area) {
    			// First wait for things to settle down
    			long targetMS = 200;
    			long currentMS = 0;
    			long firstMS = 0;
    			int nArea = 0;
    			
    			long startMS = data.get(data.size()-1).getTime();
    			for(int i=data.size()-1;i>=0;i--) {
    				long tmpMS = data.get(i).getTime();
    				currentMS = startMS - tmpMS;
            		Log.d("CMS","CMS: "+currentMS);
    				
    				if( currentMS >= targetMS) {
    					area = data.size() - 1 - i;
    					break;
    				}
    			}
    		}
    	}
    	return -1.0f; //Just in case
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.running_view, menu);
        return true;
    }
    
    public void stopRunning() {
    	Toast toast = Toast.makeText(getApplicationContext(), "Processing Run", Toast.LENGTH_LONG);
    	toast.show();
    	String file = storeRun();
    	Intent intentStopRunning = new Intent(this,PostRunView.class);
    	intentStopRunning.putExtra("file", file);
    	startActivity(intentStopRunning);
    }
    
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            /*
            b.setMessage("You have a camera! Don't you look nice today.");
            AlertDialog a = b.create();
            a.show();
            */
            return true;
        } else {
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setMessage("Sorry! It appears you don't have a camera. It is unlikely this app will work for you.");
            AlertDialog a = b.create();
            a.show();
            return false;
        }
    }
    
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    
    private String storeRun() {
    	String file = "";
    	if (!gps.getMapDataPoints().isEmpty()) {
    		Date d = new Date(gps.getMapDataPoints().get(0).getTime());
    		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy hh:mm a");
    		
    		try {
				file = Long.toString(new SimpleDateFormat("dd/mm/yy hh:mm a").parse(sdf.format(d)).getTime()) + ".xml";
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    		
    		FileOutputStream fos;
			try {
				fos = openFileOutput(file, Context.MODE_PRIVATE);
				String target = "<document target=\"" + Float.toString(targetBPM) + "\">";
				fos.write(target.getBytes());
				for (MapDataPoint m : gps.getMapDataPoints()) {
					fos.write(m.getXml().getBytes());
				}
				fos.write("</document>".getBytes());
	    		fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    		
    	}
    	return file;
    }
}
