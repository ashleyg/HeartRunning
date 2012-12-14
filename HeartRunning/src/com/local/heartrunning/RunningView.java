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
    ArrayList<Integer> data;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
        // Initialize the data
        data = new ArrayList<Integer>();
        
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
    	//hrText.setText(avgBrightness+" bpm");
    	//Log.d("BPM",""+avgBrightness);
    	data.add(Integer.valueOf(avgBrightness));
    	graph.invalidate();
    	calculateBPM();
    }
    
    public void calculateBPM() {
    	if(data.size() > 51) {  
    		ArrayList<Integer> peaks = new ArrayList<Integer>();
	    	boolean goingUp = false;
	    	for(int i=data.size() - 50; i<data.size()-1; i++) {
	    		//Store the area variables for convenience
	    		int cur = data.get(i);
	    		int prev = data.get(i-1);
	    		int next = data.get(i+1);
	    		
	    		//We're going up
	    		if(cur > prev) {
	    			goingUp = true;
	    		}
	    		
	    		//We're at a peak???
	    		if(goingUp && next < cur) {
	    			goingUp = false;
	    			peaks.add(Integer.valueOf(i));
	    		}
	    	}
	    	
	    	//Now calculate the average time period between peaks
	    	Integer oldI = null;
	    	int totalDiff = 0;
	    	for(Integer i : peaks) {
	    		if(oldI != null) {
	    			totalDiff += (i-oldI);
	    		}
	    		oldI = i;
	    	}
	    	float bpm = totalDiff/peaks.size();
	    	hrText.setText(bpm+" bpm");
	    	Log.d("BPM",""+bpm);
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
