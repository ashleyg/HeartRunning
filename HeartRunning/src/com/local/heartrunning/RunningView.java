package com.local.heartrunning;

import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RunningView extends Activity {

	// GUI Components
	Button finishRunningButton;
	TextView hrText;
	
	// Other stuff
	GPSManager gps;
	
	private Camera mCamera;
    private CameraPreview mPreview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_view);
        
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
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        
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
    	
    	double avgBrightness = total / (width*height);
    	hrText.setText(avgBrightness+" bpm");
    	Log.d("BPM",""+avgBrightness);
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
