package com.local.heartrunning;

import java.io.OutputStream;
import java.util.ArrayList;

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

public class RunningView extends Activity {

	// GUI Components
	Button finishRunningButton;
	TextView hrText;
	GraphView graph;
	
	// Other stuff
	GPSManager gps;	
	private Camera mCamera;
    private CameraPreview mPreview;
    
    // Data
    ArrayList<DataPoint> data;
    
    int runningAverage = -1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
        // Initialize the data
        data = new ArrayList<DataPoint>();
        
        // Initialize the GPS view
        gps = new GPSManager(this);
        
        // Load GUI Components
        finishRunningButton = (Button)findViewById(R.id.button_finish_run);
        finishRunningButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopRuning();
			}
		});
        
        hrText = (TextView)findViewById(R.id.hrText);
        graph = (GraphView)findViewById(R.id.heart_rate_graph);
        graph.linkData(data);
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        // The following turns the flash on
        Parameters p = mCamera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(p);

        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mPreview.linkParent(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);  
    }
    
    //public void processImage(Bitmap bitmap) {
    public void processImage(byte[] imageData, int width, int height) {
    	int total = 0;
    	
    	//Loop through the first width*height which are apparently
    	//http://stackoverflow.com/questions/5272388/need-help-with-androids-nv21-format 
    	//The last comment says this
    	//TODO - Check the cast does something remotely useful
    	
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
    	Log.d("running",""+runningAverage);
    	
    	//hrText.setText(avgBrightness+" bpm");
    	Log.d("avg",""+avgBrightness);
    	data.add(new DataPoint(null, avgBrightness));
    	graph.invalidate();
    	calculateBPM();
    }
    
    public int getSmoothedPoint(int index) {
    	int smoothingRange = 2;
    	int total = 0;
    	for(int i=index-smoothingRange; i<index+smoothingRange; i++) {
    		total = data.get(i).getBrightness();
    	}
    	return Math.round((float)total/(2*smoothingRange+1));
    }
    
    public void calculateBPM() {
    	int area = 6;
    	if(data.size() > 50 + area) {  
    		ArrayList<DataPoint> peaks = new ArrayList<DataPoint>();
	    	boolean goingUp = false;
	    	for(int i=data.size() - 50; i<data.size()-area; i++) {
	    		//Store the area variables for convenience
	    		int cur = data.get(i).getBrightness();
	    		int prev = data.get(i-1).getBrightness();
	    		int next = data.get(i+1).getBrightness();
	    		
	    		//We're going up
	    		//if(cur > (this.runningAverage*1.5)) {
		    		//if(cur > prev && cur > next) {
		    			//peaks.add(data.get(i));
		    		//}
	    		//}
		    		
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
	    	
	    	//Now calculate the average time period between peaks
	    	DataPoint oldI = null;
	    	int totalDiff = 0;
	    	for(DataPoint i : peaks) {
	    		if(oldI != null) {
	    			totalDiff += (i.getTime()-oldI.getTime());
	    		}
	    		oldI = i;
	    	}
	    	
	    	float bpmillisecond;
	    	if(peaks.size() > 0) {
	    		bpmillisecond = totalDiff/peaks.size();
	    	} else {
	    		bpmillisecond = 1;
	    	}
	    	float bpm = bpmillisecond/(1000*60);
	    	hrText.setText(bpm+" bpm");
	    	Log.d("BPM",""+bpm);
	    	graph.drawPeaks(peaks);
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.running_view, menu);
        return true;
    }
    
    public void stopRuning() {
    	Intent intentStopRunning = new Intent(this,PostRunView.class);
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
        	/*
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setMessage("Sorry this has no camera");
            AlertDialog a = b.create();
            a.show();
            */
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
}
